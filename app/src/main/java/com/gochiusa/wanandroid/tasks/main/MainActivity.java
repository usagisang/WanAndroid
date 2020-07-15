package com.gochiusa.wanandroid.tasks.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gochiusa.wanandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    /**
     *  碎片管理器
     */
    private FragmentManager mFragmentManager;
    /**
     *  当前在最上层显示的碎片
     */
    private Fragment mNowShowFragment;

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
        initHomePageFragment();
    }


    /**
     * 初始化子控件
     */
    private void initChildView() {
        // 初始化Toolbar并设置
        mToolbar =  findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);
        // 初始化底部导航栏
        mBottomNavigationView = findViewById(R.id.bnv_main);
    }

    /**
     * 辅助方法，第一次开启APP时，初始化显示的碎片Fragment
     */
    private void initHomePageFragment() {
        // 初始化碎片管理器
        mFragmentManager = getSupportFragmentManager();
        // 第一次打开显示的碎片一般是首页
        mNowShowFragment = HomePageFragment.newInstance();
        // 使用碎片管理器显示碎片
        mFragmentManager.beginTransaction().
                add(R.id.fl_main_content, mNowShowFragment).commit();
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

            }
        }
        return true;
    }
}