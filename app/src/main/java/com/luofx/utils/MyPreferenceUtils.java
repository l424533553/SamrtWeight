package com.luofx.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 说明：SharedPreferences工具类
 * 作者：User_luo on 2018/6/13 09:33
 * 邮箱：424533553@qq.com
 */
public class MyPreferenceUtils {
    private static SharedPreferences mSp;
    private final static String SP_NAME = "config";

    /**
     * 获得sharePreference内存对象
     *
     * @param context  上下文内容
     */
    public static SharedPreferences getSp(Context context) {
        if (mSp == null) {
            mSp = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    /**
     * 获取boolean类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
       return getSp(context).getBoolean(key, defValue);
    }

    /**
     * 获取boolean类型的值,如果没有对应的值，默认值返回false
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }


    /**
     * 设置int类型的值
     */
    public static void setInt(Context context, String key, int value) {
        Editor editor = getSp(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }


    /**
     * 设置boolean类型的值
     */
    public static void setBoolean(Context context, String key, boolean value) {
        Editor editor = getSp(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取String类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static String getString(Context context, String key, String defValue) {
        return getSp(context).getString(key, defValue);
    }

    /**
     * 获取int类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static int getInt(Context context, String key, int defValue) {
        return getSp(context).getInt(key, defValue);
    }

    /**
     * 获取String类型的值,如果没有对应的值，默认值返回null
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * 设置String类型的值
     */
    public static void setString(Context context, String key, String value) {
        Editor editor =  getSp(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取long类型的值
     *
     * @param context  上下文
     * @param key      对应的键
     * @param defValue 如果没有对应的值，
     */
    public static long getLong(Context context, String key, long defValue) {
        return  getSp(context).getLong(key, defValue);
    }

    /**
     * 获取long类型的值,如果没有对应的值，默认值返回0
     *
     * @param context 上下文
     * @param key     对应的键
     */
    public static Long getLong(Context context, String key) {
        return getLong(context, key, 0);
    }

    /**
     * 设置Long类型的值
     */
    public static void setLong(Context context, String key, long value) {
        Editor editor = getSp(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 根据key值删除指定的数据
     *
     */
    public static void remove(Context context, String key) {
        Editor editor = getSp(context).edit();
        editor.remove(key);
        editor.apply();
    }
}
