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
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.widget.FlowLayout;

import java.util.List;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View {

    private Button mClearButton;
    private FlowLayout mHotWordLayout;
    private FlowLayout mHistoryLayout;
    private TextView mHistoryTextView;
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

    private void initChildView(View parent) {
        mClearButton = parent.findViewById(R.id.btn_search_clear);
        mHistoryLayout = parent.findViewById(R.id.flow_layout_search_history);
        mHotWordLayout = parent.findViewById(R.id.flow_layout_search_hot_word);
        mHistoryTextView = parent.findViewById(R.id.tv_search_history);
        // 设置清空按钮的点击事件
        mClearButton.setOnClickListener((view) -> hideHistory());
        // 隐藏控件
        hideHistoryView();
    }

    @Override
    protected SearchContract.Presenter onBindPresenter() {
        return new SearchPresenter(this);
    }

    /**
     *  清除所有历史记录
     */
    private void hideHistory() {
        // 移除所有子View
        mHistoryLayout.removeAllViews();
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
     *  将历史搜索记录添加到流式布局中
     * @param historyList 历史搜索记录文本
     */
    @Override
    public void showHistory(List<String> historyList) {
        // 显示按钮和历史搜索的TextView
        mHistoryTextView.setVisibility(View.VISIBLE);
        mClearButton.setVisibility(View.VISIBLE);
        addTextViewToFlowLayout(historyList, mHistoryLayout);
    }

    @Override
    public void showHotWord(List<String> hotWordList) {
        addTextViewToFlowLayout(hotWordList, mHotWordLayout);
    }

    /**
     *  将给定的字符串列表，以小Tab的形式添加到流式布局中
     * @param stringList 显示在子TextView的文本
     */
    private void addTextViewToFlowLayout(List<String> stringList, FlowLayout flowLayout) {
        for (String string : stringList) {
            // 创建TextView
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(
                    R.layout.item_search_flow_layout, flowLayout, false);
            // 设置显示的文本
            textView.setText(string);
            // 设置点击的监听器
            textView.setOnClickListener((view) -> {
                if (mSearchView != null) {
                    // 设置输入框文本并提交
                    mSearchView.setQuery(textView.getText(), true);
                }
            });
            mHotWordLayout.addView(textView);
        }
    }

    protected void setSearchView(SearchView searchView) {
        mSearchView = searchView;
    }
}
