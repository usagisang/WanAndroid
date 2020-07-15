package com.gochiusa.wanandroid.tasks.main.home;


import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.base.model.SingleThreadModel;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.util.OffsetCalculator;

import java.util.List;

public class HomePageModel extends SingleThreadModel implements HomePageContract.HomeModel {

    /**
     *  偏移量计算工具类
     */
    private OffsetCalculator mOffsetCalculator;
    /**
     * 到达尽头的提示
     */
    public static final String NOT_MORE_TIP = "到达了尽头惹";

    public HomePageModel() {
        mOffsetCalculator = new OffsetCalculator(0, 1, 0);
    }


    @Override
    public void loadMoreArticle(RequestCallback<List<Article>, String> callback) {
    }

    @Override
    public void loadNewArticle(RequestCallback<List<Article>, String> callback) {
    }
}
