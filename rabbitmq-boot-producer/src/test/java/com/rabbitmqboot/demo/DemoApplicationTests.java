package com.rabbitmqboot.demo;

import com.rabbitmqboot.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        orderService.makeOrder("1", "1", 10);
    }

}
