package com.gochiusa.wanandroid.tasks.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.util.ActivityUtil;

public class SearchActivity extends AppCompatActivity {

    /**
     *  标题栏
     */
    private Toolbar mToolbar;

    /**
     *  搜索框
     */
    private SearchView mSearchView;

    /**
     *  碎片管理器
     */
    private FragmentManager mFragmentManager;

    /**
     *  碎片对应显示的控件的id
     */
    private int mFragmentResId = R.id.fl_search;

    private QueryResultFragment mResultFragment;
    private SearchFragment mSearchFragment;
    /**
     *  搜索页面的碎片的标记
     */
    private String mSearchTag = "search";

    public SearchActivity() {
    }

    public static void startThisActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();
        initFirstFragment();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // 设置图标
            mToolbar.setNavigationIcon(R.drawable.ic_web_back);
            // 打开返回的菜单按钮
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // 设置状态栏的颜色
        ActivityUtil.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /**
     *  初始化显示的碎片
     */
    private void initFirstFragment() {
        mSearchFragment = new SearchFragment();
        // 初始化碎片管理器
        mFragmentManager = getSupportFragmentManager();
        // 配置好最初的显示设置
        mFragmentManager.beginTransaction().add(mFragmentResId, mSearchFragment).commit();
    }

    /**
     *  打开或者刷新显示搜索结果的碎片
     */
    private void showSearchResult(String searchKey) {
        // 尝试通过Tag获取显示搜索结果的碎片
        if (mFragmentManager.findFragmentByTag(mSearchTag) == null) {
            // 如果没找到，则创建新的碎片
            mResultFragment = new QueryResultFragment();
            // 设置搜索关键词
            mResultFragment.setSearchKey(searchKey);
            // 将碎片添加到界面上
            mFragmentManager.beginTransaction().add(mFragmentResId, mResultFragment, mSearchTag)
                    .addToBackStack(null).hide(mSearchFragment).commit();
        } else {
            // 碎片已经存在于界面上
            mResultFragment.setSearchKey(searchKey);
            // 刷新数据
            mResultFragment.requestFirstData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        // 通过MenuItem得到SearchView
        mSearchView = (SearchView) menu.findItem(R.id.search_view_top).getActionView();
        // 将SearchView传递给碎片
        mSearchFragment.setSearchView(mSearchView);
        initSearchView(mSearchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 如果点击返回按钮，结束这个Activity
            case android.R.id.home : {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFragmentManager.popBackStack();
    }

    /**
     *  初始化SearchView的设置
     */
    private void initSearchView(SearchView searchView) {
        // 显示搜索图标，允许关闭
        searchView.setIconified(false);
        // 设置显示在框内的提示
        searchView.setQueryHint("多个关键词请用空格分开");
        // 设置提交的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 去除前后空格
                String keyWord = query.trim();
                // 关闭软键盘
                searchView.clearFocus();
                // 添加历史记录
                mSearchFragment.addHistory(keyWord);
                // 提交搜索
                showSearchResult(keyWord);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
