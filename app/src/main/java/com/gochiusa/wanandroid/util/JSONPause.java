package com.gochiusa.wanandroid.util;

import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.entity.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JSONPause {

    /**
     *  解析首页的文章的JSON数据
     * @param articleResponse 关于首页文章信息的JSON
     * @param offsetCalculator 偏移量计算器，如果为null则不读取偏移量的数据
     * @return 实体类集合，实体类封装文章的信息
     */
    public static List<Article> getArticles(
            String articleResponse, @Nullable OffsetCalculator offsetCalculator)
            throws JSONException {
        JSONObject outsideObject = new JSONObject(articleResponse);
        JSONObject dataObject = outsideObject.getJSONObject("data");
        // 获取包含文章信息的数组
        JSONArray dataArray = dataObject.getJSONArray("datas");
        int dataLength = dataArray.length();
        ArrayList<Article> articles = new ArrayList<>(dataLength);
        // 遍历JSON数组
        for (int i = 0;i < dataLength;i ++) {
            JSONObject eachObject = dataArray.getJSONObject(i);
            // 实例化实体类
            Article article = new Article();
            article.setTitle(eachObject.getString("title"));
            article.setNiceDate(eachObject.getString("niceDate"));
            article.setSuperChapterName(eachObject.getString("superChapterName"));
            article.setChapterName(eachObject.getString("chapterName"));
            article.setLink(eachObject.getString("link"));
            article.setAuthor(eachObject.getString("author"));
            articles.add(article);
        }
        // 如果偏移量计算类不为null，尝试获取偏移量信息
        if (offsetCalculator != null) {
            offsetCalculator.setOffset(dataObject.getInt("offset"));
            offsetCalculator.setPageLimit(dataObject.getInt("size"));
            offsetCalculator.setTotalCount(dataObject.getInt("total"));
        }
        return articles;
    }
}
