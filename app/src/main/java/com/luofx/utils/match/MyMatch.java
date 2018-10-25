package com.luofx.utils.match;

import java.text.DecimalFormat;

/**
 * author: luofaxin
 * date： 2018/10/25 0025.
 * email:424533553@qq.com
 * describe:
 */
public class MyMatch {

    /**
     * 精确到小数点两位
     */
    public static String accurate2(float scale) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(scale);
    }

    public static String accurate3(float scale) {
        DecimalFormat fnum = new DecimalFormat("##0.000");
        return fnum.format(scale);
    }
}
