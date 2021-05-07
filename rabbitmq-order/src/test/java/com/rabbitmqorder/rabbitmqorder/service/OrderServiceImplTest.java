package com.rabbitmqorder.rabbitmqorder.service;

import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    void sendOrder() throws Exception {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-",""));
        order.setUserId("10000");
        order.setText("买了一本书");
        order.setStatus(0); //0临时
        orderService.sendOrder(order);
    }
}