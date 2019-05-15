package com.axecom.smartweight.ui.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.axecom.smartweight.R;


public class WifiPwdDialog extends Dialog {
    public WifiPwdDialog(@NonNull Context context) {
        super(context);
    }

    public WifiPwdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public interface OnConfirmedListener {
        void onConfirmed(String result);
    }

    public static class Builder implements View.OnClickListener {
        private static final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};

        private View contentView;
        private final WifiPwdDialog wifiPwdDialog;
        private final View view;
        private final EditText inputEv;
        private final Button confirmBtn;
        private final Button cancelBtn;
        private OnConfirmedListener onConfrimedListener;

        @SuppressLint("InflateParams")
        public Builder(Context context) {
            wifiPwdDialog = new WifiPwdDialog(context, R.style.dialog);
            view = LayoutInflater.from(context).inflate(R.layout.wifi_pwd_layout, null);
            wifiPwdDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            confirmBtn = view.findViewById(R.id.wifi_pwd_confirm_btn);
            cancelBtn = view.findViewById(R.id.wifi_pwd_cancel_btn);
            inputEv = view.findViewById(R.id.wifi_pwd_et);


            confirmBtn.setOnClickListener(this);
            cancelBtn.setOnClickListener(this);
        }

        public WifiPwdDialog create(OnConfirmedListener onConfrimedListener) {
            this.onConfrimedListener = onConfrimedListener;
            wifiPwdDialog.setContentView(view);
            wifiPwdDialog.setCancelable(true);     //用户可以点击手机Back键取消对话框显示
            wifiPwdDialog.setCanceledOnTouchOutside(false);        //用户不能通过点击对话框之外的地方取消对话框显示

            return wifiPwdDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wifi_pwd_confirm_btn:
                    if (onConfrimedListener != null) {
                        onConfrimedListener.onConfirmed(inputEv.getText().toString());
                        wifiPwdDialog.dismiss();
                    }
                    break;
                case R.id.wifi_pwd_cancel_btn:
                    wifiPwdDialog.dismiss();
                    break;
            }
        }

    }
}
