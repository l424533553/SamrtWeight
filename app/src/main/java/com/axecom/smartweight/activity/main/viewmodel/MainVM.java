package com.axecom.smartweight.activity.main.viewmodel;

import com.axecom.smartweight.activity.main.model.IMainModel;
import com.axecom.smartweight.activity.main.model.MainModel;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.UserInfo;
import com.axecom.smartweight.mvvm.view.IAllView;
import com.xuanyuan.library.help.ActivityController;
import com.xuanyuan.library.utils.LiveBus;

import java.util.List;

import static com.axecom.smartweight.config.IConstants.LIVE_EVENT_NOTIFY_HOT_GOOD;
import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

/**
 * 作者：罗发新
 * 时间：2019/6/4 0004    星期二
 * 邮件：424533553@qq.com
 * 说明：
 */
public class MainVM {
    /**
     * 记录点击时间
     */
    private long recordClickTime;

    public UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    private IMainModel iMainModel;
    private IAllView iView;
    private SysApplication application;
    // 清单列表
    private List<HotGood> hotGoods;

    public List<HotGood> getHotGoods() {
        return hotGoods;
    }

    private long startTime;//开始时间
    private long currentTime;//当前时间

    private float oldWeight;//记录上一次的重量

    private int stableCount;//稳定的次数
    private boolean isStable;// 是否是稳定状态

    public boolean isStable(float currentWeight) {
        if (currentWeight == oldWeight) {
            stableCount++;
            if (stableCount >= 2) {
                isStable = true;
                return true;
            }
        } else {
            stableCount = 0;
            isStable = false;
        }
        return isStable;
    }

    public void setStable(boolean stable) {
        isStable = stable;
    }

    /**
     * 返回是否稳定
     */
    public boolean isStable() {
        return isStable;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * 初始化时间
     */
    public void initTime() {
        startTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();
    }

    public MainVM(IAllView iView) {
        iMainModel = new MainModel();
        this.iView = iView;
        application = (SysApplication) iView.getApplication();
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        if (userInfo != null) {
            userInfo.notifyChange();
        }
    }

    /**
     * 获取商品列表数据
     */
    public void initHotGoods() {
        application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                hotGoods = iMainModel.getHotGoods();
                LiveBus.post(EVENT_BUS_COMMON, String.class, LIVE_EVENT_NOTIFY_HOT_GOOD);
            }
        });
    }


    /**
     * 是否关闭所有的activity
     */
    public boolean finishAll() {
        // 2s 内
        if (System.currentTimeMillis() - recordClickTime > 2000) {
            recordClickTime = System.currentTimeMillis();
            return false;
        } else {
            ActivityController.finishAll();
            return true;
        }
    }


}
