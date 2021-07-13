package com.demo.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/6/21
 * @description
 */

@Controller
public class MyController {

    @GetMapping("/index")
    public String index() {
        System.out.println("请求进入页面");
        return "index";
    }

}
