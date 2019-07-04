package com.axecom.smartweight.base;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.axecom.smartweight.activity.SecondScreen;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.helper.weighter.SXWeighter;
import com.xuanyuan.library.base2.BuglyUPHelper;
import com.xuanyuan.library.base2.MyBaseApplication;
import com.axecom.smartweight.mvvm.retrofit.IRetrofitAPI;
import com.axecom.smartweight.mvvm.retrofit.RetrofitHttpUtils;
import com.axecom.smartweight.helper.printer.EPSPrint;
import com.axecom.smartweight.helper.printer.HDDPrinter;
import com.axecom.smartweight.helper.printer.MyBasePrinter;
import com.axecom.smartweight.helper.printer.WHPrinter8;
import com.axecom.smartweight.helper.printer.WHPrinterAXE;
import com.axecom.smartweight.helper.printer.WHPrinterE39;
import com.axecom.smartweight.helper.weighter.AXEWeighter;
import com.axecom.smartweight.helper.weighter.MyBaseWeighter;
import com.axecom.smartweight.helper.weighter.STWeighter;
import com.axecom.smartweight.helper.weighter.XSWeighter15;
import com.axecom.smartweight.helper.weighter.XSWeighter8;
import com.axecom.smartweight.utils.security.DesBCBHelper;
import com.xuanyuan.library.utils.text.StringUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;

import java.util.Objects;

import static com.axecom.smartweight.config.IConstants.IS_NO_PRINT_QR;
import static com.axecom.smartweight.config.IConstants.PRINTER_HHD;
import static com.axecom.smartweight.config.IConstants.PRINTER_TYPE;

/**
 * Created by Longer on 2016/10/26.
 */
public class SysApplication extends MyBaseApplication {
    private final static String USER_ST = "android-build";
    private final static String USER_XS_15 = "liaokai";
    private final static String USER_XS_8 = "liuyegang";
    private final static String USER_AXB = "root";
    private final static String USER_SX_442 = "liubin";
    private static Handler mHandler;
    //    private  static long mMainThreadId;
    private UserInfo userInfo;
    private boolean isConfigureEpayParams;//是否配置了 支付参数

    public boolean isConfigureEpayParams() {
        return isConfigureEpayParams;
    }

    public void setConfigureEpayParams(boolean configureEpayParams) {
        isConfigureEpayParams = configureEpayParams;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        isConfigureEpayParams = false;
        if (userInfo != null) {
            if (!StringUtils.isEmpty(userInfo.getMchid()) && !StringUtils.isEmpty(userInfo.getKey())) {
                isConfigureEpayParams = true;
            }
        }
    }

    private MyBasePrinter print;

    public MyBasePrinter getPrint() {
        return print;
    }
//    public static Context getContext() {
//        return mContext;
//    }

    public static Handler getHandler() {
        return mHandler;
    }

    public DesBCBHelper getDesBCBHelper() {
        return DesBCBHelper.getmInstants();
    }

    @Override
    public void onCreate() {// 程序的入口方法
        super.onCreate();
        // 2.主线程的Handler
        mHandler = new Handler();
        if (Build.VERSION.SDK_INT >= 21) {//okhttp仅支持最小21
            RetrofitHttpUtils retrofitHttpUtils = new RetrofitHttpUtils();
            retrofitHttpUtils.init(IRetrofitAPI.BASE_IP_HOST, this);
        }

        // 非测试模式
        if (!DEBUG_MODE) {
            tidType = decisionScaleType();
            modularUnit();
            startBanner();
        }
        MyLog.e("ggk", "sysApplication   onCreate");
//        initBugly(this);
    }

//    /**
//     * 进行Bugly设置，开启更新之旅。
//     */
//    private void initBugly() {
//        if (!DEBUG_MODE) {
//            BuglyUPHelper buglyUPHelper = new BuglyUPHelper(this.getApplicationContext());
//            BuglyStrategy strategy = new BuglyStrategy();
//            // 设置app渠道号，设定了渠道号
//            strategy.setAppChannel(BuglyUPHelper.APP_CHANNEL);
//
//
////            buglyUPHelper.setContext(getApplicationContext());
//            //需要发在初始化的后面
////            buglyUPHelper.setUpgradeListener();
//            buglyUPHelper.setUILifecycleListener();
////            buglyUPHelper.setUpgradeStateListener();
//
//            buglyUPHelper.initUpdateConfig();
//            buglyUPHelper.initBugly(strategy);
//        }
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MyLog.e("ggk", "sysApplication   onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MyLog.e("ggk", "sysApplication   onTerminate");
    }

