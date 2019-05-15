package com.axecom.smartweight.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.axecom.smartweight.carouselservice.service.SecondScreen;
import com.axecom.smartweight.my.entity.UserInfo;
import com.luofx.base.MyBaseApplication;
import com.luofx.newclass.printer.EPSPrint;
import com.luofx.newclass.printer.MyBasePrinter;
import com.luofx.newclass.printer.WHPrinter15;
import com.luofx.newclass.printer.WHPrinter8;
import com.luofx.newclass.printer.WHPrinterAXE;
import com.luofx.newclass.weighter.MyBaseWeighter;
import com.luofx.newclass.weighter.STWeighter;
import com.luofx.newclass.weighter.XSWeighter15;
import com.luofx.newclass.weighter.XSWeighter8;
import com.luofx.newclass.weighter.XSWeighterAXE;
import com.luofx.utils.security.DesBCBHelper;
import com.luofx.utils.text.StringUtils;
import com.xuanyuan.library.MyLog;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;

import java.util.Objects;

/**
 * Created by Longer on 2016/10/26.
 */
public class SysApplication extends MyBaseApplication {
    private final static String USER_ST = "android-build";
    private final static String USER_XS_15 = "liaokai";
    private final static String USER_XS_8 = "liuyegang";
    private final static String USER_AXB = "root";
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


    public static Context getInstance() {
        return sInstance;
    }

    @SuppressLint("StaticFieldLeak")
    private static SysApplication sInstance;


    @Override
    public void onCreate() {// 程序的入口方法
        super.onCreate();
        // 2.主线程的Handler
        mHandler = new Handler();
        sInstance = this;
        // 非测试模式
        if (!DEBUG_MODE) {
            tidType = decisionScaleType();
            modularUnit();
            startBanner();
        }
        MyLog.e("ggk", "sysApplication   onCreate");
    }

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
     *
     * @return 秤的种类类型
     */
    private int decisionScaleType() {
        String user = android.os.Build.USER;
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

    // 称的类型  0为商通  ，1 为XS15 , 2 为8寸。
    private int tidType = 0;
    private final static int TID_TYPE_ST = 0;
    private final static int TID_TYPE_XS15 = 1;
    private final static int TID_TYPE_XS8 = 2;
    private final static int TID_TYPE_AXE = 3;
    private final static int TID_TYPE_OTHER = -2;
    private final static int TID_TYPE_ERROR = -1;

    public int getTidType() {
        return tidType;
    }

    private boolean isOpenPrinter;

    public boolean isOpenPrinter() {
        return isOpenPrinter;
    }

    private MyBaseWeighter myBaseWeighter;
    private boolean isOpenWeight = false;//称重串口是否打开


    public MyBaseWeighter getMyBaseWeighter() {
        return myBaseWeighter;
    }

    public boolean isOpenWeight() {
        return isOpenWeight;
    }

    /**
     * 初始化模块单元
     * 1.初始化打印机
     */
    private void modularUnit() {
        if (tidType == 0) {
            print = new EPSPrint(EPSPrint.PATH_ST, EPSPrint.BAUDRATE_ST);
        } else if (tidType == 1) {
            print = new WHPrinter15(WHPrinter15.PATH_XS15, WHPrinter15.BAUDRATE_XS15);
        } else if (tidType == 2) {
            print = new WHPrinter8(WHPrinter8.PATH_XS8, WHPrinter8.BAUDRATE_XS8);
        }else if(tidType == 3){
            print = new WHPrinterAXE(WHPrinter8.PATH_XS8, WHPrinter8.BAUDRATE_XS8);
        }
        if (print != null) {
            boolean isOpenPrinter = print.open();
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
        }else if(tidType == 3){
            myBaseWeighter = new XSWeighterAXE();
            isOpenWeight = myBaseWeighter.open();
        }
        if (!isOpenWeight) {
            MyToast.toastShort(getContext(), "称重串口连接失败");
        }
    }

    /**
     * 最终关闭串口
     */
    public void onDestory() {
        if (print != null) {
            print.closeSerialPort();
        }
    }
}

