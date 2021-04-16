package com.gl.binder_pool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 连接池
 * <p/>
 * Created by wangchenlong on 16/6/17.
 */
public class BinderPoolService extends Service {

    private static final String TAG = "DEBUG-WCL: " + BinderPoolService.class.getSimpleName();

    private Binder mBinderPool = new BinderPool.BinderPoolImpl(); // 动态选择Binder

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return mBinderPool;
    }
}
