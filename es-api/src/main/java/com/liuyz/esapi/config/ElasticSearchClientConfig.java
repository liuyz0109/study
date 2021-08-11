package com.liuyz.esapi.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/13
 * @description
 */

@Configuration
public class ElasticSearchClientConfig {

//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        return new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("127.0.0.1", 9200, "http")));
//    }

    @Bean
    public RestHighLevelClient restHighLevelClusterClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.141.101", 9200, "http")
                )
        );
    }

}
