package com.rabbitmqdispatcher.rabbitmqdispatcher.controller;

import com.rabbitmqdispatcher.rabbitmqdispatcher.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */

@RequestMapping("/dispatch")
@Controller
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @ResponseBody
    @RequestMapping("/order")
    public Object order(String orderId) throws InterruptedException {
        String result = dispatcherService.saveOrder(orderId);
        // 设置系统休眠时间，因为order中设置了延迟时间为2秒，此处若设置休眠3秒，order会报错
        // 此时订单数据保存，派送信息回滚，造成数据不一致
//        Thread.sleep(3000);
        return result;
    }

}
