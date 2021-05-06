package com.rabbitmqorder.rabbitmqorder.service;

import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@Service
public class JDBCService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int saveOrder(Order order) {
        String sql = "insert into order_master values (?,?,?,?)";
        return jdbcTemplate.update(sql,order.getOrderId(),order.getUser_id(),order.getText(),order.getStatus());
    }

}
