package com.gochiusa.wanandroid.tasks.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.widget.FlowLayout;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View {

    private Button mClearButton;
    private FlowLayout mHotWordLayout;
    private FlowLayout mHistoryLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_search_flow, container, false);
        initChildView(view);

        return view;
    }

    private void initChildView(View parent) {
        mClearButton = parent.findViewById(R.id.btn_search_clear);
        mHistoryLayout = parent.findViewById(R.id.flow_layout_search_history);
        mHotWordLayout = parent.findViewById(R.id.flow_layout_search_hot_word);
    }

    @Override
    protected SearchContract.Presenter onBindPresenter() {
        return new SearchPresenter(this);
    }
}
