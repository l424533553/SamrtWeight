package com.axecom.smartweight.mvvm.viewmodel;


import android.databinding.ObservableField;

import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.adapter.GoodsTradeAdapter;
import com.axecom.smartweight.mvvm.model.DataModelImpl;
import com.axecom.smartweight.mvvm.model.GoodsTradeBean;
import com.axecom.smartweight.mvvm.model.IDataModel;
import com.axecom.smartweight.mvvm.view.IAllView;
import com.xuanyuan.library.utils.MyDateUtils;
import com.xuanyuan.library.utils.LiveBus;

import java.util.Date;
import java.util.List;

import static com.axecom.smartweight.config.IConstants.LIVE_EVENT_NOTIFY_GOOD_TRADE;

import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;
import static com.xuanyuan.library.config.IConfig.TIME;

/**
 * 作者： 周旭 on 2017年10月18日 0018.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class ProductFragmentVM implements DataListener {
    private IDataModel iDataModel;
    private IAllView mNewsView;
    // 选择的时间
    private long selecTime;
    private SysApplication application;

    public ProductFragmentVM(IAllView mNewsView, GoodsTradeAdapter mAdapter) {
        this.mNewsView = mNewsView;
        iDataModel = new DataModelImpl();
        selecTime = System.currentTimeMillis();
        application = (SysApplication) mNewsView.getApplication();
    }

    private List<GoodsTradeBean> list;

    public List<GoodsTradeBean> getList() {
        return list;
    }

    //用户名的绑定
    public ObservableField<String> queryDate = new ObservableField<>(MyDateUtils.getY2D(System.currentTimeMillis()));

    /**
     * 第一次获取新闻数据
     */
    public void getNewsData() {
        queryDate.set(MyDateUtils.getY2D(selecTime));
        application.getThreadPool().execute(() -> {
            list = iDataModel.loadData(selecTime, ProductFragmentVM.this);
            LiveBus.post(EVENT_BUS_COMMON, String.class, LIVE_EVENT_NOTIFY_GOOD_TRADE);
        });
    }

    /**
     * 第一次获取数据，
     */
    public void getSearch(String dateFrom, String dateTo) {
        application.getThreadPool().execute(() -> {
            list = iDataModel.loadData(dateFrom, dateTo, ProductFragmentVM.this);
            LiveBus.post(EVENT_BUS_COMMON, String.class, LIVE_EVENT_NOTIFY_GOOD_TRADE);
        });
    }

    /**
     * 查找前一天的数据
     */
    public void findDataBefore() {
        selecTime -= TIME;
        getNewsData();
    }

    /**
     * 查找后一天的数据 ，
     */
    public void findDataNext() {
        selecTime += TIME;
        getNewsData();
    }

    /**
     * @param dateFrom 起始日期
     * @param dateTo   开始日期
     */
    public void findDataSearch(String dateFrom, String dateTo) {
        String from = dateFrom + " 00:00:00";
        String to = dateTo + " 00:00:00";
        getSearch(from, to);
    }
}

