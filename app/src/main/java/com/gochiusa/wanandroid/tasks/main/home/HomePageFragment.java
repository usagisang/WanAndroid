package com.gochiusa.wanandroid.tasks.main.home;

import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.adapter.ArticleAdapter;
import com.gochiusa.wanandroid.base.view.BaseRecyclerViewFragment;
import com.gochiusa.wanandroid.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends BaseRecyclerViewFragment<HomePageContract.HomePresenter>
        implements HomePageContract.HomeView {

    private ArticleAdapter mArticleAdapter;

    @Override
    protected HomePageContract.HomePresenter onBindPresenter() {
        return new HomePagePresenter(this);
    }

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


    public HomePageFragment() {}

}
