package com.demo.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/20
 * @description
 */

@RestController
public class MyController {

    @ResponseBody
    @PostMapping("/receiver/getInfos")
    public Object getInfos(@RequestBody String jsonStr) {

        System.out.println(jsonStr);

        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("msg", "请求核验成功，请耐心等待回传核验结果！");

        return map;
//        return null;
    }

}
