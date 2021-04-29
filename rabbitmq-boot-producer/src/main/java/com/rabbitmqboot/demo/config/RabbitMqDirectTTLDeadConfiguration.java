package com.rabbitmqboot.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/4/28
 * @description rabbitmq配置类-direct-ttl-dead
 * 配置类可以配置在生产者也可以配置在消费者
 */

@Configuration
public class RabbitMqDirectTTLDeadConfiguration {

    //声明交换机
    @Bean
    public DirectExchange directTTLDeadExchange() {
        return new DirectExchange("ttl_dead_direct_exchange", true, false);
    }

    //声明队列
    @Bean
    public Queue ttlDeadQueueDirect() {
        return new Queue("ttl.dead.direct.queue", true);
    }

    //绑定关系
    @Bean
    public Binding bindingDirectTTLDead() {
        return BindingBuilder.bind(ttlDeadQueueDirect()).to(directTTLDeadExchange()).with("ttlDead");
    }

}
