package com.gochiusa.wanandroid.base;

public interface RequestCallback<T, V> {
    void onResponse(T response);
    void onFailure(V failure);
}
