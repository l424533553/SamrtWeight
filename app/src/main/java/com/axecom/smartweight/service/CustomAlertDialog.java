package com.axecom.smartweight.service;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.entity.project.AdResultBean;

/**
 * 自定义的消息通知 弹出框
 * 弹出市场的广告通知
 */
public class CustomAlertDialog extends Dialog implements View.OnClickListener {


//    private Context context;
//    private String title;
//    private String message;
//    private String strPositive;
//    private String strNegative;

    //    private int requestCode;
    //    private OnDialogButtonClickListener listener;

    private final AdResultBean.DataBean dataBean;

    public CustomAlertDialog(Context context, AdResultBean.DataBean dataBean) {
//                             OnDialogButtonClickListener listener) {
        super(context, R.style.dialog);
//        this.context = context;
        this.dataBean = dataBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admessage);
        setCancelable(false);
//        setCancelable(false);//设置点击对话框外部和按返回键都不可以取消
//        setCanceledOnTouchOutside(false);//设置点击对话框外部是否可以取消，默认是不可以取消（但是点返回键可以取消）

        TextView tvAdDate = findViewById(R.id.tvAdDate);
        TextView tvAdMarketName = findViewById(R.id.tvAdMarketName);
        TextView tvAdContent = findViewById(R.id.tvAdContent);
        TextView tvAdTitle = findViewById(R.id.tvAdTitle);
        if (dataBean != null) {
            tvAdDate.setText(dataBean.getVdate());
            tvAdMarketName.setText(dataBean.getMarketname());
            tvAdContent.setText(dataBean.getContent());
            tvAdTitle.setText(dataBean.getTitle());
        }

        findViewById(R.id.btnAdSubmit).setOnClickListener(this);
//        findViewById(R.id.btnAdCancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        view.getId();//取消按钮
//                listener.onDialogButtonClick(1, false);
        dismiss();
    }

}

