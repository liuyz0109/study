package com.rabbitmqdispatcher.rabbitmqdispatcher.service;

import com.rabbitmqdispatcher.rabbitmqdispatcher.pojo.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@Component
public class JDBCService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveDispatcher(String orderId) {
        String sql = "insert into dispatcher values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, UUID.randomUUID().toString().replaceAll("-", ""), orderId, "买了一本书", "11111",new Date());
    }

    public int saveDispatcherForMQ(Dispatcher dispatcher) {
        String sql = "insert into dispatcher values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, dispatcher.getDispatcherID(), dispatcher.getOrderId(), dispatcher.getText(), dispatcher.getUserId(), dispatcher.getDate());
    }

}
