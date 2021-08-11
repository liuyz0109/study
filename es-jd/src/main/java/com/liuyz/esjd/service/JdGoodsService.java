package com.liuyz.esjd.service;

import com.alibaba.fastjson.JSON;
import com.liuyz.esjd.pojo.JdGoods;
import com.liuyz.esjd.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/16
 * @description 京东商品业务层
 */

@Service
public class JdGoodsService {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * @param keyword 关键字
     * @return 存入结果
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 存入数据
     */
    public Boolean parse(String keyword) throws IOException {
        List<JdGoods> jdGoodsList = HtmlParseUtil.parseJd(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("30s");
        for (int i = 0; i < jdGoodsList.size(); i++) {
            bulkRequest.add(new IndexRequest("jd_goods").source(JSON.toJSONString(jdGoodsList.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    /**
     * @param keyword  关键字
     * @param pageNo   起始页
     * @param pageSize 分页显示数量
     * @return 查询结果
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 分页查询
     */
    public List<Map<String, Object>> search(String keyword, int pageNo, int pageSize) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //超时
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        if (pageNo < 1) {
            pageNo = 1;
        }
        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color: red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.requireFieldMatch(false); //多个匹配只高亮一次
        searchSourceBuilder.highlighter(highlightBuilder);

        //精准匹配
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", keyword);
//        searchSourceBuilder.query(termQueryBuilder);

        //模糊匹配
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", keyword);
        searchSourceBuilder.query(matchQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : search.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            HighlightField highlightField = hit.getHighlightFields().get("name");
            //替换高亮字段
            if (null != highlightField) {
                sourceAsMap.put("name", highlightField.fragments()[0].toString());
            }
            list.add(sourceAsMap);
        }
        return list;
    }

}
