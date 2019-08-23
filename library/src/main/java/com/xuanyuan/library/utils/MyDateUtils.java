package com.xuanyuan.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 说明：
 * 作者：User_luo on 2018/5/8 14:58
 * 邮箱：424533553@qq.com
 */
public class MyDateUtils {
    public final static String YY_TO_mm = "yyyy-MM-dd HH:mm:ss";//检测数据日期时间格式
    public final static String YY_MM_DD = "yyyy-MM-dd";//检测数据日期时间格式
    public final static String YY_MM = "yyyy-MM";//检测数据日期时间格式
    public final static String mm_ss = "mm:ss";//检测数据日期时间格式
    public final static String YY_TO_ss = "yyyy-MM-dd HH:mm:ss";//精确到秒的日期时间格式
    private static final SimpleDateFormat yyTossFormat = new SimpleDateFormat(YY_TO_ss, Locale.CHINA);
    private static final SimpleDateFormat HHFormat = new SimpleDateFormat("HH", Locale.CHINA);
    private static final SimpleDateFormat DDFormat = new SimpleDateFormat("dd", Locale.CHINA);
    private static final SimpleDateFormat mmssFormat = new SimpleDateFormat(mm_ss, Locale.CHINA);
    private static final SimpleDateFormat sampleNoFormat = new SimpleDateFormat("MMddyyyyHHmmss", Locale.CHINA);
    private static final SimpleDateFormat yymmddFormat = new SimpleDateFormat(YY_MM_DD, Locale.CHINA);
    private static final SimpleDateFormat yymmFormat = new SimpleDateFormat(YY_MM, Locale.CHINA);

    public static SimpleDateFormat getYymmddFormat() {
        return yymmddFormat;
    }

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

    //获取系统当前日期时间
    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    //获取系统当前日期(英文格式)
    public static String getCurrentDateEnglish() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    //获取系统当前是星期几
    public static String getCurrentWeekDay(int type) {
        String week = "";
        Calendar c1 = Calendar.getInstance();
        int day = c1.get(Calendar.DAY_OF_WEEK);
        if (type == 2) {
            switch (day) {
                case 1:
                    week = "Sunday";
                    break;
                case 2:
                    week = "Monday";
                    break;
                case 3:
                    week = "Tuesdays";
                    break;
                case 4:
                    week = "Wednesday";
                    break;
                case 5:
                    week = "Thursday";
                    break;
                case 6:
                    week = "Fridays";
                    break;
                case 7:
                    week = "Saturday";
                    break;


            }
        } else {
            switch (day) {
                case 1:
                    week = "星期日";
                    break;
                case 2:
                    week = "星期一";
                    break;
                case 3:
                    week = "星期二";
                    break;
                case 4:
                    week = "星期三";
                    break;
                case 5:
                    week = "星期四";
                    break;
                case 6:
                    week = "星期五";
                    break;
                case 7:
                    week = "星期六";
                    break;
            }
        }
        return week;
    }


    public static String getYY_TO_ss(long date) {
        return yyTossFormat.format(date);
    }

    public static String getY2D(long date) {
        return yymmddFormat.format(date);
    }

    public static String getSampleNo(Date date) {
        return sampleNoFormat.format(date);
    }

    public static String getSampleNo() {
        return sampleNoFormat.format(System.currentTimeMillis());
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

    /**
     * @param time 选择的时间
     * @return 时间是否在系统今日之前
     */
    private boolean isTodayBefore(long time) {
        try {
            String todayString = MyDateUtils.getYYMMDD(System.currentTimeMillis());
            Date todayDate = MyDateUtils.getYymmddFormat().parse(todayString);
            //今日的凌晨时间
            long todayTime = todayDate.getTime();
            if (time < todayTime) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void getTodayString() {
        String todayString = MyDateUtils.getYYMMDD(System.currentTimeMillis());

    }

    //判断闰年
    private boolean isLeap(int year) {
        return ((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0);
    }

    //返回当月天数
    public int getDays(int year, int month) {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month) {
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

    /**
     * 生成12位的出厂编号
     *
     * @param tid 秤id
     * @return 2019101 是编号前缀定死的
     */
    public static String getDeviceNo(int tid) {
        String data = "00000" + tid;
        return 2019101 + data.substring(data.length() - 5);
    }
}
