package com.luofx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 说明：
 * 作者：User_luo on 2018/5/8 14:58
 * 邮箱：424533553@qq.com
 */
public class DateUtils {
    public final static String YY_TO_mm = "yyyy-MM-dd HH:mm:ss";//检测数据日期时间格式
    public final static String YY_MM_DD = "yyyy-MM-dd";//检测数据日期时间格式
    public final static String YY_MM = "yyyy-MM";//检测数据日期时间格式
    public final static String mm_ss = "mm:ss";//检测数据日期时间格式
    public final static String YY_TO_ss = "yyyy-MM-dd HH:mm:ss";//精确到秒的日期时间格式
    private static SimpleDateFormat yyTossFormat = new SimpleDateFormat(YY_TO_ss, Locale.CHINA);
    private static SimpleDateFormat HHFormat = new SimpleDateFormat("HH", Locale.CHINA);
    private static SimpleDateFormat DDFormat = new SimpleDateFormat("dd", Locale.CHINA);
    private static SimpleDateFormat mmssFormat = new SimpleDateFormat(mm_ss, Locale.CHINA);
    private static SimpleDateFormat sampleNoFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    private static SimpleDateFormat yymmddFormat = new SimpleDateFormat(YY_MM_DD, Locale.CHINA);
    private static SimpleDateFormat yymmFormat = new SimpleDateFormat(YY_MM, Locale.CHINA);

    public static String getYY_TO_ss(Date date) {
        return yyTossFormat.format(date);
    }
    public static String getHH(Date date) {
        return HHFormat.format(date);
    }
    public static String getDD(Date date) {
        return DDFormat.format(date);
    }

    public static long getYY_TO_ss(String dateString) {
        try {
            Date date = yyTossFormat.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getYY_TO_ss(long date) {
        return yyTossFormat.format(date);
    }

    public static String getSampleNo(Date date) {
        return sampleNoFormat.format(date) + (int) (Math.random() * 9 + 1);
    }

    public static String getSampleNo() {
        return sampleNoFormat.format(new Date());
    }

    public static String getYYMMDD(Date date) {
        return yymmddFormat.format(date);
    }
    public static String getYYMM(Date date) {
        return yymmFormat.format(date);
    }

    public static String getYYMMDD(Long date) {
        return yymmddFormat.format(date);
    }

    public static String getmm_ss(int date) {
        return mmssFormat.format(new Date(date * 1000));
    }



    //判断闰年
   private boolean isLeap(int year)
    {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }

    //返回当月天数
   public int getDays(int year, int month)
    {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

}
