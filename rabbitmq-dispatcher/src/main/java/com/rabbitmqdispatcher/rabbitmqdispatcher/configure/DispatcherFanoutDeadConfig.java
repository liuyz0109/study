package com.rabbitmqdispatcher.rabbitmqdispatcher.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/7
 * @description 配送fanout死信
 */

@Configuration
public class DispatcherFanoutDeadConfig {

    @Bean
    public FanoutExchange dispatcherFanoutExchange() {
        return new FanoutExchange("dispatcher.fanout.exchange", true, false);
    }

    @Bean
    public FanoutExchange dispatcherFanoutDeadExchange() {
        return new FanoutExchange("dispatcher.fanout.dead.exchange", true, false);
    }

    @Bean
    public Queue dispatcherFanoutQueue() {
        //绑定死信
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dispatcher.fanout.dead.exchange");
        return new Queue("dispatcher.fanout.queue", true,false,false,args);
    }

    @Bean
    public Queue dispatcherFanoutDeadQueue() {
        return new Queue("dispatcher.fanout.dead.queue", true);
    }

    @Bean
    public Binding dispatcjerFanoutBinding() {
        return BindingBuilder.bind(dispatcherFanoutQueue()).to(dispatcherFanoutExchange());
    }

    @Bean
    public Binding dispatcherFanoutDeadBinding() {
        return BindingBuilder.bind(dispatcherFanoutDeadQueue()).to(dispatcherFanoutDeadExchange());
    }

}
