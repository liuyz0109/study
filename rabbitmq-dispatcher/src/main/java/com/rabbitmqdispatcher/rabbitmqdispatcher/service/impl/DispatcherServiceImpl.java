package com.rabbitmqdispatcher.rabbitmqdispatcher.service.impl;

import com.rabbitmqdispatcher.rabbitmqdispatcher.service.DispatcherService;
import com.rabbitmqdispatcher.rabbitmqdispatcher.service.JDBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@Service
public class DispatcherServiceImpl implements DispatcherService {

    @Autowired
    private JDBCService jdbcService;

    @Override
    public String saveOrder(String orderId) {
        //保存数据至数据库
        int i = jdbcService.saveDispatcher(orderId);
        if (i == 1) {
            return "success";
        }
        return "false";
    }
}
