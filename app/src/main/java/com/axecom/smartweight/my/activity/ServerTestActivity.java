package com.axecom.smartweight.my.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.my.config.IConstants;
import com.axecom.smartweight.my.helper.HttpHelper;
import com.luofx.listener.VolleyListener;
import com.luofx.utils.net.NetWorkJudge;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-5-29.
 */

public class ServerTestActivity extends Activity implements IConstants, View.OnClickListener, VolleyListener {
    private TextView titleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_test_activity);
        setInitView();
    }

    public void setInitView() {
        Button cancelBtn = findViewById(R.id.server_test_cancel_btn);
        titleTv = findViewById(R.id.server_test_title_tv);
        cancelBtn.setOnClickListener(this);
        testConnection();
    }

    public void testConnection() {
        if (NetWorkJudge.isNetworkAvailable(this)) {
            SysApplication sysApplication = (SysApplication) getApplication();
            HttpHelper.getmInstants(sysApplication).onLineTest(this, 1);
        } else {
            titleTv.setText("网络不可用");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.server_test_cancel_btn) {
            finish();
        }
    }

    @Override
    public void onResponse(VolleyError volleyError, int flag) {
        titleTv.setText("连接失败");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        titleTv.setText("连接成功");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
