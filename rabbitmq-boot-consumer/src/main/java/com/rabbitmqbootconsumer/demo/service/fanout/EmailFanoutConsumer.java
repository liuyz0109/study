package com.rabbitmqbootconsumer.demo.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description 配置实现fanout
 */

@Component
@RabbitListener(queues = {"email.fanout.queue"})
public class EmailFanoutConsumer {

    @RabbitHandler
    public void receiveMsg(String msg) {
        System.out.println("email-fanout接收到消息：" + msg);
    }

}
