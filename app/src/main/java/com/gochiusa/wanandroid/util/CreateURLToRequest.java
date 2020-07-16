package com.gochiusa.wanandroid.util;

import static com.gochiusa.wanandroid.util.DomainAPIConstant.*;
public final class CreateURLToRequest {

    /**
     *    生成请求首页文章数据的URL
     * @param page 请求第{@code page}页的信息，page计数从0开始
     * @return 可以请求到首页文章的相关信息的URL，以json格式返回
     */
    public static String createHomeArticleURL(int page) {
        return DOMAIN_URL + HOME_ARTICLE_API + page + DATA_FORMAT;
    }
}
