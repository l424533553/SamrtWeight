package com.axecom.smartweight.my.activity.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.mvp.IMainView;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.ui.uiutils.UIUtils;
import com.xuanyuan.library.MyPreferenceUtils;
import com.xuanyuan.library.mvp.view.MyBaseCommonACActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.axecom.smartweight.utils.CommonUtils.parseFloat;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:
 */
public abstract class MainBaseACActivity extends MyBaseCommonACActivity implements IConstants, IMainView {

    protected final OrderBean orderBeanTemp = new OrderBean();
    protected SysApplication sysApplication;
    protected Handler handler;
//    protected HttpHelper helper;

    protected float priceLarge;
    protected float priceMiddle;
    protected float priceSmall;

    protected int zeroAd;//0位ad
    protected int currentAd;// 当前Ad
    protected int currentWeight;// 当前重量


    protected int tareWeight;
    //    0：正常，1：作弊
    protected int cheatSign;//作弊标志
    //    0：正，1：负
    protected int isNegative;//是否负数
    //    0：正常，1：超载
    protected int isOver;//是否超重
    //    0：非零，1：零位
    protected int isZero;//是否零位
    //    0：非去皮，1：去皮
    protected int isPeeled;//是否去皮
    //    0：不稳定，1：稳定
    protected int isStable;//是否稳定


    //k 值
    protected String kValue;
    //标定Ad
    protected String sbAd;
    //标定零位Ad
    protected String sbZeroAd;
    protected boolean isCanAdd = true;//是否直接点击现金 进入累计菜单

    private void initBaseData() {
        priceLarge = MyPreferenceUtils.getSp(context).getFloat(PRICE_LARGE, 0.3f);
        priceMiddle = MyPreferenceUtils.getSp(context).getFloat(PRICE_MIDDLE, 0.2f);
        priceSmall = MyPreferenceUtils.getSp(context).getFloat(PRICE_SMALL, 0.1f);
//        0250000 2107365 0.0383055
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sysApplication = (SysApplication) getApplication();
        initBaseData();
    }

    @Override
    public SysApplication getMyAppliaction() {
        return sysApplication;
    }


    /**
     * 验证金额是否有效
     *
     * @return 物品金额有效
     */
    protected boolean priceValid(EditText editText) {
        return parseFloat(editText.getText().toString()) > 0.00f || parseFloat(editText.getHint().toString()) > 0.00f;
    }

    public void disableShowInput(final EditText editText) {
//        Class<EditText> cls = EditText.class;
//        Method method;
//        try {
//            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
//            method.setAccessible(true);
//            method.invoke(editText, false);
//        } catch (Exception e) {//TODO: handle exception
//        }
//        try {
//            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
//            method.setAccessible(true);
//            method.invoke(editText, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setSelection(editText.length());
                setGrandTotalTxt();
            }
        });
    }

    public abstract void setGrandTotalTxt();

    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text, int type) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText().toString() + text);
        }
    }


    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);// HH:mm:ss
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (type == 1)
            c.add(Calendar.MONTH, -1);
        if (type == 2)
            c.add(Calendar.MONTH, +1);
        if (type == 3)
            c.add(Calendar.DAY_OF_MONTH, -1);
        if (type == 4)
            c.add(Calendar.DAY_OF_MONTH, +1);
        Date m = c.getTime();
        return simpleDateFormat.format(m);
    }


    private SweetAlertDialog mSweetAlertDialog;

    public void showLoading(String titleText, int type) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    /**
     * 顯示进度框
     */
    public void showLoading() {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText, String confirmText) {
        if (mSweetAlertDialog != null) {
            if (mSweetAlertDialog.isShowing()) {
                mSweetAlertDialog.dismiss();
            }
            mSweetAlertDialog = null;
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setConfirmText(confirmText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String title, String confirmText, long times) {
        String titleText;
        if ("0".equals(title)) {
            titleText = "支付成功";
        } else {
            titleText = "待支付";
        }

        showLoading(titleText, confirmText);
        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoading();
            }
        }, times);
    }

    public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void closeLoading() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
            mSweetAlertDialog.dismissWithAnimation();
        }
    }

//    private void initData();
    // 测试功能


    /**
     * 跳转Activity的方法,传入我们需要的参数即可
     */
    public <T> void startDDMActivity(Class<T> activity, boolean isAinmain) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }
}
