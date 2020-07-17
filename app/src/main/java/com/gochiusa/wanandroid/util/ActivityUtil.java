package com.gochiusa.wanandroid.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public final class ActivityUtil {

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
}
