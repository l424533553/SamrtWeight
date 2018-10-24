package com.luofx.help;


import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: luofaxin
 * date： 2018/10/19 0019.
 * email:424533553@qq.com
 * describe:   金额 过滤器
 * InputFilter[] filters={new CashierInputFilter()};
 * refundMoneyEdt.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
 * <p>
 * <p>
 * android:inputType="numberDecimal"
 * //或者
 * setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
 */
public class CashierInputFilter implements InputFilter {

    Pattern mPattern;

    //输入的最大金额
    private static final int MAX_VALUE = 1000;
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;

    private static final String POINTER = ".";

    private static final String ZERO = "0";

    public CashierInputFilter() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    /**
     * @param source    新输入的字符串
     * @param start     新输入的字符串起始下标，一般为0
     * @param end       新输入的字符串终点下标，一般为source长度-1
     * @param dest      输入之前文本框内容
     * @param dstart    原内容起始坐标，一般为0
     * @param dend      原内容终点坐标，一般为dest长度-1
     * @return          输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {


        String sourceText = source.toString();
        String destText11 = dest.toString();



        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }

        Matcher matcher = mPattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if(sourceText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source.toString())) {  //只能输入一个小数点
                    return "";
                }
            }

            //验证小数点精度，保证小数点后只能输入两位
            int index = sourceText.indexOf(POINTER);
            int length = sourceText.length() - index-1;

            if (length > POINTER_LENGTH) {
                return sourceText.subSequence(0, sourceText.length()-1);
            }
        } else {
            /**
             * 没有输入小数点的情况下，只能输入小数点和数字
             * 1. 首位不能输入小数点
             * 2. 如果首位输入0，则接下来只能输入小数点了
             */
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source.toString())) && TextUtils.isEmpty(sourceText)) {  //首位不能输入小数点
                    return "";
                } else if (sourceText.startsWith(ZERO)) { //如果首位输入0，接下来只能输入小数点
                    if(sourceText.length()>1){
                        if('0'==sourceText.charAt(sourceText.length()-1)){
                            return sourceText.subSequence(0, sourceText.length()-1);
                        }
                    }
//                } else if (!POINTER.equals(source.toString()) && sourceText.startsWith(ZERO)) { //如果首位输入0，接下来只能输入小数点

                }
            }
        }

        //验证输入金额的大小
        double sumText = Double.parseDouble(sourceText);
        if (sumText > MAX_VALUE) {
            return sourceText.subSequence(0, sourceText.length()-1);
        }

        return  source;
    }
}

