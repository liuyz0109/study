package com.rabbitmqorder.rabbitmqorder.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@Component
public class OrderServiceImpl {

    @Autowired
    private JDBCService jdbcService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //使用mq做分布式事务，将本地事务取消
    //@Transactional(rollbackFor = Exception.class)
    public void sendOrder(Order order) throws Exception {
        //保存订单数据，同步保存数据到冗余表
        int i = jdbcService.saveOrder(order);
        if (1 != i) {
            throw new Exception("订单创建失败，保存订单问题");
        }
//        //调用配送服务 dispatcher
//        String result = dispatcherHttpApi(order.getOrderId());
//        if (!"success".equals(result)) {
//            throw new Exception("订单创建失败，配送订单问题");
//        }
        //数据发rabbitmq，并在交换机接收到消息后执行回调机制，发送消息时需要指定回调数据的标记id，最好使用主键，方便后续对数据的修改
        //当消息发送到交换机中，会调用该方法，通过获取CorrelationData的标记id，即上一步指定的数据的主键，
        //可以根据主键修改状态status的操作，代表数据已被发送到rabbitmq，
        //之后使用定时器循环发送的消息就可以根据该状态status的值来确认哪些消息未发送，哪些已发送。
        rabbitTemplate.convertAndSend("order.fanout.exchange", "", JSON.toJSONString(order),new CorrelationData(order.getOrderId()));
    }

    //创建rabbitmq确认回调机制
    //被PostConstruct修饰的方法regCallBack()，会在加载Servlet的时候执行，且只会执行一次
    //构造函数之后，init方法之前执行（所有的Bean加载完成）
    @PostConstruct
    public void regCallBack() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                String id = correlationData.getId();
                //b为true，代表消息已接收
                if (!b) {
                    System.out.println("消息接收失败，orderID为：" + id);
                    return;
                }
                //修改冗余表的状态为1，已接收
                try {
                    String sql = "update order_master_back set status = 1 where order_id = ?";
                    int update = jdbcTemplate.update(sql, id);
                    if (1 == update) {
                        System.out.println("状态修改为1，已接受，订单id：" + id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("状态修改失败，订单id：" + id);
                }
            }
        });
    }

    //通过httpRequestFactory完成方法调用
    private String dispatcherHttpApi(String orderId) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置连接时间
        factory.setConnectTimeout(3000);
        //设置超时时间
        factory.setReadTimeout(2000);
        String url = "http://192.168.0.168:8085/dispatch/order?orderId=" + orderId;
        RestTemplate restTemplate = new RestTemplate(factory);
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

}
