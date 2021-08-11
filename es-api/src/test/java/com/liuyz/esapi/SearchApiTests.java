package com.liuyz.esapi;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/14
 * @description 搜索api测试
 */

@SpringBootTest
public class SearchApiTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    //搜索文档
    @Test
    void testSearchApi() throws IOException {
        //搜索请求，指定索引
        SearchRequest request = new SearchRequest("index_document");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //Set the from option that determines the result index to start searching from. Defaults to 0.
        searchSourceBuilder.from(3);

        //Set the size option that determines the number of search hits to return. Defaults to 10.
        searchSourceBuilder.size(15);

        //Set an optional timeout that controls how long the search is allowed to take.
        searchSourceBuilder.timeout(new TimeValue(1, TimeUnit.SECONDS));

        //排序
//        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC)); //按照score降序排序
        searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.DESC)).sort("birthday", SortOrder.ASC); //按照age降序、birthday升序排序

        //过滤返回值 searchSourceBuilder.fetchSource(includeFields, excludeFields)
        String[] includeFields = new String[]{"name", "age"};
        searchSourceBuilder.fetchSource(includeFields, null); //只返回name、age字段

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置高亮字段名（高亮的字段需要和搜索的字段一致！！！）
        highlightBuilder.field("name");
        //设置高亮字段前缀
        highlightBuilder.preTags("<span style='color: red'>");
        //设置高亮字段后缀
        highlightBuilder.postTags("</span>");
        //添加高亮构造器
        searchSourceBuilder.highlighter(highlightBuilder);

        //匹配所有 QueryBuilders.matchAllQuery()
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);
        //模糊匹配 QueryBuilders.matchQuery
        //2 4 6 表示：多条件查询，只要满足其中的一条就会返回
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "2 4 6");
//        searchSourceBuilder.query(matchQueryBuilder);
        //精确匹配 new TermQueryBuilder
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "3");
//        searchSourceBuilder.query(termQueryBuilder);

        //设置请求源
        request.source(searchSourceBuilder);
        //同步请求，获取响应
        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        System.out.println("查询状态：" + searchResponse.status());
        System.out.println("是否超时：" + searchResponse.isTimedOut());
        SearchHits hits = searchResponse.getHits();
        // do something with the SearchHit
        for (SearchHit searchHit : hits) {
            System.out.println("-------------hit begin---------------");
            System.out.println("index:" + searchHit.getIndex());
            System.out.println("id:" + searchHit.getId());
            System.out.println("score:" + searchHit.getScore());
            System.out.println("内容：" + searchHit.getSourceAsMap().toString());
            //高亮后的结果
//            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
//            HighlightField highlight = highlightFields.get("name");
//            System.out.println("highlight:" + highlight.fragments()[0].toString());
//            System.out.println("-------------hit over---------------");
        }
    }

}
