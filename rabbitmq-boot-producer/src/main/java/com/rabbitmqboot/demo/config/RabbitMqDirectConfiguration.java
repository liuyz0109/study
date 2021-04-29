package com.rabbitmqboot.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description rabbitmq配置类-direct
 * 配置类可以配置在生产者也可以配置在消费者
 */

@Configuration
public class RabbitMqDirectConfiguration {

    //声明交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct_order_exchange", true, false);
    }

    //声明队列
    @Bean
    public Queue smsQueueDirect() {
        return new Queue("sms.direct.queue", true);
    }

    @Bean
    public Queue emailQueueDirect() {
        return new Queue("email.direct.queue", true);
    }

    @Bean
    public Queue wechatQueueDirect() {
        return new Queue("wechat.direct.queue", true);
    }

    //绑定关系
    @Bean
    public Binding smsBindingDirect() {
        return BindingBuilder.bind(smsQueueDirect()).to(directExchange()).with("sms");
    }

    @Bean
    public Binding emailBindingDirect() {
        return BindingBuilder.bind(emailQueueDirect()).to(directExchange()).with("email");
    }

    @Bean
    public Binding wechatBindingDirect() {
        return BindingBuilder.bind(wechatQueueDirect()).to(directExchange()).with("wechat");
    }

}
