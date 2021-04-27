package com.liuyz.mybatis.session;

import com.liuyz.mybatis.mapping.MyConfiguration;

import java.io.InputStream;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/3/1
 * @description 自定义sqlSessionFactoryBuilder
 */
public class MySqlSessionFactoryBuilder {

    public MySqlSessionFactory build(InputStream inputStream){

        //自定义的configuration，即为mybatis的配置对象，配置信息
        MyConfiguration myConfiguration = null;

        return new MySqlSessionFactory(myConfiguration);
    }

}
