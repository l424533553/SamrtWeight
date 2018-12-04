package com.axecom.smartweight.my.service;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.axecom.smartweight.R;
import com.axecom.smartweight.my.entity.AdResultBean;

public class CustomAlertDialog extends Dialog implements View.OnClickListener {

//    public interface OnDialogButtonClickListener {
//        /**
//         * 点击按钮的回调方法
//         *
//         * @param requestCode
//         * @param isPositive
//         */
//        void onDialogButtonClick(int requestCode, boolean isPositive);
//    }

    private Context context;
    private String title;
    private String message;
    private String strPositive;
    private String strNegative;
    //    private int requestCode;
    //    private OnDialogButtonClickListener listener;

    private AdResultBean.DataBean dataBean;

    public CustomAlertDialog(Context context, AdResultBean.DataBean dataBean) {
//                             OnDialogButtonClickListener listener) {
        super(context, R.style.Dialog123);
        this.context = context;
        this.dataBean = dataBean;

//        this.message = message;
//        this.requestCode = requestCode;
//        this.listener = listener;
    }

//    private TextView tvTitle;
//    private TextView tvMessage;

    private TextView tvAdDate, tvAdMarketName, tvAdContent, tvAdTitle;


//    private Button btnPositive;
//    private Button btnNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_admessage);
        setCancelable(false);
//        setCancelable(false);//设置点击对话框外部和按返回键都不可以取消
//        setCanceledOnTouchOutside(false);//设置点击对话框外部是否可以取消，默认是不可以取消（但是点返回键可以取消）

        tvAdDate = findViewById(R.id.tvAdDate);
        tvAdMarketName = findViewById(R.id.tvAdMarketName);
        tvAdContent = findViewById(R.id.tvAdContent);
        tvAdTitle = findViewById(R.id.tvAdTitle);
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
        switch (view.getId()) {
//            case R.id.btnAdCancle:
//                //确定按钮
//                listener.onDialogButtonClick(0, true);
//                break;
            case R.id.btnAdSubmit:
                //取消按钮
//                listener.onDialogButtonClick(1, false);
                break;
        }
        dismiss();
    }

}

