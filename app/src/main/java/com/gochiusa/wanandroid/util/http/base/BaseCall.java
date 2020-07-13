package com.gochiusa.wanandroid.util.http.base;

import com.gochiusa.wanandroid.util.http.Request;
import com.gochiusa.wanandroid.util.http.Response;

import java.io.IOException;

public interface BaseCall {

    /**
     * 返回原始的Request
     */
    Request request();

    /**
     *  发送同步请求
     */
    Response execute() throws IOException;

    interface Factory {
        BaseCall newCall(Request request);
    }
}
