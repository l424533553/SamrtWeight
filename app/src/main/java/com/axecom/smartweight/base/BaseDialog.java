package com.axecom.smartweight.base;

import android.content.Context;
import android.graphics.Color;

import com.axecom.smartweight.ui.uiutils.UIUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Longer on 2016/10/26.
 */
public class BaseDialog {

    private SweetAlertDialog mSweetAlertDialog;
    protected SysApplication sysApplication; //
    private final Context context;  // 数据功能

    public BaseDialog(Context context) {
        this.context = context;
    }

    public void showLoading(String titleText, int type) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading() {
        if (mSweetAlertDialog != null) {
            if (mSweetAlertDialog.isShowing()) {
                mSweetAlertDialog.dismiss();
            }
            mSweetAlertDialog = null;
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading(String titleText, String confirmText) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setConfirmText(confirmText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }


    public void showLoading(String titleText, String confirmText, long times) {
        showLoading(titleText, confirmText);
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


