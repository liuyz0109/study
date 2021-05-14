package com.rabbitmqboot.demo.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 订单service
 */

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderFanout(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "fanout_order_exchange";
        String routingKey = "";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid);
    }

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderDirect(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "direct_order_exchange";
        String routingKey1 = "sms";
        String routingKey2 = "email";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey1, orderid);
        rabbitTemplate.convertAndSend(exchangeName, routingKey2, orderid);
    }

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderTopic(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "topic_order_exchange";
        //#：0或多级，可有可无；*：1级，有且仅有1个。
        //#.email.#
        //*.sms.*
        //#.wechat.*
        //email和sms队列可以接收到消息
        String routingKey = "email.sms.wechat";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid);
    }

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderDirectTTL(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "ttl_direct_exchange";
        String routingKey = "ttl";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid);
    }

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderDirectTTLMsg(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "ttl_msg_direct_exchange";
        String routingKey = "ttlMsg";
        //设置单独消息的TTL时间
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //TTL时间此时为String类型
                message.getMessageProperties().setExpiration("5000");
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            }
        };
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid,messagePostProcessor);
    }

    /**
     * @param userid 用户id
     * @param goodid 商品id
     * @param num 商品数量
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 创建订单
     */
    public void makeOrderDirectTTLDead(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "ttl_direct_exchange";
        String routingKey = "ttl";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid);
    }

}
