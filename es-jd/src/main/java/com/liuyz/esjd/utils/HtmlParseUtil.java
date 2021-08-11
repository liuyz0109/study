package com.liuyz.esjd.utils;

import com.liuyz.esjd.pojo.JdGoods;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/16
 * @description 解析网页工具类
 */

public class HtmlParseUtil {

    //测试解析工具
    public static void main(String[] args) throws IOException {
        HtmlParseUtil.parseJd("语文").forEach(System.out::println);
    }

    /**
     * @param keyword 搜索关键字
     * @return 解析对象集合
     * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
     * @description 解析京东网页，返回商品对象集合
     */
    public static List<JdGoods> parseJd(String keyword) throws IOException {
        //获取请求 https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";

        //解析网页（返回的Document对象就是浏览器的Document对象）
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36")
                .timeout(30000).get();

        //获取需要数据的div
        Element element = document.getElementById("J_goodsList");

        //获取该标签下的li元素
        Elements elements = element.getElementsByTag("li");
        List<JdGoods> jdGoodsList = new ArrayList<>();
        for (Element li : elements) {
            String src = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = li.getElementsByClass("p-price").eq(0).text();
            String name = li.getElementsByClass("p-name").eq(0).text();
            JdGoods jdGoods = new JdGoods(name, price, src);
            jdGoodsList.add(jdGoods);
        }
        return jdGoodsList;
    }

}
