package com.gochiusa.wanandroid.tasks.search;

import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.adapter.ArticleAdapter;
import com.gochiusa.wanandroid.base.view.BaseRecyclerViewFragment;
import com.gochiusa.wanandroid.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class QueryResultFragment extends BaseRecyclerViewFragment<ResultContract.ResultPresenter>
        implements ResultContract.ResultView {

    /**
     *  搜索关键词，目前默认的空字符串会全匹配搜索结果
     */
    private String mSearchKey = "";

    private ArticleAdapter mArticleAdapter;

    /**
     *  重写初始化RecyclerView的方法，添加初始化适配器的部分
     */
    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        // 初始化适配器
        mArticleAdapter = new ArticleAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(mArticleAdapter);
    }

    @Override
    protected void requestFirstData() {
        getPresenter().firstRequest(mSearchKey);
    }


    /**
     *  设置搜索关键词
     */
    public void setSearchKey(String searchKey) {
        mSearchKey = searchKey;
    }

    @Override
    protected ResultContract.ResultPresenter onBindPresenter() {
        return new QueryResultPresenter(this);
    }

    @Override
    public void addArticlesToList(List<Article> articles) {
        mArticleAdapter.addAll(articles);
    }

    /**
     *  显示尾布局
     */
    @Override
    public void showLoading() {
        mArticleAdapter.showFootView();
    }

    /**
     * 隐藏尾布局
     */
    @Override
    public void hideLoading() {
        mArticleAdapter.hideFootView();
    }

    @Override
    public void removeAllArticle() {
        mArticleAdapter.clear();
    }
}
