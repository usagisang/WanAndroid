package com.gochiusa.wanandroid.util;

import static com.gochiusa.wanandroid.util.DomainAPIConstant.*;
public final class CreateURLToRequest {

    /**
     *    生成请求首页文章数据的URL
     * @param page 请求第{@code page}页的信息，page计数从0开始
     * @return 可以请求到首页文章的相关信息的URL，以json格式返回
     */
    public static String createHomeArticleURL(int page) {
        return DOMAIN_URL + HOME_ARTICLE_API + LIST_API + page + DATA_FORMAT;
    }

    /**
     *  生成获取体系的数据的URL
     */
    public static String createTypeTreeURL() {
        return DOMAIN_URL + TREE_API + DATA_FORMAT;
    }

    /**
     * 生成获取项目分类数据的URL
     */
    public static String createProjectTreeURL() {
        return DOMAIN_URL + PROJECT_API + TREE_API + DATA_FORMAT;
    }

    /**
     *  生成获取一个分类下项目的数据
     * @param page 页数
     * @param id 具体分类的id
     */
    public static String createProjectDataURL(int page, int id) {
        return DOMAIN_URL + PROJECT_API + LIST_API + page + DATA_FORMAT + CID_PARAM + id;
    }
}
