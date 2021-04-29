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
    void fanoutTest() {
        orderService.makeOrderFanout("1", "1", 10);
    }

    @Test
    void directTest() {
        orderService.makeOrderDirect("1", "1", 10);
    }

    @Test
    void topicTest() {
        orderService.makeOrderTopic("1", "1", 10);
    }

    @Test
    void directTTLTest() {
        orderService.makeOrderDirectTTL("1", "1", 10);
    }

    @Test
    void directTTLMsgTest() {
        orderService.makeOrderDirectTTLMsg("1", "1", 10);
    }

    @Test
    void directTTLDeadTest() {
        orderService.makeOrderDirectTTLDead("1", "1", 10);
    }

    @Test
    void directTTLDeadLimitTest() {
        for (int i = 0; i < 12; i++) {
            orderService.makeOrderDirectTTLDead("1", "1", 10);
        }
    }

}
