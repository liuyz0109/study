package com.liuyz.esapi;

import com.alibaba.fastjson.JSON;
import com.liuyz.esapi.pojo.UserPojo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/14
 * @description 文档api测试
 */

@SpringBootTest
public class DocumentApiTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //创建索引带文档（插入/修改）
    @Test
    void testIndexApi() throws IOException {
        //索引请求
        IndexRequest request = new IndexRequest("index_document");
        //设置文档id
        request.id("1");
        //设置文档内容并指定内容类型
        UserPojo userPojo = new UserPojo("张三", 15, new Date());
        request.source(JSON.toJSONString(userPojo), XContentType.JSON);
        //同步执行
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        //响应
        String index = indexResponse.getIndex();
        String id = indexResponse.getId();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("----------------插入数据");
            System.out.println("----------------index:" + index);
            System.out.println("----------------id:" + id);
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("----------------修改数据");
            System.out.println("----------------index:" + index);
            System.out.println("----------------id:" + id);
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("shardInfo.getTotal():" + shardInfo.getTotal());
            System.out.println("shardInfo.getSuccessful():" + shardInfo.getSuccessful());
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
                System.out.println(reason);
            }
        }
    }

    //批量插入文档api
    @Test
    void testBulkInsertApi() throws IOException {
        //批量请求
        BulkRequest bulkRequest = new BulkRequest();
        //设置超时时间
        bulkRequest.timeout("10s");
        //创建数据
        List<UserPojo> userPojoList = new ArrayList<>();
        userPojoList.add(new UserPojo("张三13", 31, new Date()));
        userPojoList.add(new UserPojo("张三14", 32, new Date()));
        userPojoList.add(new UserPojo("张三15", 33, new Date()));
        userPojoList.add(new UserPojo("张三16", 34, new Date()));
        userPojoList.add(new UserPojo("张三17", 35, new Date()));
        userPojoList.add(new UserPojo("张三18", 36, new Date()));
        for (int i = 12; i < userPojoList.size() + 12; i++) {
            bulkRequest.add(new IndexRequest("index_document").id(i + 1 + "").source(JSON.toJSONString(userPojoList.get(i - 12)), XContentType.JSON));
        }
        //同步执行，获取响应
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            System.out.println("------------------begin one---------------------");
            System.out.println("index:" + bulkItemResponse.getIndex());
            System.out.println("id:" + bulkItemResponse.getId());
            System.out.println("执行类型:" + bulkItemResponse.getOpType());
            System.out.println("-------------------over one---------------------");
        }
    }

    //获取文档api
    @Test
    void testGetApi() throws IOException {
        //获取请求
        GetRequest getRequest = new GetRequest("index_document", "1");
        //同步执行，获取响应
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        String index = getResponse.getIndex();
        String id = getResponse.getId();
        System.out.println("--------------index:" + index);
        System.out.println("--------------id:" + id);
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            Map<String, Object> source = getResponse.getSource();
            System.out.println("-----------------version:" + version);
            System.out.println("-----------------source:" + source);
        }
    }

    //更新文档api
    @Test
    void testUpdateApi() throws IOException {
        //更新请求
        UpdateRequest request = new UpdateRequest("index_document", "1");
        //封装内容
        UserPojo userPojo1 = new UserPojo();
        userPojo1.setAge(66);
        request.doc(JSON.toJSONString(userPojo1), XContentType.JSON);
        //同步执行
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        String index = updateResponse.getIndex();
        String id = updateResponse.getId();
        System.out.println("---------------------index:" + index);
        System.out.println("---------------------id:" + id);
        System.out.println("---------------------result:" + updateResponse.getResult());
        System.out.println("---------------------source:"+updateResponse.toString());
    }

    //删除文档api
    @Test
    void testDeleteDocument() throws IOException {
        //删除请求
        DeleteRequest request = new DeleteRequest("index_document", "1");
        //同步执行
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        String index = deleteResponse.getIndex();
        String id = deleteResponse.getId();
        System.out.println("------------------index:" + index);
        System.out.println("------------------id:" + id);
    }

}
