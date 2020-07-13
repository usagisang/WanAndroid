package com.gochiusa.wanandroid.util.http;

import com.gochiusa.wanandroid.util.http.base.BaseCall;

import java.io.IOException;

/**
 * 类的设计模仿自OkHttp的Call
 */
public final class Call implements BaseCall {

    private HttpClient mClient;
    private Request mRequest;
    protected Call(HttpClient httpClient, Request request) {
        mClient = httpClient;
        mRequest = request;
    }


    @Override
    public Request request() {
        return mRequest;
    }

    @Override
    public Response execute() throws IOException {
        return null;
    }
}
