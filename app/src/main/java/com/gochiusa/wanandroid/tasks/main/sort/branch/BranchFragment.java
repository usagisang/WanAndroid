package com.gochiusa.wanandroid.tasks.main.sort.branch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.adapter.ArticleAdapter;
import com.gochiusa.wanandroid.base.view.BaseRecyclerViewFragment;
import com.gochiusa.wanandroid.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class BranchFragment extends BaseRecyclerViewFragment<BranchContract.Presenter>
        implements BranchContract.View {

    private ArticleAdapter mArticleAdapter;

    private int mArticleTypeId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container,false);
        initChildView(view);
        // 向Presenter请求最初的数据
        getPresenter().firstRequest(mArticleTypeId);
        return view;
    }

    @Override
    protected BranchContract.Presenter onBindPresenter() {
        return new BranchPresenter(this);
    }

    /**
     *  重写初始化RecyclerView的方法，添加初始化适配器的部分
     */
    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        mArticleAdapter = new ArticleAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(mArticleAdapter);
    }


    @Override
    public void addArticlesToList(List<Article> articles) {
        mArticleAdapter.addAll(articles);
    }

    @Override
    public void showLoading() {
        mArticleAdapter.showFootView();
    }

    @Override
    public void hideLoading() {
        mArticleAdapter.hideFootView();
    }

    @Override
    public void removeAllArticle() {
        mArticleAdapter.clear();
    }

    public BranchFragment() {}

    public BranchFragment(int articleTypeId) {
        mArticleTypeId = articleTypeId;
    }
}
