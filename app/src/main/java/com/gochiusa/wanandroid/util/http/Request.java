package com.gochiusa.wanandroid.util.http;


public class Request {
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    /**
     * 默认使用GET方式请求
     */
    String mRequestMethod = GET_METHOD;

    /**
     *  请求的内容，如果执行GET方法，则为null
     */
    RequestBody mRequestBody;

    /**
     *  想要向其发起请求的网址
     */
    String mHttpUrl;

    /**
     *  辅助方法，设置请求的方法和请求体
     */
    private void methodWithBody(String requestMethod, RequestBody requestBody) {
        mRequestMethod = requestMethod;
        mRequestBody = requestBody;
    }

    public static final class Builder {
        private Request mRequest = new Request();

        public Builder post(RequestBody requestBody) {
            mRequest.methodWithBody(Request.POST_METHOD, requestBody);
            return this;
        }

        public Builder get() {
            mRequest.methodWithBody(Request.GET_METHOD, null);
            return this;
        }

        public Builder setUrl(String url) {
            mRequest.mHttpUrl = url;
            return this;
        }

        public Request build() {
            Request request = mRequest;
            // 置空引用
            mRequest = null;
            return request;
        }
    }

}
