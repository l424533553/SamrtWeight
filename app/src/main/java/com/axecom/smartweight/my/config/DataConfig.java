package com.axecom.smartweight.my.config;

/**
 * 作者：罗发新
 * 时间：2019/4/11 0011    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class DataConfig implements IConstants {


    //   除皮27  置零26   设置25   返回33
    //    //   现金17  扫码9    累计41   清除49


    public static String getKey(String key) {
        switch (key) {
            case "040":
                return MENU_1;
            case "032":
                return MENU_2;
            case "031":
                return MENU_3;
            case "030":
                return MENU_4;

            case "024":
                return MENU_5;
            case "023":
                return MENU_6;
            case "039":
                return MENU_7;
            case "038":
                return MENU_8;
            case "037":
                return MENU_9;
            case "028":
                return MENU_10;

            case "016":
                return MENU_11;
            case "015":
                return MENU_12;
            case "014":
                return MENU_13;
            case "022":
                return MENU_14;
            case "021":
                return MENU_15;
            case "013":
                return MENU_16;
            case "008":
                return MENU_17;
            case "007":
                return MENU_18;
            case "006":
                return MENU_19;
            case "046":
                return MENU_20;
            case "045":
                return MENU_21;
            case "005":
                return MENU_22;

            case "048":
                return MENU_23;
            case "047":
                return MENU_24;
            case "055":
                return MENU_25;
            case "054":
                return MENU_26;
            case "061":
                return MENU_27;
            case "053":
                return MENU_28;
            case "056":
                return MENU_29;
            case "064":
                return MENU_30;
            case "063":
                return MENU_31;
            case "062":
                return MENU_32;
            case "060":
                return MENU_33;
            case "052":
                return MENU_34;


            case "003":
                return NUM_1;
            case "043":
                return NUM_2;
            case "002":
                return NUM_3;
            case "019":
                return NUM_4;
            case "011":
                return NUM_5;
            case "010":
                return NUM_6;
            case "035":
                return NUM_7;
            case "034":
                return NUM_8;
            case "018":
                return NUM_9;
            case "057":
                return NUM_DELETE;
            case "051":
                return NUM_0;
            case "050":
                return NUM_POT;


            case "027":
                return FUNC_TARE;
            case "026":
                return FUNC_ZERO;
            case "025":
                return FUNC_SET;
            case "033":
                return FUNC_BACK;
            case "017":
                return FUNC_CARSH;
            case "009":
                return FUNC_QR;
            case "041":
                return FUNC_ADD;
            case "049":
                return FUNC_CLEAR;
            case "042":
                return FUNC_DELETE;
            default:
                return null;
        }
    }
}


//   除皮27  置零26   设置25   返回33
//    //   现金17  扫码9    累计41   清除49







