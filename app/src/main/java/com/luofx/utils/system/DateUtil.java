package com.luofx.utils.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/7.
 */

public class DateUtil {
    /**
     * 获取当前时间(格式化)
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取当前时间毫秒值
     */
    public static String getTime() {
        return String.valueOf(System.currentTimeMillis());
    }
}