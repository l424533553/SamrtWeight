package com.luofx.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.toolbox.Volley;
import com.axecom.smartweight.my.config.IConstants;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.luofx.entity.Deviceinfo;
import com.luofx.entity.dao.DeviceInfoDao;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuanyuan.library.MyPreferenceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 说明：
 * 作者：User_luo on 2018/7/24 13:52
 * 邮箱：424533553@qq.com
 * 需要导入Volley.jar 或者  远程依赖
 */
@SuppressLint("Registered")
public class MyBaseApplication extends BaseVolleyApplication implements IConstants {
    private Context context;
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
     * 设备信息
     */
    private Deviceinfo deviceinfo;

    public Deviceinfo getDeviceinfo() {
        return deviceinfo;
    }

    public void setDeviceinfo(Deviceinfo deviceinfo) {
        this.deviceinfo = deviceinfo;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        queues = Volley.newRequestQueue(getApplicationContext());
        threadPool = Executors.newFixedThreadPool(5);
        singleThread = Executors.newSingleThreadExecutor();
        readyDevice();
        initLiveEventBus();

        //TODO 上传给平台需要解禁
//        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        // 注册crashHandler
//        crashHandler.init(getApplicationContext());

    }

    /**
     * 初始化  LiveEventBus
     */
    private void initLiveEventBus() {
        LiveEventBus.get()
                .config()
                .supportBroadcast(this)
                .lifecycleObserverAlwaysActive(true);
    }

    /**
     * 初始化
     */
    private void initBugly() {

        //建议开发时 为true ,发布时为false
//        CrashReport.initCrashReport(getApplicationContext(), "c907c1f71b", true);

        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this.getContext());
        strategy.setAppPackageName(getApplicationContext().getPackageName());
        PackageInfo packageInfo;
        try {
            packageInfo = this.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
//            localVersion = packageInfo.versionCode;

            strategy.setAppVersion(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        CrashReport.initCrashReport(context, "c907c1f71b", false, strategy);


//        Context context = getApplicationContext();
//// 获取当前包名
//        String packageName = context.getPackageName();
//// 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//// 设置是否为上报进程
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//// 初始化Bugly
//        CrashReport.initCrashReport(context, "c907c1f71b", false, strategy);


    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 检测 设备信息
     */
    private void readyDevice() {
        // 检查获取信息
        boolean hasDevice = MyPreferenceUtils.getBoolean(context, IS_HAS_DEVICE);
        if (!hasDevice) {
            getHardwareInfo();
            MyPreferenceUtils.setBoolean(context, IS_HAS_DEVICE, true);
        } else {
            DeviceInfoDao deviceInfoDao = new DeviceInfoDao(context);
            Deviceinfo deviceinfo = deviceInfoDao.queryById(1);
            setDeviceinfo(deviceinfo);
        }
    }

    /**
     * 获取硬件信息
     */
    private void getHardwareInfo() {
        TelephonyManager phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        @SuppressLint("HardwareIds")
        String mac = wifi.getConnectionInfo().getMacAddress();   // mac 地址
        int phonetype = phone.getPhoneType();  //  手机类型
        String model = android.os.Build.MODEL; // ****  手机型号
        String sdk = String.valueOf(Build.VERSION.SDK_INT); // 系统版本值

        String brand = android.os.Build.BRAND;
        String release = phone.getDeviceSoftwareVersion();// 系统版本 ,
        int networktype = phone.getNetworkType();   // 网络类型
        String networkoperatorname = phone.getNetworkOperatorName();   // 网络类型名
        String radioVersion = android.os.Build.getRadioVersion();   // 固件版本

        // 设备信息
        Deviceinfo deviceinfo = new Deviceinfo(release, sdk, brand, model, networkoperatorname, networktype, phonetype, mac, radioVersion);
        DeviceInfoDao deviceInfoDao = new DeviceInfoDao(context);
        deviceInfoDao.insert(deviceinfo);
        setDeviceinfo(deviceinfo);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //关闭线程池
        threadPool.shutdown();
        singleThread.shutdown();
    }


}
