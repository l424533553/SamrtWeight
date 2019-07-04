package com.axecom.smartweight.activity.common.mvvm.home;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.dao.OrderBeanDao;
import com.axecom.smartweight.entity.dao.UserInfoDao;
import com.axecom.smartweight.entity.netresult.ResultInfo;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.helper.HttpHelper;
import com.axecom.smartweight.mvvm.model.GoodsTradeBean;
import com.axecom.smartweight.mvvm.viewmodel.DataListener;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.MyToast;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.utils.LiveBus;
import com.xuanyuan.library.utils.MyDateUtils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.axecom.smartweight.config.IConstants.EVENT_NET_WORK_AVAILABLE;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_ERR_BY_NET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_NOT_GET;
import static com.axecom.smartweight.config.IEventBus.EVENT_GET_USERINFO_OK_BY_NET;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class NetDataModelImpl implements INetDataModel, VolleyListener {

    private final int FLAG_GET_USER = 1;
    private HttpHelper httpHelper;
    // 秤的类型 0--4
    private int tidType;
    private UserInfoDao userInfoDao;

    public NetDataModelImpl(SysApplication application, int tidType) {
        httpHelper = new HttpHelper(application);
        this.tidType = tidType;
    }

    private UserInfoDao getUserInfoDao() {
        if (userInfoDao == null) {
            userInfoDao = new UserInfoDao();
        }
        return userInfoDao;
    }

    @Override
    public void getUserInfoExByRetrofit() {

    }

    @Override
    public void getUserInfoEx() {
        httpHelper.getUserInfoEx(this, FLAG_GET_USER);
    }

    @Override
    public UserInfo getUserInfo() {
        List<UserInfo> lists = getUserInfoDao().queryByColumnName("id", 1);
        if (lists != null) {
            if (lists.size() >= 1) {
                return lists.get(0);
            }
        }
        return null;
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        switch (flag) {
            case FLAG_GET_USER:
                LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_GET_USERINFO_ERR_BY_NET);
                break;
        }

    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        final ResultInfo resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo.class);
        switch (flag) {
            case FLAG_GET_USER:
                if (resultInfo != null && resultInfo.getStatus() == 0) {
                    UserInfo userInfo = JSON.parseObject(resultInfo.getData(), UserInfo.class);
                    if (userInfo != null && httpHelper.validateUserInfo(userInfo, tidType)) {
                        userInfo.setId(1);
                        getUserInfoDao().updateOrInsert(userInfo);
                        LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_GET_USERINFO_OK_BY_NET);
                        return;
//                        // 比对两次的信息是否一样
//                        UserInfo appInfo = sysApplication.getUserInfo();
//                        if (appInfo != null) {
//                            if (appInfo.getSellerid() != userInfo.getSellerid()) {
//                                MyPreferenceUtils.getSp(context).edit().putBoolean(SP_IS_FIRST_INIT, true).apply();
//                            }
//                        }
//                        application.setUserInfo(userInfo);//保存信息
//                        sysApplication.setUserInfo(userInfo);//保存信息
//                        isUserInfoAble = true;
//                        startLogin();
                    }
                }
                LiveBus.post(EVENT_BUS_COMMON, String.class, EVENT_GET_USERINFO_NOT_GET);

                break;
        }
    }
}
