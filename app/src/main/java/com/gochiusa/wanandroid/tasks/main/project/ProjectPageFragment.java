package com.gochiusa.wanandroid.tasks.main.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.base.view.BaseFragment;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.tasks.main.project.child.ChildPageFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectPageFragment extends BaseFragment<ProjectContract.ProjectPresenter>
        implements ProjectContract.ProjectView {

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    /**
     *  每一个Tab的名称（标题）
     */
    private List<String> mChapterNameList;

    /**
     *  每一个Tab的类型对应的id
     */
    private List<Integer> mChapterIdList;

    /**
     *  碎片管理器
     */
    private FragmentManager mFragmentManager;

    /**
     *  状态变量，是否将项目分类数据加载完毕
     */
    private boolean mIsTreeLoadSuccess = false;

    public ProjectPageFragment() {}

    public ProjectPageFragment(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_view_pager, container, false);
        initChildView(view);
        // 向Presenter请求分类数据
        getPresenter().requestProjectTree();
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 当重新显示碎片且数据未加载，尝试再次加载项目分类数据
        if (! (hidden || mIsTreeLoadSuccess)) {
            getPresenter().requestProjectTree();
        }
    }

    /**
     *  辅助方法，初始化子控件
     * @param parent 带有子控件的容器
     */
    private void initChildView(View parent) {
        mTabLayout = parent.findViewById(R.id.tab_layout);
        mViewPager = parent.findViewById(R.id.vp_fragment);
    }


    @Override
    protected ProjectContract.ProjectPresenter onBindPresenter() {
        return new ProjectPagePresenter(this);
    }


    @Override
    public void treeLoadSuccess(Tree projectTree) {
        // 重置状态变量
        mIsTreeLoadSuccess = true;
        // 初始化存放分类的id和名称的集合
        mChapterIdList = new ArrayList<>();
        mChapterNameList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : projectTree.getAllChildren()) {
            // 将id和名称一一对应加入到列表中
            mChapterIdList.add(entry.getValue());
            mChapterNameList.add(entry.getKey());
        }
        mTabLayout.setupWithViewPager(mViewPager, false);
        // 初始化ViewPager的适配器
        initViewPagerAdapter();
    }

    private void initViewPagerAdapter() {
        // 初始化ViewPager的适配器
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager,
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new ChildPageFragment(mChapterIdList.get(position));
            }

            @Override
            public int getCount() {
                return mChapterIdList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mChapterNameList.get(position);
            }
        });
    }
}
