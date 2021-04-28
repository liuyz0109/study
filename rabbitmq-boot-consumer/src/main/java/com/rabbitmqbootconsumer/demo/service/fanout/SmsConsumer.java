package com.rabbitmqbootconsumer.demo.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description
 */

@Component
@RabbitListener(queues = {"sms.fanout.queue"})
public class SmsConsumer {

    @RabbitHandler
    public void receiveMsg(String msg) {
        System.out.println("sms接收到消息：" + msg);
    }

}
