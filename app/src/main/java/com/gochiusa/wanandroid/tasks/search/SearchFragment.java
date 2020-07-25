package com.gochiusa.wanandroid.tasks.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.adapter.FlowLayoutAdapter;
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View {

    private Button mClearButton;
    private FlowLayout mHotWordLayout;
    private FlowLayout mHistoryLayout;
    /**
     *  显示"历史搜索"这个固定文字的TextView
     */
    private TextView mHistoryTextView;


    private FlowLayoutAdapter mHotWordAdapter;
    private FlowLayoutAdapter mHistoryAdapter;
    /**
     *  顶部的搜索框
     */
    private SearchView mSearchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_search_flow, container, false);
        initChildView(view);
        // 向Presenter请求数据
        getPresenter().requestData();
        return view;
    }

    /**
     *  初始化子项控件
     */
    private void initChildView(View parent) {
        mClearButton = parent.findViewById(R.id.btn_search_clear);
        mHistoryLayout = parent.findViewById(R.id.flow_layout_search_history);
        mHotWordLayout = parent.findViewById(R.id.flow_layout_search_hot_word);
        mHistoryTextView = parent.findViewById(R.id.tv_search_history);

        initAdapter();
        
        // 设置清空按钮的点击事件
        mClearButton.setOnClickListener((view) -> removeAllHistory());
        // 暂时隐藏历史记录控件
        hideHistoryView();
    }

    /**
     *  初始化流式布局的适配器
     */
    private void initAdapter() {
        // 初始化适配器
        mHistoryAdapter = new FlowLayoutAdapter(new ArrayList<>());
        mHotWordAdapter = new FlowLayoutAdapter(new ArrayList<>());
        // 设置适配器
        mHotWordLayout.setAdapter(mHotWordAdapter);
        mHistoryLayout.setAdapter(mHistoryAdapter);

        // 指定点击TextView的点击事件
        FlowLayoutAdapter.ItemClickListener listener = (text) ->
            mSearchView.setQuery(text, true);
        mHistoryAdapter.setItemClickListener(listener);
        mHotWordAdapter.setItemClickListener(listener);
    }

    @Override
    protected SearchContract.Presenter onBindPresenter() {
        return new SearchPresenter(this);
    }

    /**
     *  清除所有历史记录
     */
    private void removeAllHistory() {
        // 移除所有数据
        mHistoryAdapter.removeAll();
        mHistoryLayout.notifyDataSetChange();
        hideHistoryView();
        // 请求Presenter清除数据
        getPresenter().clearHistory();
    }

    /**
     *  隐藏历史记录的相关控件
     */
    private void hideHistoryView() {
        // 隐藏按钮和历史搜索的TextView
        mClearButton.setVisibility(View.GONE);
        mHistoryTextView.setVisibility(View.GONE);
    }

    /**
     * 显示历史记录的相关控件
     */
    private void showHistoryView() {
        // 必须先重新布局按钮后重新布局流式
        mClearButton.setVisibility(View.VISIBLE);
        mHistoryTextView.setVisibility(View.VISIBLE);
    }

    /**
     *  将历史搜索记录添加到流式布局中
     * @param historyList 含有历史搜索记录文本的列表
     */
    @Override
    public void showHistory(List<String> historyList) {
        if (historyList == null || historyList.size() == 0) {
            return;
        }
        showHistoryView();
        mHistoryAdapter.addAll(historyList);
        mHistoryLayout.notifyDataSetChange();
    }

    @Override
    public void showHotWord(List<String> hotWordList) {
        if (hotWordList == null) {
            return;
        }
        mHotWordAdapter.addAll(hotWordList);
        mHotWordLayout.notifyDataSetChange();
    }

    /**
     *  将一条历史搜索添加到流式布局中
     * @param history 历史搜索文本
     */
    public void addHistory(String history) {
        showHistoryView();
        // 将数据添加至首位，并判断这是不是旧的数据
        boolean oldData = mHistoryAdapter.addOrMoveToTop(history);
        // 通知Layout数据被更新
        mHistoryLayout.notifyDataSetChange();
        // 添加到磁盘缓存
        getPresenter().addHistory(history, oldData);
    }



    protected void setSearchView(SearchView searchView) {
        mSearchView = searchView;
    }
}
