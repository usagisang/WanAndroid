package com.gochiusa.wanandroid.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;


/**
 *   封装了对List数据源的基本操作的一个适配器类
 * @param <T> 数据类型
 * @param <V> ViewHolder的类型
 */
public abstract class ListAdapter<T, V extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<V> {

    /**
     *  数据的集合
     */
    private List<T> mList;

    /**
     * 线程锁
     */
    private final Object mLock = new Object();


    public void clear() {
        synchronized (mLock) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            mList.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void add(T object) {
        synchronized (mLock) {
            mList.add(object);
            notifyDataSetChanged();
        }
    }

    public void remove(T object) {
        synchronized (mLock) {
            mList.remove(object);
            notifyDataSetChanged();
        }
    }

    public ListAdapter(List<T> list) {
        mList = list;
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public int getPosition(@NonNull T item) {
        return mList.indexOf(item);
    }

    /**
     * 获取列表里所有元素的个数
     */
    public int getListItemCount() {
        return mList.size();
    }

    @Override
    public int getItemCount() {
        return getListItemCount();
    }
}
