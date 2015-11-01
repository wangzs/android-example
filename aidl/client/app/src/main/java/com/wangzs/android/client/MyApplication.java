package com.wangzs.android.client;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by wangzs on 2015/11/1.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init("wangzs");
        Logger.d("====> init");
    }
}
