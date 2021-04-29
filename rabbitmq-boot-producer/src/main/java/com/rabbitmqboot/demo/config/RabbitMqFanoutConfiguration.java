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
 * @description rabbitmq配置类-fanout
 * 配置类可以配置在生产者也可以配置在消费者
 */

@Configuration
public class RabbitMqFanoutConfiguration {

    //声明交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_order_exchange", true, false);
    }

    //声明队列
    @Bean
    public Queue smsQueueFanout() {
        return new Queue("sms.fanout.queue", true);
    }

    @Bean
    public Queue emailQueueFanout() {
        return new Queue("email.fanout.queue", true);
    }

    @Bean
    public Queue wechatQueueFanout() {
        return new Queue("wechat.fanout.queue", true);
    }

    //绑定关系
    @Bean
    public Binding smsBindingFanout() {
        return BindingBuilder.bind(smsQueueFanout()).to(fanoutExchange());
    }

    @Bean
    public Binding emailBindingFanout() {
        return BindingBuilder.bind(emailQueueFanout()).to(fanoutExchange());
    }

    @Bean
    public Binding wechatBindingFanout() {
        return BindingBuilder.bind(wechatQueueFanout()).to(fanoutExchange());
    }

}
