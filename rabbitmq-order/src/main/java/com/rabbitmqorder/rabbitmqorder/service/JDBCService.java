package com.rabbitmqorder.rabbitmqorder.service;

import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import com.rabbitmqorder.rabbitmqorder.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        saveOrderToBack(order);
        String sql = "insert into order_master values (?,?,?,?)";
        return jdbcTemplate.update(sql,order.getOrderId(),order.getUserId(),order.getText(),order.getStatus());
    }

    //保存数据至冗余表，来完成可靠生产
    private void saveOrderToBack(Order order) {
        String sql = "insert into order_master_back values (?,?,?,?)";
        jdbcTemplate.update(sql,order.getOrderId(),order.getUserId(),order.getText(),order.getStatus());
    }

    //查询冗余表中状态为0的数据
    public List<Order> getOrderByStatusIsZero() {
        String sql = "select * from order_master_back where status = 0";
        List<Order> query = jdbcTemplate.query(sql, new OrderRowMapper());
        return query;
    }

}
