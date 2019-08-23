package com.xuanyuan.library.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDexApplication;


/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * Bugly框架 包  ，必须继承MultiDexApplication  ，以为方法数超过了限制
 * <p>
 * 1.Android3.1版本后，每个App都必须要有至少有一个Activity，并且需要配置intent-filter，
 * 配置中的内容可以修改，比如设置 后看不见软件的图标
 * <intent-filter>
 * <action android:name="android.intent.action.MAIN" />
 * <category android:name="android.intent.category.LAUNCHER" />
 * </intent-filter>
 */
@SuppressLint("Registered")
public class BaseApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }

    public static Context getInstance() {
        return sInstance;
    }


}
