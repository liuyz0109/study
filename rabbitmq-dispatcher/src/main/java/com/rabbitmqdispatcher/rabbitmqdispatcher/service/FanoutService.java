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
 * @version 1.0, 2021/5/6
 * @description
 * 当消费者消费消息出现异常时，会将消息重新退回给MQ，但此时的MQ就会有消息，
 * 消费者又会去消费消息，再次执行到异常，再退回消息，造成死循环。
 * 解决方案：
 * 1、控制重发的次数（使用重试机制）
 * 2、try+catch+手动ack
 * 3、try+catch+手动ack+死信队列+人工干预
 */

@Service
public class FanoutService {

    @Autowired
    private JDBCService jdbcService;

    /** 使用重试机制（不能使用try\catch，不然重试机制会失效，发生死循环）
     * retry:
          enabled: true # 开启重试配置，默认为false
          max-attempts: 5 # 重试次数，默认为3
          initial-interval: 2000ms # 重试间隔时间
     */
//    @RabbitListener(queues = {"queue1"})
//    public void receiveOrder(String msg) {
//        //字符串转json
//        Order order = JSON.parseObject(msg, Order.class);
//        //封装派单数据
//        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setDispatcherID(UUID.randomUUID().toString().replaceAll("-", ""));
//        dispatcher.setOrderId(order.getOrderId());
//        dispatcher.setText(order.getText());
//        dispatcher.setUserId(order.getUserId());
//        dispatcher.setDate(new Date());
//        //保存数据
//        int i = jdbcService.saveDispatcherForMQ(dispatcher);
//        if (i == 1) {
//            System.out.println("派单数据保存成功，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
//        }else {
//            System.out.println("派单数据保存失败，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
//        }
//        System.out.println(1/0);
//    }

    /** 使用try+catch+手动ack
     * acknowledge-mode: manual # 开启手动ack，默认为none，让程序控制消息的重发、删除、转移
     */
//    @RabbitListener(queues = {"queue1"})
//    public void receiveOrder(String msg, Channel channel, CorrelationData correlationData,
//                             @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
//        try {
//            //字符串转json
//            Order order = JSON.parseObject(msg, Order.class);
//            //封装派单数据
//            Dispatcher dispatcher = new Dispatcher();
//            dispatcher.setDispatcherID(UUID.randomUUID().toString().replaceAll("-", ""));
//            dispatcher.setOrderId(order.getOrderId());
//            dispatcher.setText(order.getText());
//            dispatcher.setUserId(order.getUserId());
//            dispatcher.setDate(new Date());
//            //保存数据
//            int i = jdbcService.saveDispatcherForMQ(dispatcher);
//            if (i == 1) {
//                System.out.println("派单数据保存成功，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
//            } else {
//                System.out.println("派单数据保存失败，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
//            }
//            System.out.println(1 / 0); //出现异常
//            //设置默认ack
//            channel.basicAck(tag, false);
//        } catch (Exception e) {
//            //设置nack
//            //参数1：消息的tag
//            //参数2：是否多条处理
//            //参数3：是否重发，不重发则将消息打到死信中，未设置死信会造成消息丢失；重发则将消息发送回队列，此时如果用try\catch的话，会造成死循环
//            channel.basicNack(tag, false, false);
//        }
//    }

    /** 使用try+catch+手动ack+死信
     * acknowledge-mode: manual # 开启手动ack，默认为none，让程序控制消息的重发、删除、转移
     * 在消费者队列中绑定死信交换机，当生产者生产消息发送到消费者交换机，
     * 消费者消费队列中的消息，在处理消息中发生异常，执行nack（即在发生异常时将消息发送给死信），
     * 再通过另一个业务去消费死信队列中的消息，完成闭环。
     */
    @RabbitListener(queues = {"dispatcher.fanout.queue"})
    public void receiveOrder(String msg, Channel channel, CorrelationData correlationData,
                             @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            //字符串转json
            Order order = JSON.parseObject(msg, Order.class);
            //封装派单数据
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setDispatcherID(UUID.randomUUID().toString().replaceAll("-", ""));
            dispatcher.setOrderId(order.getOrderId());
            dispatcher.setText(order.getText());
            dispatcher.setUserId(order.getUserId());
            dispatcher.setDate(new Date());
            //保存数据
            int i = jdbcService.saveDispatcherForMQ(dispatcher);
            if (i == 1) {
                System.out.println("派单数据保存成功，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
            } else {
                System.out.println("派单数据保存失败，派单id：" + dispatcher.getDispatcherID() + "，订单id：" + dispatcher.getOrderId());
            }
            System.out.println(1 / 0); //出现异常
            //设置默认ack
            channel.basicAck(tag, false);
        } catch (Exception e) {
            //设置nack
            //参数1：消息的tag
            //参数2：是否多条处理
            //参数3：是否重发，不重发则将消息打到死信中，未设置死信会造成消息丢失；重发则将消息发送回队列，此时如果用try\catch的话，会造成死循环
            channel.basicNack(tag, false, false);
        }
    }

}
