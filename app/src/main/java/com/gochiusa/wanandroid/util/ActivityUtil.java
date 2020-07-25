package com.gochiusa.wanandroid.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gochiusa.wanandroid.service.NotificationService;

import java.util.List;

public final class ActivityUtil {

    /**
     *  默认启动延时，为24小时
     */
    private static final long RESTART_DELAY = 24 * 60 * 60 * 1000;

    /**
     *  隐藏正在显示的碎片，然后显示另一个碎片。
     *  需要显示的碎片必须已经添加到{@code FragmentManager}中
     * @param fragmentManager 碎片管理器
     * @param showFragment 需要显示在最上层的碎片，必须已经被添加
     * @param hideFragment 需要隐藏的正在显示的碎片
     */
    public static void hideFragmentWithShow(FragmentManager fragmentManager, Fragment showFragment,
                                            Fragment hideFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(hideFragment).show(showFragment).commit();
    }

    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 一键关闭这个程序，只能在单任务栈的情况下使用
     */
    public static void closeApplication(Context context) {
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // 获取任务栈
            List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();

            // 逐个关闭Activity
            for (ActivityManager.AppTask appTask : appTaskList) {
                appTask.finishAndRemoveTask();
            }
            // 结束进程
            System.exit(0);
        }
    }

    /**
     *  释放碎片管理器中所有的Fragment
     */
    public static void detachAllFragment(FragmentManager fragmentManager) {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentList) {
            transaction.detach(fragment);
        }
        transaction.commit();
    }

    /**
     *  设置状态栏显示的颜色
     * @param activity 正在任务栈顶层显示的Activity
     * @param colorId 来自资源文件（R文件）的颜色id
     */
    public static void setWindowStatusBarColor(Activity activity, int colorId) {
        Window window = activity.getWindow();
        // 添加Flag把状态栏设置为可绘制状态
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 使用ColorId获取Color，并设置到状态栏上
        window.setStatusBarColor(activity.getResources().getColor(colorId));
    }

    /**
     *  使用AlarmManager创建Alarm，在默认的一段时间之后启动服务
     */
    public static void startServiceAfter(@Nullable AlarmManager alarmManager, Context context) {
        startServiceAfter(alarmManager, context, RESTART_DELAY);
    }

    /**
     *  使用AlarmManager创建Alarm，在指定的一段时间之后启动服务以推送广播
     * @param time 指定的活动启动延时，为毫秒数
     */
    public static void startServiceAfter(
            @Nullable AlarmManager alarmManager, Context context, long time) {
        if (alarmManager == null) {
            return;
        }
        // 获取从开机到现在的毫秒数，然后多久之后开始推送
        long startTime = SystemClock.elapsedRealtime() + time;
        // 获取PendingIntent以启动服务
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        // 先取消掉上一次填充的Alarm
        alarmManager.cancel(pendingIntent);
        // 重新创建Alarm
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, startTime ,pendingIntent);
    }

    public static boolean checkNetWork(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        // 分版本调用API
        if (Build.VERSION.SDK_INT < 23) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        } else {
            Network network = manager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        }
    }
}
