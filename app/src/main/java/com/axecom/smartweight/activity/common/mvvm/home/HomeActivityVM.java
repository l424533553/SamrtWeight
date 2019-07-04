package com.axecom.smartweight.activity.common.mvvm.home;

import android.os.Build;

import com.axecom.smartweight.adapter.GoodsTradeAdapter;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.mvvm.view.IAllView;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.utils.LiveBus;

import static com.axecom.smartweight.config.IConstants.SP_IS_FIRST_INIT;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_OK_BY_NET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_VALIDATE_FINISH;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

/**
 * 作者：罗发新
 * 时间：2019/6/21 0021    星期五
 * 邮件：424533553@qq.com
 * 说明：
 */
public class HomeActivityVM {

    private INetDataModel iNetDataModel;
    private SysApplication application;

    public HomeActivityVM(IAllView mNewsView) {
        application = (SysApplication) mNewsView.getApplication();
        iNetDataModel = new NetDataModelImpl(application, application.getTidType());

    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        application.getThreadPool().execute(() -> {
            if (Build.VERSION.SDK_INT >= 21) {
                iNetDataModel.getUserInfoExByRetrofit();
            } else {
                iNetDataModel.getUserInfoEx();
            }
        });
    }

    /**
     *
     */
    public void validateUserInfo() {
        UserInfo userInfo = iNetDataModel.getUserInfo();
        if (userInfo != null) {
            // 比对两次的信息是否一样
            UserInfo appInfo = application.getUserInfo();
            if (appInfo != null) {
                if (appInfo.getSellerid() != userInfo.getSellerid()) {
                    MyPreferenceUtils.getSp(application).edit().putBoolean(SP_IS_FIRST_INIT, true).apply();
                }
            }
            application.setUserInfo(userInfo);//保存信息
            LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_GET_USERINFO_VALIDATE_FINISH);
        }

    }
}
