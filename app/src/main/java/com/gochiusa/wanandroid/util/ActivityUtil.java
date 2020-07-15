package com.gochiusa.wanandroid.util;

import android.app.ActivityManager;
import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public final class ActivityUtil {
    public static void addFragmentToActivity(FragmentManager fragmentManager,
                                             Fragment newFragment, Fragment oldFragment, int frameId) {
        addFragmentToActivity(fragmentManager, newFragment, oldFragment, frameId, null);
    }

    /**
     *  使用另外的碎片，替换正在显示的碎片。使用这个方法添加的碎片不会被加入返回栈
     * @param fragmentManager 碎片管理器
     * @param newFragment 需要显示在最上层的碎片
     * @param oldFragment 需要隐藏的正在显示的碎片
     * @param frameId 显示碎片的控件id
     * @param tag 为新添加的碎片指定的Tag标识
     */
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment newFragment,
                                             Fragment oldFragment, int frameId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (tag != null) {
            // 添加碎片，并为这个碎片指定一个tag
            transaction.add(frameId, newFragment, tag);
        } else {
            // 添加碎片，但不指定tag
            transaction.add(frameId, newFragment);
        }
        // 隐藏当前顶部显示的碎片
        transaction.hide(oldFragment);
        transaction.commit();

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