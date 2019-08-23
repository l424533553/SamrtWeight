package com.axecom.smartweight.activity.main.viewmodel;

import com.axecom.smartweight.activity.main.model.IMainModel;
import com.axecom.smartweight.activity.main.model.MainModel;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.entity.dao.OrderInfoDao;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.OrderInfo;
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

    public IMainModel getiMainModel() {
        return iMainModel;
    }

    private IAllView iView;
    private SysApplication application;
    // 清单列表
    private List<HotGood> hotGoods;

    public List<HotGood> getHotGoods() {
        return hotGoods;
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
        application.getThreadPool().execute(() -> {
            hotGoods = iMainModel.getHotGoods();
            LiveBus.post(EVENT_BUS_COMMON, String.class, LIVE_EVENT_NOTIFY_HOT_GOOD);
        });
    }

//    /**
//     * @param billcode
//     */
//    public void updateOrderBillcode(String billcode) {
//        iMainModel.updateOrderBillcode(billcode);
//    }
//
//    public void insertOrderInfo(OrderInfo orderInfo) {
//        iMainModel.insertOrderInfo(orderInfo);
//    }

    /**
     * 是否关闭所有的activity
     */
    public boolean finishAll() {
        if (System.currentTimeMillis() - recordClickTime > 2000) {
            recordClickTime = System.currentTimeMillis();
            return true;
        } else {
            ActivityController.finishAll(true);
            return false;
        }
    }

}
