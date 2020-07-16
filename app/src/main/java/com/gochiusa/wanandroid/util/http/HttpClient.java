package com.gochiusa.wanandroid.util.http;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 *  类的设计模仿自OkHttp的OkHttpClient
 */
public final class HttpClient implements Call.Factory {

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
    boolean mDoInput = true;
    /**
     *  默认不允许输出。如果在Request设置了请求体，进行Post请求时，默认设置会被覆盖。
     */
    boolean mDoOutPut = false;


    @Override
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public HttpClient() {}

    HttpClient(Builder builder) {

    }

    /**
     *  HttpClient的建造类，模仿使用建造者模式
     */
    public static final class Builder {
        /**
         *  与{@code HttpClient}的变量，包括默认值均一一对应。
         */
        private int connectionTimeout = 8000;
        private int readTimeout = 8000;
        private boolean useCache = false;
        private boolean doInput = true;
        private boolean doOutPut = false;


        public Builder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setCookieManager(CookieStore cookieStore, CookiePolicy cookiePolicy) {
            CookieHandler.setDefault(new CookieManager(cookieStore, cookiePolicy));
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Builder setDoInput(boolean doInput) {
            this.doInput = doInput;
            return this;
        }

        public Builder setDoOutPut(boolean doOutPut) {
            this.doOutPut = doOutPut;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}
