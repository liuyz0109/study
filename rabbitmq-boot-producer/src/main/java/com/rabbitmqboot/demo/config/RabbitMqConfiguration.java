package com.rabbitmqboot.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description rabbitmq配置类
 */

@Configuration
public class RabbitMqConfiguration {

    //声明交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_order_exchange", true, false);
    }

    //声明队列
    @Bean
    public Queue smsQueue() {
        return new Queue("sms.fanout.queue", true);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email.fanout.queue", true);
    }

    @Bean
    public Queue wechatQueue() {
        return new Queue("wechat.fanout.queue", true);
    }

    //绑定关系
    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding wechatBinding() {
        return BindingBuilder.bind(wechatQueue()).to(fanoutExchange());
    }

}
