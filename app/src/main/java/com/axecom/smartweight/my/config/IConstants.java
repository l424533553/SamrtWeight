package com.axecom.smartweight.my.config;

/**
 * author: luofaxin
 * date： 2018/8/31 0031.
 * email:424533553@qq.com
 * describe:
 */
public interface IConstants {
    String USER_TOKEN = "user_token";
    String USER_SCALES_ID = "scales_id";
    /**
     * 称重更新了
     */

    int NOTIFY_MARQUEE = 9222;
    int NOTIFY_CLEAR = 9333;
    int NOTIFY_INITDAT = 9444;
    int NOTIFY_SUCCESS = 9555;
    int NOTIFY_EPAY_SUCCESS = 9666; //电子支付成功

    int NOTIFY_TRACENO = 9999;
    int NOTIFY_JUMP = 9888;
    int NOTIFY_TRACE_STATE = 9101;
    int NOTIFY_CLOSE_DIALOG = 9102;

    String BASE_IP_ST = "http://119.23.43.64";

    /**
     * 香山秤  的称重模块消息通知
     */
    int WEIGHT_NOTIFY_XS15 = 2115;// 香山秤15.6屏的通知
    int WEIGHT_NOTIFY_XS8 = 2114;// 香山秤15.6屏的通知

    int NOTIFY_AD_SECOND = 2033;//商通的称重通知
    int NOTIFY_AD_PICTURE_FORMAT = 2043;//商通的称重通知

    int PAYMENT_TIMEOUT = 180000;//等待支付超时时间

    int RC_SYS_SET = 55001;//请求系统设着

    String PRICE_LARGE = "largePrice";//大袋子价格
    String PRICE_MIDDLE = "middlePrice";
    String PRICE_SMALL = "smallPrice";

    String DEFAULT_BAG_ITEM_NO = "3610";//购物袋的商品id
    String IS_NO_PRINT_QR = "isNoPrintQr";//是否不打印二维码

    /**
     * 市场 id
     */
    String MARKET_ID = "marketid";
    String MARKET_NAME = "marketname";
    String STRING_TYPE = "type";

    /**
     * 秤编号id
     */
    String TID = "tid";

    String SELLER = "seller";
    String SELLER_ID = "sellerid";
    String STALL_NO = "stallNo";//摊位号

    String KEY = "key";
    String MCHID = "mchid";
    String BASE_WEB_IP = "base_web_ip";


    String BASE_COMPANY_NAME = "深圳市安鑫宝科技发展有限公司";

    String KET_SWITCH_SIMPLE_OR_COMPLEX = "key_switch_simple_or_complex";

    String ACTION_UNLOCK_SOFT = "com.axecom.smartweight.ui.activity.unlock";
    String LOCK_STATE = "lockState";


    /* 开机启动广播 */
    String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    String ACTION_START = "com.axecom.iweight.carouselservice.start";
    String ACTION_DESTORY = "com.axecom.iweight.carouselservice.destroy";

//    /** 广告通知 **/
//    String NOTIFY_MESSAGE_CHANGE = "com.axecom.smartweight.my.helper.notify.message";
//    String BACKGROUND_CHANGE = "com.axecom.smartweight.ui.activity.setting.background.change";

    String IMAGE_STATE = "screenImageState";

    String IS_HAS_DEVICE = "isHaveDevice"; // 是否已经有了硬件信息


    String RADIO_VERSION_ST = "";// 固件版本  商通称
    String RADIO_VERSION_SX_7 = "";// 固件版本  香山 7寸屏
    String RADIO_VERSION_SX_15 = "";// 固件版本  香山  15.6寸屏

    int VOLLEY_FLAG_FPMSLOGIN = 9;//计量院登陆接口
    int VOLLEY_FLAG_FPMS_SUBMIT = 10;//计量院登陆接口
    int VOLLEY_FLAG_FPMS_STATE = 11;//计量院心跳接口
    int VOLLEY_FLAG_FPMS_CHECK = 12;//计量院标定
    int VOLLEY_FLAG_AXE_TRACRE_NO = 13;//获取批次号

    String FPMS_LOGINTIME = "logintime";// 登陆时间
    String FPMS_EXPIRETIME = "expiretime";// 失效时间
    String FPMS_THIRDKEY = "thirdKey";   //第三方秘钥
    String FPMS_AUTHENCODE = "authenCode";  // 授权码
    String FPMS_DATAKEY = "datakey";  // 数据钥匙

    String MAIN_KEY = "F7AD4703F4520AFDB0216339";

    // 心跳休眠时间
    int UPDATE_STATE_TIME = 120000;
    String VALUE_K_WEIGHT = "ValueK";// 称重的K值
    String VALUE_SB_AD= "sbAd";// 标定Ad
    String VALUE_SB_ZERO_AD= "sbZeroAd";// 标定0位Ad

}

