package com.axecom.smartweight.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.axecom.smartweight.R;
import com.axecom.smartweight.bean.SubOrderBean;
import com.axecom.smartweight.bean.SubOrderReqBean;
import com.axecom.smartweight.net.RetrofitFactory;
import com.axecom.smartweight.ui.uiutils.UIUtils;
import com.axecom.smartweight.ui.uiutils.ViewUtils;
import com.axecom.smartweight.utils.LogUtils;
import com.axecom.smartweight.utils.SPUtils;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Longer on 2016/10/26.
 */
public  class BaseDialog {

    private SweetAlertDialog mSweetAlertDialog;


    protected SysApplication sysApplication;

    private Context context;

    public BaseDialog(Context context) {
        this.context = context;
    }

    PopupWindow mPopupWindow;


    public void showLoading(String titleText, int type) {
        if(mSweetAlertDialog!=null){
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading() {
        if(mSweetAlertDialog!=null){
            if(mSweetAlertDialog.isShowing()){
                mSweetAlertDialog.dismiss();
            }
            mSweetAlertDialog=null;
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText,String confirmText) {
        if(mSweetAlertDialog!=null){
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setConfirmText(confirmText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText,String confirmText,long times) {

        showLoading(titleText,confirmText);
        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoading();
            }
        }, times);
    }

        public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
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








}
