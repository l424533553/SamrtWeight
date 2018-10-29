package com.shangtongyin;

import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.support.multidex.MultiDex;

import com.axecom.smartweight.my.entity.UserInfo;
import com.luofx.base.MyBaseApplication;
import com.luofx.utils.log.ErrorLog;
import com.luofx.utils.log.LogUtils;
import com.shangtongyin.tools.serialport.EpsPrint;
import com.shangtongyin.tools.serialport.USB_Print;

/**
 * Fun：
 * Author：Linus_Xie
 * Date：2018/8/2 14:55
 */
public class ShangTongApp extends MyBaseApplication  implements Thread.UncaughtExceptionHandler {


    private int marketid;

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private EpsPrint epsPrint;

    public EpsPrint getEpsPrint() {
        return epsPrint;
    }


    public ShangTongApp() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init();


        epsPrint = new EpsPrint();
        epsPrint.open();
//        registerBroadcast();
        USB_Print.initPrinter(this);
//        SharedPreferences sharedPreferences = PreferenceUtils.getSp(this);

//        Thread.setDefaultUncaughtExceptionHandler(this);


    }

    /**
     * 最终关闭串口
     */
    public void onDestory() {
        if (epsPrint != null) {
            epsPrint.closeSerialPort();
        }
    }

   /**
     * 获取到 未捕获的异常
     *
     * @param t 线程
     * @param e 异常
     */
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ErrorLog.errorLog(Thread.currentThread().getName() + "");
                ErrorLog.errorLog("Thread Id :" + t.getId());
                ErrorLog.errorLog("Thread Ex :" + e.toString());
                Looper.loop();
            }
        }).start();

        SystemClock.sleep(3000);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
