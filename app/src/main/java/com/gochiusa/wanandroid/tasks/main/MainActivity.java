package com.gochiusa.wanandroid.tasks.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.tasks.main.home.HomePageFragment;
import com.gochiusa.wanandroid.tasks.main.project.ProjectPageFragment;
import com.gochiusa.wanandroid.tasks.main.sort.SortPageFragment;
import com.gochiusa.wanandroid.tasks.search.SearchActivity;
import com.gochiusa.wanandroid.util.ActivityUtil;
import com.gochiusa.wanandroid.util.NotificationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    /**
     *  碎片管理器
     */
    private FragmentManager mFragmentManager;
    /**
     * 主页的碎片
     */
    private Fragment mHomePageFragment;

    /**
     * 正展示在顶层的碎片
     */
    private Fragment mTopFragment;
    /**
     * 知识体系页面的碎片
     */
    private Fragment mSortPageFragment;

    /**
     *  项目页面的碎片
     */
    private Fragment mProjectPageFragment;

    /**
     *  标题栏
     */
    private Toolbar mToolbar;
    /**
     *  底部导航栏
     */
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initChildView();
        initFragments();
        initNotificationChannel();
        // 创建定时任务
        ActivityUtil.startServiceAfter(
                (AlarmManager) getSystemService(Context.ALARM_SERVICE), this);
    }


    /**
     * 初始化子控件
     */
    private void initChildView() {
        // 初始化Toolbar并设置
        mToolbar = findViewById(R.id.toolbar);
        initToolbar(mToolbar);
        // 初始化底部导航栏
        mBottomNavigationView = findViewById(R.id.bnv_main);
        initBottomNavigationView(mBottomNavigationView);
    }

    /**
     *  设置Toolbar
     */
    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        // 设置状态栏的颜色
        ActivityUtil.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /**
     *  设置底部导航栏的监听事件等属性
     */
    private void initBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.menu_main_navigation_home : {
                    changeFragment(mHomePageFragment);
                    break;
                }
                case R.id.menu_main_navigation_knowledge_type : {
                    changeFragment(mSortPageFragment);
                    break;
                }
                case R.id.menu_main_navigation_project : {
                    changeFragment(mProjectPageFragment);
                    break;
                }
            }
            return true;
        });
    }

    /**
     * 辅助方法，初始化需要显示的碎片Fragment
     */
    private void initFragments() {
        int resourceId = R.id.fl_main_content;
        // 初始化碎片管理器
        mFragmentManager = getSupportFragmentManager();
        // 初始化碎片
        mHomePageFragment = new HomePageFragment();
        mSortPageFragment = new SortPageFragment();
        mProjectPageFragment = new ProjectPageFragment(mFragmentManager);
        // 打开事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 添加所有需要显示的碎片，并隐藏除了首页之外的碎片
        transaction.add(resourceId, mHomePageFragment)
                .add(resourceId, mSortPageFragment).add(resourceId, mProjectPageFragment)
                .hide(mSortPageFragment).hide(mProjectPageFragment);
        transaction.commit();

        mTopFragment = mHomePageFragment;
    }

    /**
     *  尝试创建通知渠道
     */
    private void initNotificationChannel() {
        // 如果版本大于8，创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createNotificationChannel((NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_toolbar_search : {
                SearchActivity.startThisActivity(this);
                break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // 清除所有绑定在碎片管理器上的碎片
        ActivityUtil.detachAllFragment(mFragmentManager);
        super.onBackPressed();
    }

    /**
     * 辅助方法，切换显示的碎片
     */
    public void changeFragment(@NonNull Fragment fragment) {
        // 仅当传入的碎片与正在显示的碎片不一致时，进行切换
        if (! fragment.equals(mTopFragment)) {
            ActivityUtil.hideFragmentWithShow(mFragmentManager, fragment, mTopFragment);
            mTopFragment = fragment;
        }
    }
}