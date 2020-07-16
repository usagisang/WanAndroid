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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.util.ActivityUtil;

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
        // 打开返回的按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // 设置状态栏的颜色
        ActivityUtil.setWindowStatusBarColor(this, R.color.colorPrimary);
    }

    /**
     *  设置WebView
     */
    private void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        // 允许JavaScript直接打开窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 允许JavaScript内容
        webSettings.setJavaScriptEnabled(true);
        // WebView的缓存模式为优先使用本地缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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

    /**
     *  辅助方法，跳转至系统浏览器打开网页
     */
    private void openInBrowser() {
        // 使用隐式Intent打开其他的浏览器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = mWebView.getUrl();
        if (url.startsWith("https://")) {
            intent.setData(Uri.parse(mWebView.getUrl()));
        } else {
            // 如果URL被重定向为打开具体应用的URL，则使用原始的URL尝试打开页面
            intent.setData(Uri.parse(sFirstURL));
        }
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 重写Activity按下返回键的处理，使得WebView的网页可以后退
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            // 清除WebView留下的缓存，这会清除应用所使用的所有WebView的缓存
            mWebView.clearCache(true);
            // 清除当前WebView访问的历史记录
            mWebView.clearHistory();
            // 清除WebView自动完成填充的表单数据
            mWebView.clearFormData();
            return super.onKeyDown(keyCode, event);
        }
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
                break;
            }
            // 如果点击在浏览器内打开的按钮
            case R.id.menu_web_toolbar_action_open : {
                openInBrowser();
                break;
            }
        }
        return true;
    }
}