    /**
     * OK 判定秤的种类
     *
     * @return 秤的种类类型
     */
    private int decisionScaleType() {
        String user = android.os.Build.USER;
        String name = android.os.Build.DEVICE;
        if (TextUtils.isEmpty(user)) {
            return TID_TYPE_ERROR;
        } else if (USER_ST.equalsIgnoreCase(user)) {
            return TID_TYPE_ST;
        } else if (USER_XS_15.equalsIgnoreCase(user)) {
            return TID_TYPE_XS15;
        } else if (USER_XS_8.equalsIgnoreCase(user)) {
            return TID_TYPE_XS8;
        } else if (USER_AXB.equalsIgnoreCase(user)) {
            return TID_TYPE_AXE;
        } else if (USER_SX_442.equalsIgnoreCase(user)) {
            return TID_TYPE_SX442;
        }
        return TID_TYPE_OTHER;
    }

    private SecondScreen banner;// 第二

    public SecondScreen getBanner() {
        if (banner == null) {
            startBanner();
        }
        return banner;
    }

    /**
     * 启动广告 Dialog
     */
    public void startBanner() {
        DisplayManager displayManager = (DisplayManager) getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        //获取屏幕数量
        Display[] presentationDisplays = displayManager.getDisplays();
        if (presentationDisplays.length > 1) {
            if (banner == null) {
                banner = new SecondScreen(getApplicationContext(), presentationDisplays[1]);
                Objects.requireNonNull(banner.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            banner.show();
        }
    }

    // 称的类型  0为商通  ，1 为XS15 , 2 为8寸。  3 axe自研秤   4 深信秤
    private int tidType = 0;
    private final static int TID_TYPE_ST = 0;
    private final static int TID_TYPE_XS15 = 1;
    private final static int TID_TYPE_XS8 = 2;
    private final static int TID_TYPE_AXE = 3;
    private final static int TID_TYPE_SX442 = 4;
    private final static int TID_TYPE_OTHER = -2;
    private final static int TID_TYPE_ERROR = -1;

    public int getTidType() {
        return tidType;
    }

    public boolean isOpenPrinter() {
        return isOpenPrinter;
    }

    private MyBaseWeighter myBaseWeighter;
    private boolean isOpenWeight = false;//称重串口是否打开
    private boolean isOpenPrinter = false;//打印口是否打开了

    public MyBaseWeighter getMyBaseWeighter() {
        return myBaseWeighter;
    }

    /**
     * 初始化模块单元
     * 1.初始化打印机
     */
    private void modularUnit() {
        if (tidType == 0) {
            print = new EPSPrint(EPSPrint.PATH_ST, EPSPrint.BAUDRATE_ST);
        } else if (tidType == 1) {
            int printerIndex = MyPreferenceUtils.getSp(getApplicationContext()).getInt(PRINTER_TYPE, PRINTER_HHD);
            if (printerIndex == 0) {
                print = new HDDPrinter(HDDPrinter.PATH_XS15, HDDPrinter.BAUDRATE_XS15);
            } else {
                print = new WHPrinterE39(WHPrinterE39.PATH_XS15, WHPrinterE39.BAUDRATE_XS15);
            }
        } else if (tidType == 2) {
            print = new WHPrinter8(WHPrinter8.PATH_XS8, WHPrinter8.BAUDRATE_XS8);
        } else if (tidType == 3) {
            //TODO  自研秤打印机好了之后 ，请恢复下面的代码
//            print = new WHPrinterAXE(WHPrinterAXE.PATH_AXE, WHPrinterAXE.BAUDRATE_AXE);
        }
        if (print != null) {
            isOpenPrinter = print.open();
            if (!isOpenPrinter) {
                MyToast.toastLong(getContext(), "打印机串口连接失败");
            } else {
                // 设置是否打印二维码
                boolean isNoQR = MyPreferenceUtils.getBoolean(getContext(), IS_NO_PRINT_QR, false);
                print.setNoQR(isNoQR);
            }
        }

        if (tidType == 0) {
            myBaseWeighter = new STWeighter();
            isOpenWeight = myBaseWeighter.open();
        } else if (tidType == 1) {
            myBaseWeighter = new XSWeighter15();
            isOpenWeight = myBaseWeighter.open();
        } else if (tidType == 2) {
            myBaseWeighter = new XSWeighter8();
            isOpenWeight = myBaseWeighter.open();
        } else if (tidType == 3) {
            myBaseWeighter = new AXEWeighter();
            isOpenWeight = myBaseWeighter.open();
        } else if (tidType == 4) {
            myBaseWeighter = new SXWeighter();
            isOpenWeight = myBaseWeighter.open();
        }
        if (!isOpenWeight) {
            MyToast.toastShort(getContext(), "称重串口连接失败");
        }

        Log.i("test", "测试12345678");
    }

    /**
     * 最终关闭串口
     */
    public void onDestory() {
        if (myBaseWeighter != null) {
            myBaseWeighter.closeSerialPort();
        }
        if (print != null) {
            if (isOpenPrinter) {
                //测试中
//                print.closeSerialPort();
            }
        }
    }
}

