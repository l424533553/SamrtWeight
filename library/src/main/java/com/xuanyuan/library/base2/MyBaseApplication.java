package com.xuanyuan.library.base2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.xuanyuan.library.utils.LogPrint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 */
@SuppressLint("Registered")
public class MyBaseApplication extends BaseVolleyApplication {
    //  线程池  记得要关闭
    protected ExecutorService threadPool;
    protected ExecutorService singleThread;

    public ExecutorService getSingleThread() {
        return singleThread;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    /**
     * @return 全局上下文对象
     */
    public static Context getInstance() {
        return sInstance;
    }

    @SuppressLint("StaticFieldLeak")
    protected static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        threadPool = Executors.newFixedThreadPool(5);
        singleThread = Executors.newSingleThreadExecutor();
        initLiveEventBus();


        //TODO 上传给平台需要解禁
//        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        // 注册crashHandler
//        crashHandler.init(getApplicationContext());
    }

    //
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogPrint.i("sysApplication   onLowMemory");
    }

    /**
     * 初始化  LiveEventBus
     */
    private void initLiveEventBus() {
        LiveEventBus.get().config().supportBroadcast(this).lifecycleObserverAlwaysActive(true);
    }

    /**
     * 使用情况
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        //关闭线程池
        threadPool.shutdown();
        singleThread.shutdown();
        LogPrint.i("sysApplication   onTerminate");
    }

}
