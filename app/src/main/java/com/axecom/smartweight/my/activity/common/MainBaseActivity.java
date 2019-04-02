package com.axecom.smartweight.my.activity.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.mvp.IMainContract;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.entity.OrderBean;
import com.axecom.smartweight.ui.uiutils.UIUtils;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.luofx.utils.MyPreferenceUtils;
import com.xuanyuan.library.mvp.view.MyBaseCommonActivity;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.axecom.smartweight.utils.CommonUtils.parseFloat;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:
 */
public abstract class MainBaseActivity extends MyBaseCommonActivity implements IConstants, IMainContract.IView {

    protected OrderBean orderBeanTemp = new OrderBean();
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

        sbZeroAd = MyPreferenceUtils.getString(context, VALUE_SB_ZERO_AD, null);
        sbAd = MyPreferenceUtils.getString(context, VALUE_SB_AD, null);
        kValue = MyPreferenceUtils.getString(context, VALUE_K_WEIGHT, null);

//        0250000 2107365 0.0383055
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sysApplication = (SysApplication) getApplication();
        initNetReceiver();
        initBaseData();
    }

    @Override
    public SysApplication getMyAppliaction() {
        return sysApplication;
    }


    /**
     * 广播，数据结构
     */
    private NetBroadcastReceiver recevier;

    private void initNetReceiver() {
        recevier = new NetBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        intentFilter.addAction("com.axecom.smartweight.ui.activity.setting.background.change");
//        intentFilter.addAction(NOTIFY_MESSAGE_CHANGE);
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(recevier, intentFilter);
    }

    protected abstract void onBroadCastReceive(Context context, Intent intent);


    private class NetBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果相等的话就说明网络状态发生了变化
            onBroadCastReceive(context, intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recevier != null) {
            unregisterReceiver(recevier);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
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
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {//TODO: handle exception
        }
        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }

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

    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    public void setEditText(EditText editText, int position, String text, int type) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText().toString() + text);
        }
    }


    public String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public String getCurrentTime(String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
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
     * 网络  图片  holderView
     */
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            imageView.setImageResource(R.drawable.logo);
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(data, imageView);
            Glide.with(context).load(data).into(imageView);
        }
    }

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
