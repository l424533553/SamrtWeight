package com.luofx.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * Bugly框架 包  ，必须继承MultiDexApplication  ，以为方法数超过了限制
 */
@SuppressLint("Registered")
public class BaseBuglyApplication extends MultiDexApplication {
    /**
     * true 为调试模式
     */
    public static final boolean DEBUG_MODE = false;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 安装tinker， 不同的方式选择不一样
        if (!DEBUG_MODE) {
            Beta.installTinker();
        }
//      Beta.installTinker(this);
    }

    /**
     * 升级第一步 ：
     * 1.初始化  Bugly.init(getApplicationContext(), "注册时申请的APPID", false);
     */
    @Override
    public void onCreate() {
        super.onCreate();
        setStrictMode();
        initBugly();
    }

    /**
     * 进行Bugly设置，开启更新之旅。
     */
    private void initBugly() {
        if (!DEBUG_MODE) {
            BuglyUPHelper buglyUPHelper = new BuglyUPHelper();
            BuglyStrategy strategy = new BuglyStrategy();
            // 设置app渠道号，设定了渠道号
            strategy.setAppChannel(BuglyUPHelper.APP_CHANNEL);
            buglyUPHelper.setContext(getApplicationContext());
            //需要发在初始化的后面
//            buglyUPHelper.setUpgradeListener();
            buglyUPHelper.setUILifecycleListener();
//            buglyUPHelper.setUpgradeStateListener();
            buglyUPHelper.initBugly(strategy);
            buglyUPHelper.initUpdateConfig();
        }
    }

    //严格审查模式
    public void setStrictMode() {
        if (DEBUG_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()//线程策略（ThreadPolicy）
                    .detectDiskReads()//检测在UI线程读磁盘操作
                    .detectDiskWrites()//检测UI线程写磁盘操作
                    .detectCustomSlowCalls()//发现UI线程调用的哪些方法执行得比较慢
//                .detectResourceMismatches()//最低版本为API23  发现资源不匹配
                    .detectNetwork() //检测在UI线程执行网络操作
                    .penaltyDialog()//一旦检测到弹出Dialog
                    .penaltyDeath()//一旦检测到应用就会崩溃
                    .penaltyFlashScreen()//一旦检测到应用将闪屏退出 有的设备不支持
                    .penaltyDeathOnNetwork()//一旦检测到应用就会崩溃
                    .penaltyDropBox()//一旦检测到将信息存到DropBox文件夹中 data/system/dropbox
                    .penaltyLog()//一旦检测到将信息以LogCat的形式打印出来
                    .permitDiskReads()//允许UI线程在磁盘上读操作
                    .permitAll()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()//虚拟机策略（VmPolicy）
                    .detectActivityLeaks()//最低版本API11 用户检查 Activity 的内存泄露情况
//                .detectCleartextNetwork()//最低版本为API23  检测明文的网络
                    .detectFileUriExposure()//最低版本为API18   检测file://或者是content://
                    .detectLeakedClosableObjects()//最低版本API11  资源没有正确关闭时触发
                    .detectLeakedRegistrationObjects()//最低版本API16  BroadcastReceiver、ServiceConnection是否被释放
                    .detectLeakedSqlLiteObjects()//最低版本API9   资源没有正确关闭时回触发
//                .setClassInstanceLimit(MyClass.class, 2)//设置某个类的同时处于内存中的实例上限，可以协助检查内存泄露
                    .penaltyLog()//与上面的一致
                    .penaltyDeath()
                    .detectAll()
                    .build());
        }
    }

}
