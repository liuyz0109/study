package com.rabbitmqbootconsumer.demo.service.topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 注解实现topic
 */

@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "wechat.topic.queue",durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = "topic_order_exchange",type = ExchangeTypes.TOPIC),
        key = "#.wechat.*"
))
public class WechatTopicConsumer {

    @RabbitHandler
    public void receiveMsg(String msg) {
        System.out.println("wechat-topic接收到消息：" + msg);
    }

}
