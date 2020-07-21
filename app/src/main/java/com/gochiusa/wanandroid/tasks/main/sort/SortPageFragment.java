package com.gochiusa.wanandroid.tasks.main.sort;

import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.adapter.TreeAdapter;
import com.gochiusa.wanandroid.base.view.BaseRecyclerViewFragment;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.ArrayList;
import java.util.List;


public class SortPageFragment extends BaseRecyclerViewFragment<SortPageContract.SortPresenter>
        implements SortPageContract.SortView {

    private TreeAdapter mTreeAdapter;

    /**
     *  重写初始化RecyclerView的方法，添加初始化适配器的部分
     */
    protected void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        // 初始化适配器
        mTreeAdapter = new TreeAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(mTreeAdapter);
    }

    @Override
    public void replaceAll(List<Tree> trees) {
        mTreeAdapter.clear();
        mTreeAdapter.addAll(trees);
    }

    @Override
    protected SortPageContract.SortPresenter onBindPresenter() {
        return new SortPagePresenter(this);
    }
}
