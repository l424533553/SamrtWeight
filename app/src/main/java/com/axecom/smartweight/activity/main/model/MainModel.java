package com.axecom.smartweight.activity.main.model;

import com.axecom.smartweight.entity.dao.HotGoodsDao;
import com.axecom.smartweight.entity.dao.OrderBeanDao;
import com.axecom.smartweight.entity.dao.OrderInfoDao;
import com.axecom.smartweight.entity.project.HotGood;
import com.axecom.smartweight.entity.project.OrderBean;
import com.axecom.smartweight.entity.project.OrderInfo;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/6/10 0010    星期一
 * 邮件：424533553@qq.com
 * 说明：
 */
public class MainModel implements IMainModel {
    public MainModel() {
    }

    private HotGoodsDao hotGoodsDao;

    private HotGoodsDao getHotGoodsDao() {
        if (hotGoodsDao == null) {
            hotGoodsDao = new HotGoodsDao();
        }
        return hotGoodsDao;
    }

    private OrderInfoDao orderInfoDao;

    private OrderInfoDao getOrderInfoDao() {
        if (orderInfoDao == null) {
            orderInfoDao = new OrderInfoDao();
        }
        return orderInfoDao;
    }

    private OrderBeanDao orderBeanDao;

    public OrderBeanDao getOrderBeanDao() {
        if (orderBeanDao == null) {
            orderBeanDao = new OrderBeanDao();
        }
        return orderBeanDao;
    }

    @Override
    public List<HotGood> getHotGoods() {
        return getHotGoodsDao().queryAll();
    }

    public void updateOrderBillcode(String billcode) {
        getOrderInfoDao().update(billcode);
    }

    public void insertOrderInfo(OrderInfo orderInfo) {
        getOrderInfoDao().insert(orderInfo);
    }

    @Override
    public boolean updateOrInsert(OrderInfo orderInfo) {
        return getOrderInfoDao().updateOrInsert(orderInfo);
    }

    @Override
    public List<OrderInfo> queryByState() {
        return getOrderInfoDao().queryByState();
    }

    @Override
    public int insertOrderBean(List<OrderBean> orderBeans) {
        return getOrderBeanDao().insert(orderBeans);
    }

}
