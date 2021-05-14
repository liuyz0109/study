package com.rabbitmqdispatcher.rabbitmqdispatcher.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmqdispatcher.rabbitmqdispatcher.pojo.Dispatcher;
import com.rabbitmqdispatcher.rabbitmqdispatcher.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/7
 * @description 死信消费者，需要考虑幂等性问题
 */

@Service
public class FanoutDeadService {

    @Autowired
    private JDBCService jdbcService;

    @RabbitListener(queues = {"dispatcher.fanout.dead.queue"})
    public void saveDeadDispatcher(String msg, Channel channel, CorrelationData correlationData,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            //转换死信消息的对象
            Order order = JSON.parseObject(msg, Order.class);
            //封装派单数据
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setDispatcherID(UUID.randomUUID().toString().replaceAll("-", ""));
            dispatcher.setOrderId(order.getOrderId());
            dispatcher.setText("死信：" + order.getText());
            dispatcher.setUserId(order.getUserId());
            dispatcher.setDate(new Date());
            //保存数据
            //需要考虑幂等性问题
            int i = jdbcService.saveDispatcherForMQ(dispatcher);
            if (i == 1) {
                System.out.println("死信派单数据保存成功，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
            } else {
                System.out.println("死信派单数据保存失败，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
            }
            System.out.println(1 / 0); //出现异常
            //设置默认ack
            channel.basicAck(tag, false);
        } catch (Exception e) {
            //死信中再发生错误，进行人工干预
            System.out.println("死信进行人工干预");
            //移除消息
            channel.basicNack(tag, false, false);
            System.out.println("死信删除消息");
        }
    }

}
