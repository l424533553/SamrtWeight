package com.axecom.smartweight.my.entity;

import java.io.Serializable;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:事件总线
 */
public class BaseBusEvent {
    public final static String MARKET_NOTICE = "market_notice";//市场通知
    public final static String TYPE_GET_K_VALUE = "getKValue";//获取K值
    public final static String ACTION_UNLOCK_SOFT = "unlock_soft";// 如果锁定状态，则解锁软件，可以正常使用
    public final static String BACKGROUND_CHANGE = "background_change";// 如果锁定状态，则解锁软件，可以正常使用
    public final static String NOTIFY_WEIGHT_SX15 = "weight_xs15";//香山15.6寸屏 称重通知
    public final static String NOTIFY_WEIGHT_ST = "weight_st";//商通秤的 称重数据变化 通知
    public final static String NOTIFY_WEIGHT_KVALUE = "weight_k_value";//秤的K值

    public final static String NOTIFY_HOT_GOOD_CHANGE = "hotGoods";//商品菜单改变了 ，可能是数量，可能是追溯变化了
    public final static String NOTIFY_USERINFO = "userInfo";//用户信息更新过了

    private String eventType;
    private Object other;
    private Serializable serializable;

    public Serializable getSerializable() {
        return serializable;
    }

    public void setSerializable(Serializable serializable) {
        this.serializable = serializable;
    }

    public BaseBusEvent() {
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getOther() {
        return other;
    }

    //可以是实体对象
    public void setOther(Object other) {
        this.other = other;
    }
}

