package com.gochiusa.wanandroid.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.gochiusa.wanandroid.R;

public final class NotificationUtil {

    /**
     *  注册的通知渠道的id
     */
    public static final String NOTIFICATION_CHANNEL_ID = "com.gochiusa.wanandroid.article";

    /**
     *  通知渠道显示的名称
     */
    private static final String NOTIFICATION_CHANNEL_NAME = "普通通知";

    /**
     *  发送的通知的id
     */
    private static final int NOTIFICATION_ID = 10;

    /**
     *  创建推送最新文章的通知渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(
            @Nullable NotificationManager manager) {
        // 如果传入null或者已经创建通知渠道，则直接结束方法
        if (manager == null || manager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);
        // 取消振动效果
        channel.enableVibration(false);
        // 添加通知渠道
        manager.createNotificationChannel(channel);
    }

    /**
     *  使用传入的参数，发送通知
     * @param title 通知的标题
     * @param content 通知的内容
     * @param manager 通知管理类
     * @param pendingIntent 点击通知后的跳转行为
     */
    public static void sendNotification(String title, String content,
                                        @NonNull NotificationManager manager,
                                        Context context, PendingIntent pendingIntent) {
        Notification.Builder builder;
        // 根据版本不同，使用不同的构造方法
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
        }
        // 创建通知
        Notification notification = builder.setContentTitle(title)
                .setContentIntent(pendingIntent).setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true).build();
        // 发送
        manager.notify(NOTIFICATION_ID, notification);
    }
}
