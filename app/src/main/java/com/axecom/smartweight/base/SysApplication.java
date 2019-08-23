package com.axecom.smartweight.base;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import com.axecom.smartweight.activity.SecondScreen;
import com.axecom.smartweight.entity.fpms_chinaap.UpdateTempBean;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.helper.TidUtils;
import com.axecom.smartweight.helper.printer.AdapterFactory;
import com.axecom.smartweight.helper.printer.MyBasePrinter;
import com.axecom.smartweight.helper.weighter.MyBaseWeighter;
import com.axecom.smartweight.mvvm.retrofit.IRetrofitAPI;
import com.axecom.smartweight.mvvm.retrofit.RetrofitHttpUtils;
import com.axecom.smartweight.utils.security.DesBCBHelper;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.base2.MyBaseApplication;
import com.xuanyuan.library.service.KeyDownService;
import com.xuanyuan.library.utils.LogPrint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.axecom.smartweight.config.IConstants.AXE_COUNT_ERR;
import static com.axecom.smartweight.config.IConstants.AXE_COUNT_OK;
import static com.axecom.smartweight.config.IConstants.FPS_COUNT_ERR;
import static com.axecom.smartweight.config.IConstants.FPS_COUNT_OK;

/**
 * Created by Longer on 2016/10/26.
 */
public class SysApplication extends MyBaseApplication {
    private static Handler mHandler;
    //    private  static long mMainThreadId;
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public DesBCBHelper getDesBCBHelper() {
        return DesBCBHelper.getmInstants();
    }

    private Intent keyDownService;

    @Override
    public void onCreate() {// 程序的入口方法
        super.onCreate();
        // 2.主线程的Handler
        mHandler = new Handler();

        if (Build.VERSION.SDK_INT >= 21) {//okhttp仅支持最小21
            RetrofitHttpUtils retrofitHttpUtils = new RetrofitHttpUtils();
            retrofitHttpUtils.init(IRetrofitAPI.BASE_IP_HOST, this);
        }

        // 非测试模式，才会尽心初始化
        if (!DEBUG_MODE) {
            tidType = TidUtils.decisionScaleType();
            modularUnit(tidType);
            startBanner();
        }
        LogPrint.i("sysApplication   onCreate");
        initConfig(this);
    }

    /**
     * 临时变量
     */
    List<UpdateTempBean> updateBeanFpmsList = new ArrayList<>();
    List<UpdateTempBean> updateBeanAxeList = new ArrayList<>();

    public List<UpdateTempBean> getUpdateBeanFpmsList() {
        return updateBeanFpmsList;
    }

    public void setUpdateBeanFpmsList(List<UpdateTempBean> updateBeanFpmsList) {
        this.updateBeanFpmsList = updateBeanFpmsList;
    }

    public List<UpdateTempBean> getUpdateBeanAxeList() {
        return updateBeanAxeList;
    }

    public void setUpdateBeanAxeList(List<UpdateTempBean> updateBeanAxeList) {
        this.updateBeanAxeList = updateBeanAxeList;
    }

    /**
     * 初始化 配置参数
     */
    private void initConfig(Context context) {
        MyPreferenceUtils.getSp(context).edit()
                .putInt(AXE_COUNT_ERR, 0)
                .putInt(AXE_COUNT_OK, 0)
                .putInt(FPS_COUNT_ERR, 0)
                .putInt(FPS_COUNT_OK, 0).apply();
        updateBeanAxeList.clear();
        updateBeanFpmsList.clear();
    }

    private SecondScreen banner;// 第二

    public SecondScreen getBanner() {
        if (banner == null) {
            startBanner();
        }
        return banner;
    }

    /**
     * 启动副屏
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

    //
    private int tidType = 0;

    public int getTidType() {
        return tidType;
    }

    public MyBaseWeighter getMyBaseWeighter() {
        return adapterFactory.getWeighter();
    }

    public MyBasePrinter getPrint() {
        return adapterFactory.getPrinter();
    }

    private AdapterFactory adapterFactory;

    public AdapterFactory getAdapterFactory() {
        return adapterFactory;
    }

    /**
     * VM
     * 初始化模块单元
     * 1.初始化打印机
     */
    private void modularUnit(int tidType) {
        adapterFactory = new AdapterFactory(tidType);
        if (!adapterFactory.createPrinter(tidType)) {
            MyToast.showError(this, "打印机未连通！");
        }
        if (!adapterFactory.createWeighter(tidType)) {
            MyToast.showError(sInstance, "称重串口连接失败");
        }

        if (tidType == 4) {
            //开启辅助服务
            keyDownService = new Intent(this, KeyDownService.class);
            startService(keyDownService);
        }
    }

    /**
     * VM
     * 最终关闭串口
     */
    public void onDestory() {
        adapterFactory.closeConfig();
        if (keyDownService != null) {
            stopService(keyDownService);
        }
    }
}

