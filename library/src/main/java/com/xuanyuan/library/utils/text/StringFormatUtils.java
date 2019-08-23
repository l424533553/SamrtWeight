package com.xuanyuan.library.utils.text;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：罗发新
 * 时间：2019/6/6 0006    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class StringFormatUtils {


    /**
     *  判断 网络 url地址格式是否正确
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     *
     * @return   检查邮件地址的格式
     */
    public static boolean checkEmail(String email) {
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
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
