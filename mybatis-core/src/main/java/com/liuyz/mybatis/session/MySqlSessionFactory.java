package com.liuyz.mybatis.session;

import com.liuyz.mybatis.mapping.MyConfiguration;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/3/1
 * @description 自定义sqlSessionFactory
 */
public class MySqlSessionFactory {

    private MyConfiguration myConfiguration;

    public MySqlSessionFactory(MyConfiguration myConfiguration) {
        this.myConfiguration = myConfiguration;
    }
}
