package com.gochiusa.wanandroid.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.gochiusa.wanandroid.base.RequestCallback;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.model.HomePageModel;
import com.gochiusa.wanandroid.tasks.web.WebViewActivity;
import com.gochiusa.wanandroid.util.ActivityUtil;
import com.gochiusa.wanandroid.util.NotificationUtil;

import java.util.List;

public class NotificationService extends Service {
    public NotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建请求数据的Model
        HomePageModel homePageModel = new HomePageModel();
        homePageModel.loadNewArticle(createCallback());
        return super.onStartCommand(intent, flags, startId);
    }


    private RequestCallback<List<Article>, String> createCallback() {
        return new RequestCallback<List<Article>, String>() {
            @Override
            public void onResponse(List<Article> response) {
                NotificationManager manager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // 仅当能够获取到通知管理器才继续获取其他参数并准备发送通知
                if (manager != null) {
                    Article article = response.get(0);
                    String title = "每日推送";
                    String content = article.getTitle();
                    Intent intent = new Intent(
                            NotificationService.this, WebViewActivity.class);
                    // 传递网址数据
                    intent.putExtra(WebViewActivity.OPEN_URL, article.getLink());
                    // 生成PendingIntent，指示点击通知的跳转
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            NotificationService.this, 1, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    // 发送通知
                    NotificationUtil.sendNotification(title, content,
                            manager, NotificationService.this, pendingIntent);
                }
                // 再次创建定时任务
                ActivityUtil.startServiceAfter(
                        (AlarmManager) getSystemService(Context.ALARM_SERVICE),
                        NotificationService.this);
                // 结束服务
                stopSelf();
            }
            @Override
            public void onFailure(String failure) {
                // 再次创建定时任务
                ActivityUtil.startServiceAfter(
                        (AlarmManager) getSystemService(Context.ALARM_SERVICE),
                        NotificationService.this);
                // 结束服务
                stopSelf();
            }
        };
    }
}
