package com.axecom.smartweight.helper;

import android.text.TextUtils;

/**
 * 作者：罗发新
 * 时间：2019/7/4 0004    星期四
 * 邮件：424533553@qq.com
 * 说明： 秤工具
 */
public class TidUtils {
    // 称的类型  0为商通  ，1 为XS15 , 2 为8寸。  3 axe自研秤   4 深信秤
    private final static int TID_TYPE_ST = 0;
    private final static int TID_TYPE_XS15 = 1;
    private final static int TID_TYPE_XS8 = 2;
    private final static int TID_TYPE_AXE = 3;
    private final static int TID_TYPE_SX442 = 4;
    private final static int TID_TYPE_OTHER = -2;
    private final static int TID_TYPE_ERROR = -1;

    private final static String USER_ST = "android-build";
    private final static String USER_XS_15 = "liaokai";
    private final static String USER_XS_8 = "liuyegang";
    private final static String USER_AXB = "root";
    private final static String USER_SX_442 = "liubin";
    /**
     * OK 判定秤的种类
     *
     * @return 秤的种类类型
     */
    public static int decisionScaleType() {
        String user = android.os.Build.USER;
        String name = android.os.Build.DEVICE;
        if (TextUtils.isEmpty(user)) {
            return TID_TYPE_ERROR;
        } else if (USER_ST.equalsIgnoreCase(user)) {
            return TID_TYPE_ST;
        } else if (USER_XS_15.equalsIgnoreCase(user)) {
            return TID_TYPE_XS15;
        } else if (USER_XS_8.equalsIgnoreCase(user)) {
            return TID_TYPE_XS8;
        } else if (USER_AXB.equalsIgnoreCase(user)) {
            return TID_TYPE_AXE;
        } else if (USER_SX_442.equalsIgnoreCase(user)) {
            return TID_TYPE_SX442;
        }
        return TID_TYPE_OTHER;
    }
}
