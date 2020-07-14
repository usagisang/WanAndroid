package com.gochiusa.wanandroid.util.http;

import com.gochiusa.wanandroid.util.http.base.BaseCall;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 *  类的设计模仿自OkHttp的OkHttpClient
 */
public final class HttpClient implements BaseCall.Factory {

    /**
     *  最大连接超时
     */
    int mConnectionTimeout = 8000;
    /**
     * 最大读取超时
     */
    int mReadTimeout = 8000;

    /**
     *  默认不使用缓存
     */
    boolean mUseCache = false;
    /**
     *  默认允许输入
     */
    boolean doInput = true;
    /**
     *  默认不允许输出
     */
    boolean doOutPut = false;


    @Override
    public BaseCall newCall(Request request) {
        return new Call(this, request);
    }

    /**
     *  HttpClient的建造类，模仿使用建造者模式
     */
    public static final class Builder {
        private HttpClient mHttpClient;

        public Builder() {
            mHttpClient = new HttpClient();
        }

        public Builder setConnectionTimeout(int connectionTimeout) {
            mHttpClient.mConnectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            mHttpClient.mReadTimeout = readTimeout;
            return this;
        }


        public Builder setCookieManager(CookieStore cookieStore, CookiePolicy cookiePolicy) {
            CookieHandler.setDefault(new CookieManager(cookieStore, cookiePolicy));
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            mHttpClient.mUseCache = useCache;
            return this;
        }

        public Builder setDoInput(boolean doInput) {
            mHttpClient.doInput = doInput;
            return this;
        }

        public Builder setDoOutPut(boolean doOutPut) {
            mHttpClient.doOutPut = doOutPut;
            return this;
        }

        public HttpClient build() {
            return mHttpClient;
        }
    }
}
