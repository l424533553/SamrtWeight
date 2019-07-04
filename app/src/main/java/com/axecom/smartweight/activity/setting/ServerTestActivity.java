package com.axecom.smartweight.activity.setting;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.volley.VolleyError;
import com.axecom.smartweight.R;
import com.axecom.smartweight.base.SysApplication;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.databinding.ServerTestActivityBinding;
import com.axecom.smartweight.helper.HttpHelper;
import com.xuanyuan.library.listener.VolleyListener;
import com.xuanyuan.library.utils.net.MyNetWorkUtils;

import org.json.JSONObject;

/**
 * VM
 * 测试服务 是否连通的Activity
 */
public class ServerTestActivity extends Activity implements IConstants, View.OnClickListener, VolleyListener {
    private ServerTestActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.server_test_activity);
        binding.setOnClickListener(this);
        testConnection();

    }

    public void testConnection() {
        if (MyNetWorkUtils.isNetworkAvailable(this)) {
            SysApplication sysApplication = (SysApplication) getApplication();
            HttpHelper.getmInstants(sysApplication).onLineTest(this, 1);
        } else {
            binding.serverTestTitleTv.setText("网络不可用");
            new Handler().postDelayed(this::finish, 1000);
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
        binding.serverTestTitleTv.setText("连接失败");
        new Handler().postDelayed(this::finish, 1000);
    }

    @Override
    public void onResponse(JSONObject jsonObject, int flag) {
        binding.serverTestTitleTv.setText("连接成功");
        new Handler().postDelayed(this::finish, 1000);
    }
}
