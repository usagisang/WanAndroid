package com.gochiusa.wanandroid.util.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  基于DiskLruCache封装的磁盘缓存类
 */
public class DiskCache implements Cache {

    private static final int MAX_SIZE = 1024 * 1024 * 3;

    private DiskLruCache mDiskLruCache;

    private Context context;
    public DiskCache(Context context) throws IOException {
        this.context = context;
        mDiskLruCache = open();
    }
    @Override
    public Bitmap get(String key) {
        try {
            // 获取输入流
            InputStream inputStream = mDiskLruCache.get(
                    Utils.createFileKey(key)).getInputStream(0);
            // 将输入流解析成位图
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void set(String key, Bitmap bitmap) {
        String fileKey = Utils.createFileKey(key);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(fileKey);
            // 打开输出流，并将位图转换成字节数组，写入
            editor.newOutputStream(0).write(getBitmapBytes(bitmap));
            editor.commit();
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  打开DiskLruCache
     */
    private DiskLruCache open() throws IOException {
        return DiskLruCache.open(context.getCacheDir(), 1, 1, MAX_SIZE);
    }

    /**
     * 获取位图的字节数组
     */
    private byte[] getBitmapBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 使用PNG格式压缩图片（实际没有压缩）并写到输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /**
     *  DiskLruCache自动管理了缓存淘汰
     */
    @Override
    public int size() {
        return 0;
    }

    @Override
    public int maxSize() {
        return MAX_SIZE;
    }

    @Override
    public void clear() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
