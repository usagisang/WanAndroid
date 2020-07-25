package com.gochiusa.wanandroid.util;

import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.entity.Project;
import com.gochiusa.wanandroid.entity.Tree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JSONPause {

    /**
     * 一些在JSON数据的比较常用的键值
     */
    private static final String NAME = "name";
    private static final String DATA = "data";
    private static final String ID = "id";
    private static final String OFFSET = "offset";
    private static final String SIZE = "size";
    private static final String TOTAL = "total";
    private static final String NICE_DATE = "niceDate";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";

    /**
     *  解析首页的文章的JSON数据
     * @param articleResponse 关于首页文章信息的JSON
     * @param offsetCalculator 偏移量计算器，如果为null则不更新偏移量的数据
     * @return 实体类集合，实体类封装文章的信息
     */
    public static List<Article> getArticles(
            String articleResponse, @Nullable OffsetCalculator offsetCalculator)
            throws JSONException {
        JSONObject outsideObject = new JSONObject(articleResponse);
        JSONObject dataObject = outsideObject.getJSONObject(DATA);
        // 获取包含文章信息的数组
        JSONArray dataArray = dataObject.getJSONArray("datas");
        int dataLength = dataArray.length();
        ArrayList<Article> articles = new ArrayList<>(dataLength);
        // 遍历JSON数组
        for (int i = 0;i < dataLength;i ++) {
            JSONObject eachObject = dataArray.getJSONObject(i);
            // 实例化实体类
            Article article = new Article();
            article.setTitle(eachObject.getString(TITLE));
            article.setNiceDate(eachObject.getString(NICE_DATE));
            article.setSuperChapterName(eachObject.getString("superChapterName"));
            article.setChapterName(eachObject.getString("chapterName"));
            article.setLink(eachObject.getString(LINK));
            article.setAuthor(eachObject.getString(AUTHOR));
            articles.add(article);
        }
        // 如果偏移量计算类不为null，尝试获取偏移量信息
        if (offsetCalculator != null) {
            offsetCalculator.setOffset(dataObject.getInt(OFFSET));
            offsetCalculator.setPageLimit(dataObject.getInt(SIZE));
            offsetCalculator.setTotalCount(dataObject.getInt(TOTAL));
        }
        return articles;
    }

    /**
     *  解析体系的JSON数据
     * @param jsonData 包含体系数据的JSON
     * @return 实体类的集合，实体类内封装了体系数据
     */
    public static List<Tree> getTypeTrees(String jsonData) throws JSONException {
        JSONObject outsideObject = new JSONObject(jsonData);
        // 获取包含每一个体系信息的数组
        JSONArray dataArray = outsideObject.getJSONArray(DATA);
        int dataLength = dataArray.length();
        ArrayList<Tree> treeArrayList = new ArrayList<>(dataLength);

        // 循环遍历
        for (int i = 0;i < dataLength;i ++) {
            JSONObject eachObject = dataArray.getJSONObject(i);
            // 实例化实体类
            Tree tree = new Tree();
            // 获取一级体系的名字
            tree.setName(eachObject.getString(NAME));
            // 获取该一级体系包含的所有二级体系
            JSONArray secondArray = eachObject.getJSONArray("children");

            // 循环遍历
            for (int j = 0;j < secondArray.length();j ++) {
                JSONObject childrenObject = secondArray.getJSONObject(j);
                // 添加二级体系的名字和对应的id
                tree.addChildren(childrenObject.getString(NAME),
                        childrenObject.getInt(ID));
            }
            treeArrayList.add(tree);
        }
        return treeArrayList;
    }

    /**
     *  解析项目分类的JSON数据
     * @param jsonData 包含项目分类的JSON数据
     * @return 由于项目只存在一级分类，因此返回的集合实际上只有一个元素
     */
    public static List<Tree> getProjectTree(String jsonData) throws JSONException {
        // 初始化集合与实体类
        Tree tree = new Tree();
        List<Tree> result = new ArrayList<>(1);
        result.add(tree);

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray data = jsonObject.getJSONArray(DATA);
        for (int i = 0;i < data.length();i ++) {
            JSONObject eachObject = data.getJSONObject(i);
            // 添加子分类
            tree.addChildren(eachObject.getString(NAME), eachObject.getInt(ID));
        }
        return result;
    }

    /**
     *  解析具体的一个分类下所有项目的JSON数据
     * @param jsonData 关于项目的JSON数据
     * @param offsetCalculator 偏移量计算器，如果为null则不更新偏移量的数据
     * @return 实体类集合，实体类封装文章的信息
     */
    public static List<Project> getProjects(
            String jsonData, @Nullable OffsetCalculator offsetCalculator ) throws JSONException {
        JSONObject outsideObject = new JSONObject(jsonData);

        JSONObject dataObject = outsideObject.getJSONObject(DATA);
        // 获取包含项目信息的数组
        JSONArray dataArray = dataObject.getJSONArray("datas");
        int dataLength = dataArray.length();
        // 初始化数组
        List<Project> projectList = new ArrayList<>(dataLength);
        // 遍历JSON数组
        for (int i = 0;i < dataLength;i ++) {
            JSONObject eachObject = dataArray.getJSONObject(i);
            // 实例化实体类
            Project project = new Project();
            project.setAuthor(eachObject.getString(AUTHOR));
            project.setDescription(eachObject.getString("desc"));
            project.setEnvelopePictureLink(eachObject.getString("envelopePic"));
            project.setLink(eachObject.getString(LINK));
            project.setNiceDate(eachObject.getString(NICE_DATE));
            project.setTitle(eachObject.getString(TITLE));
            // 加入到集合中
            projectList.add(project);
        }

        // 如果偏移量计算类不为null，尝试获取偏移量信息
        if (offsetCalculator != null) {
            offsetCalculator.setOffset(dataObject.getInt(OFFSET));
            offsetCalculator.setPageLimit(dataObject.getInt(SIZE));
            offsetCalculator.setTotalCount(dataObject.getInt(TOTAL));
        }
        return projectList;
    }

    /**
     *  解析搜索热词的JSON数据
     */
    public static List<String> getHotKey(String jsonData) throws JSONException {
        JSONObject outsideObject = new JSONObject(jsonData);
        // 获取包含搜索热词的数组
        JSONArray dataArray = outsideObject.getJSONArray(DATA);
        int dataLength = dataArray.length();
        ArrayList<String> hotKeyList = new ArrayList<>(dataLength);
        for (int i = 0;i < dataLength;i ++) {
            JSONObject eachObject = dataArray.getJSONObject(i);
            hotKeyList.add(eachObject.getString(NAME));
        }
        return hotKeyList;
    }

}
