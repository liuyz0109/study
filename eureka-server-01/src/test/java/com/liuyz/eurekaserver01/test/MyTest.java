package com.liuyz.eurekaserver01.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/6/18
 * @description
 */

public class MyTest {

    public static void main(String[] args) {
        final String a = "1";
        int b = Integer.parseInt(a);
        b += b + 2;
        System.out.println(b);
    }

}
