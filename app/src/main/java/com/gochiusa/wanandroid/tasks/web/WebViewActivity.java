package com.gochiusa.wanandroid.tasks.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gochiusa.wanandroid.R;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends AppCompatActivity {

    public static void startThisActivity(Context context, String firstURL) {
        Intent intent = new Intent(context, WebViewActivity.class);
        sFirstURL = firstURL;
        context.startActivity(intent);
    }

    /**
     * WebView加载时所启动的第一个页面的URL
     */
    private static String sFirstURL;


    private Toolbar mToolbar;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initChildView();
    }

    /**
     * 辅助方法，初始化子项view
     */
    private void initChildView() {
        // 找到子控件
        mToolbar = findViewById(R.id.tb_web);
        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.progress_bar_web);
        // 对Toolbar进行一些设置
        initToolbar(mToolbar);
        initWebView(mWebView);
    }

    /**
     *  设置Toolbar
     */
    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        // 设置打开返回功能
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    /**
     *  设置WebView
     */
    private void initWebView(WebView webView) {
        // 允许JavaScript内容
        webView.getSettings().setJavaScriptEnabled(true);
        // 新的网页仍在这个WebView打开
        webView.setWebViewClient(new WebViewClient());

        // 重写WebChromeClient的部分方法，以实时显示网站标题与加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    // 如果进度小于100，则刷新进度条的进度
                    mProgressBar.setProgress(newProgress);
                } else {
                    // 否则隐藏进度条
                    mProgressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 将网站标题显示在标题栏处
                mToolbar.setTitle(title);
            }
        });

        // 开始加载网页
        webView.loadUrl(sFirstURL);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 重写Activity按下返回键的处理，使得WebView的网页可以后退
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_toolbar_action, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 如果点击返回按钮，结束这个Activity
            case android.R.id.home : {
                finish();
            }
            // 如果点击在浏览器内打开的按钮
            case R.id.menu_web_toolbar_action_open : {
                // 使用隐式Intent打开其他的浏览器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mWebView.getUrl()));
                startActivity(intent);
            }
        }
        return true;
    }
}
