package com.rabbitmqorder.rabbitmqorder.service;

import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@Component
public class OrderServiceImpl {

    @Autowired
    private JDBCService jdbcService;

    @Transactional(rollbackFor = Exception.class)
    public void sendOrder(Order order) throws Exception {
        //保存订单数据
        int i = jdbcService.saveOrder(order);
        if (1 != i) {
            throw new Exception("订单创建失败，保存订单问题");
        }
        //调用配送服务 dispatcher
        String result = dispatcherHttpApi(order.getOrderId());
        if (!"success".equals(result)) {
            throw new Exception("订单创建失败，配送订单问题");
        }
    }

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
