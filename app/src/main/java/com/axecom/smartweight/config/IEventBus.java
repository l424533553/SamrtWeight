package com.axecom.smartweight.config;

/**
 * 作者：罗发新
 * 时间：2019/4/22 0022    星期一
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IEventBus {


    String MARKET_NOTICE = "market_notice";//市场通知
    String TYPE_GET_K_VALUE = "getKValue";//获取K值
    String ACTION_UNLOCK_SOFT = "unlock_soft";// 如果锁定状态，则解锁软件，可以正常使用
    String BACKGROUND_CHANGE = "background_change";// 如果锁定状态，则解锁软件，可以正常使用
    String SHOPPING_BAG_PRICE_CHANGE = "ShoppingBagPrice_change";//购物袋 价格变化

    String WEIGHT_SX15 = "weight_xs15";//香山15.6寸屏 称重通知
    String WEIGHT_AXE = "weight_axe";//自研秤的称重数据
    String WEIGHT_SX = "weight_sx";//深信的称重数据
    String WEIGHT_SX8 = "weight_xs8";//香山8寸屏 称重通知
    String WEIGHT_KEY_PRESS = "weight_key_press";//按键
    String WEIGHT_ST = "weight_st";//商通秤的 称重数据变化 通知
    String WEIGHT_KVALUE = "weight_k_value";//秤的K值
    String WEIGHT_CLEAN_CHEAT = "weight_clean_cheat";//去除拆机标识
    String WEIGHT_ZEROING = "weight_zeroing";//秤置零
    String WEIGHT_CALIBRATION = "weight_calibration";//秤进行了标定
    String WEIGHT_ELECTRIC = "weight_electric";//电量数据
    String WEIGHT_INSTRUCTION_TEST = "weight_ instruction_test";//指令测试

    String NOTIFY_HOT_GOOD_CHANGE = "hotGoods";//商品菜单改变了 ，可能是数量，可能是追溯变化了
    String NOTIFY_USERINFO = "userInfo";//用户信息更新过了

    String NOTIFY_SMALLL_ROUTINE = "smallRoutine";//更新小程序二维码
    String NOTIFY_AD_SECOND = "adSecond";//更新副屏广告

    String EVENT_GET_USERINFO_ERR_BY_NET = "EVENT_GET_USERINFO_ERR_BY_NET";//通过网络获取用户信息失败
    String EVENT_GET_USERINFO_OK_BY_NET = "EVENT_GET_USERINFO_OK_BY_NET";//通过网络获取用户信息成功
    String EVENT_GET_USERINFO_VALIDATE_FINISH = "EVENT_GET_USERINFO_VALIDATE_FINISH";//通过网络获取用户信息成功
    String EVENT_GET_USERINFO_NOT_GET = "EVENT_GET_USERINFO_NOT_GET";//未获取到用户信息参数
}
