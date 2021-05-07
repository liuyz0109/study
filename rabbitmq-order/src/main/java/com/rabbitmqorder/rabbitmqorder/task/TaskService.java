package com.rabbitmqorder.rabbitmqorder.task;

import com.alibaba.fastjson.JSON;
import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import com.rabbitmqorder.rabbitmqorder.service.JDBCService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description 使用定时器循环往mq发送交换机未成功接收的数据
 */

@EnableScheduling
@Component
public class TaskService {

    @Autowired
    private JDBCService jdbcService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendMsg() {
        //查询冗余表中status为0的数据，进行发送
        List<Order> orders = jdbcService.getOrderByStatusIsZero();
        for (Order order : orders) {
            rabbitTemplate.convertAndSend("order.fanout.exchange", "", JSON.toJSONString(order),new CorrelationData(order.getOrderId()));
        }
        System.out.println("计时器执行！");
    }

}
