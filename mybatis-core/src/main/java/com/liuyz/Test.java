package com.liuyz;

import com.liuyz.mybatis.session.MySqlSessionFactory;
import com.liuyz.mybatis.session.MySqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/3/1
 * @description 测试类
 */
public class Test {

    public static void main(String[] args) {

        //读取xml配置文件
        InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("mybatis-config.xml");

        //构建sqlSessionFactory
        MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactoryBuilder().build(inputStream);

        System.out.println("读取结束");
    }

}
