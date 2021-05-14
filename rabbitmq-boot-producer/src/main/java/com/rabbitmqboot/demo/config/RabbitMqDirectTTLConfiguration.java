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
 * @description rabbitmq配置类-direct-ttl
 * 配置类可以配置在生产者也可以配置在消费者
 */

@Configuration
public class RabbitMqDirectTTLConfiguration {

    //声明交换机
    @Bean
    public DirectExchange directTTLExchange() {
        return new DirectExchange("ttl_direct_exchange", true, false);
    }

    @Bean
    public DirectExchange directTTLMsgExchange() {
        return new DirectExchange("ttl_msg_direct_exchange", true, false);
    }

    //声明队列
    @Bean
    public Queue ttlQueueDirect() {
        Map<String, Object> args = new HashMap<>();
        //传入TTL参数
        //时间必须为int类型
        args.put("x-message-ttl", 10000);
        //设置最大存储信息数量
        args.put("x-max-length", 5);
        //绑定死信交换机
        args.put("x-dead-letter-exchange", "ttl_dead_direct_exchange");
        //绑定死信队列key
        args.put("x-dead-letter-routing-key", "ttlDead");
        return new Queue("ttl.direct.queue", true,false,false,args);
    }

    @Bean
    public Queue ttlMsgQueueDirect() {
        Map<String, Object> args = new HashMap<>();
        return new Queue("ttl.msg.direct.queue", true);
    }

    //绑定关系
    @Bean
    public Binding bindingDirectTTL() {
        return BindingBuilder.bind(ttlQueueDirect()).to(directTTLExchange()).with("ttl");
    }

    @Bean
    public Binding bindingDirectTTLMsg() {
        return BindingBuilder.bind(ttlMsgQueueDirect()).to(directTTLMsgExchange()).with("ttlMsg");
    }

}
