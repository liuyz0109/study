package com.liuyz.esapi;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/22
 * @description es集群测试类
 */

@SpringBootTest
public class EsClusterTests {

    @Autowired
    @Qualifier("restHighLevelClusterClient")
    private RestHighLevelClient client;

    //创建索引
    @Test
    void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("index_cluster");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 1));
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    //更新索引配置
    @Test
    void updateIndex() throws IOException {
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
        //动态修改副本分片数量
        Settings settings = Settings.builder().put("index.number_of_replicas", 2).build();
        updateSettingsRequest.settings(settings);
        AcknowledgedResponse acknowledgedResponse = client.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    //更新索引配置
    @Test
    void updateIndex2() throws IOException {
        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
        //设置插入数据时的同步参数
        Settings settings = Settings.builder().put("index.write.wait_for_active_shards", "all").build();
        updateSettingsRequest.settings(settings);
        AcknowledgedResponse acknowledgedResponse = client.indices().putSettings(updateSettingsRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    //插入数据
    @Test
    void insertData() throws IOException {
        IndexRequest request = new IndexRequest("index_cluster");
//        request.id("111");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王五");
        map.put("age", 14);
        request.source(JSON.toJSONString(map), XContentType.JSON);
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println("version:" + indexResponse.getVersion());
        System.out.println("seqNo:" + indexResponse.getSeqNo());
        System.out.println("primaryTerm:" + indexResponse.getPrimaryTerm());
    }

    //刷新文档
    @Test
    void refresh() throws IOException {
        RefreshRequest refreshRequest = new RefreshRequest("index_cluster");
        RefreshResponse refreshResponse = client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
        System.out.println(refreshResponse.getStatus());
    }

    //更新数据
    @Test
    void updateData() throws IOException {
        UpdateRequest request = new UpdateRequest("index_cluster", "111");
        request.setIfSeqNo(2);
        request.setIfPrimaryTerm(2);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王五");
        request.doc(JSON.toJSONString(map), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println("result:" + updateResponse.getResult());
    }

    //获取数据
    //version conflict, current version [2] is different than the one provided [1]
    @Test
    void getData() throws IOException {
        GetRequest getRequest = new GetRequest("index_cluster", "111");
        getRequest.versionType(VersionType.EXTERNAL);
        getRequest.version(2);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.isExists());
    }

    //更新数据
    //java.lang.UnsupportedOperationException: update requests do not support versioning
    @Test
    void updateData2() throws IOException {
        UpdateRequest request = new UpdateRequest("index_cluster", "111");
        request.versionType(VersionType.EXTERNAL);
        request.version(4);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王五");
        request.doc(JSON.toJSONString(map), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getResult());
    }

}
