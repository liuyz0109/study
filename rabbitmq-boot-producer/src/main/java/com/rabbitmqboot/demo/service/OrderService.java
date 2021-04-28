package com.rabbitmqboot.demo.service;

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
    public void makeOrder(String userid, String goodid, Integer num) {
        //查询商品库存量
        //订单编号
        String orderid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("订单生成成功：" + orderid);
        String exchangeName = "fanout_order_exchange";
        String routingKey = "";
        //发送消息到mq
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderid);
    }

}
