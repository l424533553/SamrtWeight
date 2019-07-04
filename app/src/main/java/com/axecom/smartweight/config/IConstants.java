package com.axecom.smartweight.config;

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
    String BASE_IP_WEB = "https://data.axebao.com/smartsz";

    /**
     * 香山秤  的称重模块消息通知
     */
    int WEIGHT_NOTIFY_XS15 = 2115;// 香山秤15.6屏的通知
    int WEIGHT_NOTIFY_XS8 = 2114;// 香山秤15.6屏的通知


    int NOTIFY_AD_PICTURE_FORMAT = 2043;//商通的称重通知

    int PAYMENT_TIMEOUT = 180000;//等待支付超时时间

    int RC_SYS_SET = 55001;//请求系统设着

    String PRICE_LARGE = "largePrice";//大袋子价格
    String PRICE_MIDDLE = "middlePrice";
    String PRICE_SMALL = "smallPrice";
    String ZERO_WEIGHT = "zeroWeight";

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
//    String NOTIFY_MESSAGE_CHANGE = "com.axecom.smartweight.helper.notify.message";
//    String BACKGROUND_CHANGE = "com.axecom.smartweight.activity.setting.background.change";

    String IMAGE_STATE = "screenImageState";

    String IS_HAS_DEVICE = "isHaveDevice"; // 是否已经有了硬件信息


    String RADIO_VERSION_ST = "";// 固件版本  商通称
    String RADIO_VERSION_SX_7 = "";// 固件版本  香山 7寸屏
    String RADIO_VERSION_SX_15 = "";// 固件版本  香山  15.6寸屏

    int VOLLEY_FLAG_FPMSLOGIN_RESET = 8;//计量院登陆接口 ，失效后重新获得的
    int VOLLEY_FLAG_FPMSLOGIN = 9;//计量院登陆接口
    int VOLLEY_FLAG_FPMS_SUBMIT = 10;//计量院登陆接口
    int VOLLEY_FLAG_FPMS_STATE = 11;//计量院心跳接口
    int VOLLEY_FLAG_FPMS_CHECK = 12;//计量院标定
    int VOLLEY_FLAG_AXE_TRACRE_NO = 13;//获取批次号

    int VOLLEY_FLAG_AXE_BD = 14;//秤标定后传输数据给 安鑫宝

    String FPMS_LOGINTIME = "logintime";// 登陆时间
    String FPMS_EXPIRETIME = "expiretime";// 失效时间
    String FPMS_THIRDKEY = "thirdKey";   //第三方秘钥
    String FPMS_AUTHENCODE = "authenCode";  // 授权码
    String FPMS_DATAKEY = "datakey";  // 数据钥匙

    String MAIN_KEY = "F7AD4703F4520AFDB0216339";


    String VALUE_K_WEIGHT = "ValueK";// 称重的K值
    String VALUE_K_DEFAULT = "0.0";// 称重的K值
    String VALUE_SB_AD = "sbAd";// 标定Ad
    String VALUE_SB_ZERO_AD = "sbZeroAd";// 标定0位Ad
    String INTENT_COMMON_UPDATE = "commonUpdate";// true ;则商户没变只是一般更新  ，false:商户变换了

    //是否自动更新   0:首次接入自动更新   1.更新按键进入，可以支持单项更新    2.普通更新必须选项
    String INTENT_AUTO_UPDATE = "autoUpdate";

    String MAX_LEV = "30kg";// 最大量程

    Float MINIMUN_VOLUME = 1130f;// 香山电池

    String SP_IS_FIRST_INIT = "isFirstInit";// 是否第一次更新

    int CODE_JUMP2_DATAFLUSH = 6101;// 跳转进入数据更新界面
    int CODE_JUMP2_SCALEBD = 7101;// 秤的标定

    int FLAG_GET_USER_INFO = 8101;// 跳转进入数据更新界面
    //请求更新产品类型表
    int HANDLER_UPDATE_GOOD_TYPE = 8201;
    int HANDLER_UPDATE_ALLGOOD = 8203;
    // 获取小程序
    int HANDLER_SMALL_ROUTINE = 8204;
    int HANDLER_SECOND_IMAGE = 8206;
    //更新完成
    int HANDLER_UPDATE_FINISH = 8205;
    int HANDLER_IMAGE_FINISH = 8207;


    /*** 8寸按键常量  Start    ************************************************************************/
    String MENU_1 = "menu0";
    String MENU_2 = "menu1";
    String MENU_3 = "menu2";
    String MENU_4 = "menu3";
    String MENU_5 = "menu4";
    String MENU_6 = "menu5";
    String MENU_7 = "menu6";
    String MENU_8 = "menu7";
    String MENU_9 = "menu8";
    String MENU_10 = "menu9";

    String MENU_11 = "menu10";
    String MENU_12 = "menu11";
    String MENU_13 = "menu12";
    String MENU_14 = "menu13";
    String MENU_15 = "menu14";
    String MENU_16 = "menu15";
    String MENU_17 = "menu16";
    String MENU_18 = "menu17";
    String MENU_19 = "menu18";
    String MENU_20 = "menu19";
    String MENU_21 = "menu20";
    String MENU_22 = "menu21";
    String MENU_23 = "menu22";
    String MENU_24 = "menu23";
    String MENU_25 = "menu24";
    String MENU_26 = "menu25";
    String MENU_27 = "menu26";
    String MENU_28 = "menu27";
    String MENU_29 = "menu28";
    String MENU_30 = "menu29";
    String MENU_31 = "menu30";
    String MENU_32 = "menu31";
    String MENU_33 = "menu32";
    String MENU_34 = "menu33";

    String NUM_1 = "num0";
    String NUM_2 = "num1";
    String NUM_3 = "num2";
    String NUM_4 = "num3";
    String NUM_5 = "num4";
    String NUM_6 = "num5";
    String NUM_7 = "num6";
    String NUM_8 = "num7";
    String NUM_9 = "num8";
    String NUM_DELETE = "num9";
    String NUM_0 = "num10";
    String NUM_POT = "num11";
    String MENU_SPACE_LEFT = "spaceLeft";
    String MENU_SPACE_RIGHT = "spaceRight";

    String FUNC_TARE = "funcTare";
    String FUNC_ZERO = "funcZero";
    String FUNC_SET = "funcSet";
    String FUNC_BACK = "funcBack";

    String FUNC_CARSH = "funcCarsh";
    String FUNC_QR = "funcQR";
    String FUNC_ADD = "funcAdd";
    String FUNC_CLEAR = "funcClear";
    // 删除按键
    String FUNC_DELETE = "funcDelete";

    /**
     * 8寸按键常量  end
     *************************************************************************/
    String ACTIVITY_JUMP_TYPE = "jumpType";  // 跳转类型

    /**
     * 参数 配置区    心跳时间
     **********************************************************************/
    int UPDATE_STATE_TIME = 300 * 1000;
    String SMALLROUTINE_URL = "SmallRoutineURL";

    int POSITION_PATCH = 1;
    int POSITION_REPORTS = 2;
    int POSITION_SERVER = 3;
    int POSITION_INVALID = 4;
    int POSITION_ABNORMAL = 5;

    int POSITION_COMMODITY = 6;
    int POSITION_UPDATE = 7;
    int POSITION_HOT = 15;
    int POSITION_RE_CONNECTING = 8;
    int POSITION_WIFI = 9;
    int POSITION_LOCAL = 10;

    int POSITION_WEIGHT = 11;
    int POSITION_RE_BOOT = 12;
    int POSITION_HELP = 17;
    int POSITION_DATA_DELETE = 18;
    int POSITION_RE_EXIT= 19;//关闭软件
    int POSITION_BACK = 16;
    int POSITION_BD = 13;
    int POSITION_SYSTEM = 14;


    //消息类型  网路可用
    String EVENT_NET_WORK_AVAILABLE = "eventNetWorkAble";
    String EVENT_TOKEN_REGET = "eventTokenReget";

    String PRINTER_TYPE = "printer_type";
    int PRINTER_HHD = 0;
    int PRINTER_E39 = 1;

    String INTENT_CHEATFLAG = "cheatFlag";

    String LIVE_EVENT_NOTIFY_GOOD_TRADE = "dataNotifyGoodTrade";
    /**
     * 获取商品列表清单信息  后  通知更新
     */
    String LIVE_EVENT_NOTIFY_HOT_GOOD = "dataNotifyHotGood";

    int LOAD_DATA_TYPE_TWO = 402;
    int TEXT_TIME_FROM = 301;
    int TEXT_TIME_TO = 302;

    /**
     * fms 的数据量
     */
    String FPS_COUNT_OK = "fms_count_OK";
    String FPS_COUNT_ERR = "fms_count_ERR";
    String AXE_COUNT_OK = "axe_count_OK";
    String AXE_COUNT_ERR = "axe_count_ERR";
}

