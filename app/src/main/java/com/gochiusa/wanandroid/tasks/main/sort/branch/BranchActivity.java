package com.gochiusa.wanandroid.tasks.main.sort.branch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Tree;
import com.gochiusa.wanandroid.util.ActivityUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BranchActivity extends AppCompatActivity {

    private static Tree mTree;

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

    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        // 初始化碎片管理器
        mFragmentManager = getSupportFragmentManager();
        initChildView();
    }

    /**
     * 辅助方法，初始化子项view
     */
    private void initChildView() {
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.vp_branch);
        mTabLayout = findViewById(R.id.tab_branch);
        // 对Toolbar进行一些设置
        initToolbar(mToolbar);
        // 使用Tree的数据初始化控件
        initTabLayout();
        initViewPagerAdapter();
    }

    /**
     *  设置Toolbar
     */
    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // 设置图标
            toolbar.setNavigationIcon(R.drawable.ic_web_back);
            // 打开返回的菜单按钮
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // 设置状态栏的颜色
        ActivityUtil.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /**
     *  设置TabLayout
     */
    private void initTabLayout() {
        // 初始化存放分类的id和名称的集合
        mChapterIdList = new ArrayList<>();
        mChapterNameList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mTree.getAllChildren()) {
            // 将id和名称一一对应加入到列表中
            mChapterIdList.add(entry.getValue());
            mChapterNameList.add(entry.getKey());
        }
        // 将TabLayout与ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    /**
     *  设置ViewPager的适配器
     */
    private void initViewPagerAdapter() {
        // 初始化ViewPager的适配器
        mViewPager.setAdapter(new FragmentStatePagerAdapter(mFragmentManager,
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new BranchFragment(mChapterIdList.get(position));
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 如果点击返回按钮，结束这个Activity
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }




    public static void startThisActivity(Context context, Tree tree) {
        Intent intent = new Intent(context, BranchActivity.class);
        mTree = tree;
        context.startActivity(intent);
    }


}
