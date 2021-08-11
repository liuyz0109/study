package com.liuyz.esjd.controller;

import com.liuyz.esjd.service.JdGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/16
 * @description 京东商品控制层
 */

@RestController
public class JdGoodsController {

    @Autowired
    private JdGoodsService jdGoodsService;

    @GetMapping("/parse/{keyword}")
    public Object parse(@PathVariable("keyword") String keyword) throws IOException {
        return jdGoodsService.parse(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public Object search(@PathVariable("keyword") String keyword,@PathVariable("pageNo") int pageNo,@PathVariable("pageSize") int pageSize) throws IOException {
        return jdGoodsService.search(keyword, pageNo, pageSize);
    }

}
