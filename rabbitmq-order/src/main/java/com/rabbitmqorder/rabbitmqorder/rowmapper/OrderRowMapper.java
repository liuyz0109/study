package com.rabbitmqorder.rabbitmqorder.rowmapper;

import com.rabbitmqorder.rabbitmqorder.pojo.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/7
 * @description
 */
public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        String orderId = resultSet.getString("order_id");
        String userId = resultSet.getString("user_id");
        String text = resultSet.getString("text");
        String status = resultSet.getString("status");
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setText(text);
        order.setStatus(Integer.parseInt(status));
        return order;
    }
}
