package com.shangtongyin.tools.serialport;

/**
 * author: luofaxin
 * date： 2018/8/31 0031.
 * email:424533553@qq.com
 * describe:
 */
public interface IConstants_ST {

    //     0:默认值没有选着打印机
    int PrintMode_NORMAL = 0;
    //      1"佳博2120TF"
    int PrintMode_GP = 1;

    //       2 "美团打印机"
    int PrintMode_MT = 2;

    //        3 "商通打印机"
    int PrintMode_ST = 3;

    //      4 "香山打印机
    int PrintMode_XS = 4;


    /**
     * 称重更新了
     */
    int NOTIFY_WEIGHT = 9111;
    int NOTIFY_MARQUEE = 9222;
    int NOTIFY_CLEAR = 9333;
    int NOTIFY_INITDAT = 9444;
    int NOTIFY_SUCCESS = 9555;
    int NOTIFY_EPAY_SUCCESS = 9666; //电子支付成功


    String BASE_IP_ST = "http://119.23.43.64";


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

    String KEY = "key";
    String MCHID = "mchid";
    String BASE_WEB_IP = "base_web_ip";


    String KET_SWITCH_SIMPLE_OR_COMPLEX = "key_switch_simple_or_complex";

    String IS_RE_BOOT = "is_re_boot";

//    private int marketid;
//    private String marketname;
//    private String companyno;
//    private int tid;
//    private String seller;
//    private int sellerid;


}
