package com.rabbitmqbootconsumer.demo.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 配置实现direct
 */

@Component
@RabbitListener(queues = {"sms.direct.queue"})
public class SmsDirectConsumer {

    @RabbitHandler
    public void receiveMsg(String msg) {
        System.out.println("sms-direct接收到消息：" + msg);
    }

}
