package com.gochiusa.wanandroid.util.loader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import static android.content.Context.ACTIVITY_SERVICE;

final class Utils {
    /**
     *  内存缓存允许的最大容量，20M
     */
    private static final int MAX_MEMORY_CACHE_SIZE = 20 * 1024 * 1024;
    /**
     * 正则表达式
     */
    private static final String PATTERN = "[a-zA-Z0-9_]";

    /**
     *  计算创建的内存缓存的大小，默认为可用内存的1/8,但不超过MAX_MEMORY_CACHE_SIZE
     */
    static int calculateMemoryCacheSize(Context context) {
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null) {
            int size = 1024 * 1024 * activityManager.getMemoryClass() / 8;
            return Math.min(size, MAX_MEMORY_CACHE_SIZE);
        } else {
            // 如果没有获取到ActivityManager则默认按照20M处理
            return MAX_MEMORY_CACHE_SIZE;
        }
    }

    /**
     *  获取Bitmap占用空间的大小
     */
    static int calculateBitmapSize(Bitmap bitmap) {
        return bitmap.getAllocationByteCount();
    }

    /**
     *  DiskLruCache只支持有限的字符当作键值，该方法将所有特殊字符替换成"a"
     * @param originKey 未经规范化的键值
     * @return 转换之后的键值
     */
    static String createFileKey(String originKey) {
        char[] allChar = originKey.toCharArray();
        for (int i = 0;i < allChar.length;i ++) {
            if (! String.valueOf(allChar[i]).matches(PATTERN)) {
                allChar[i] = 'a';
            }
        }
        return String.valueOf(allChar);
    }
}
