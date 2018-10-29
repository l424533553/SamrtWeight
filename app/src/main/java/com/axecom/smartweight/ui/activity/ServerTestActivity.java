package com.axecom.smartweight.ui.activity;

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
import com.luofx.listener.VolleyListener;
import com.luofx.utils.net.NetWorkJudge;
import com.shangtongyin.tools.serialport.IConstants_ST;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018-5-29.
 */

public class ServerTestActivity extends Activity implements IConstants_ST, View.OnClickListener,VolleyListener {

    private Button cancelBtn;
    private TextView titleTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_test_activity);
        setInitView();
    }


    public void setInitView() {
        cancelBtn = findViewById(R.id.server_test_cancel_btn);
        titleTv = findViewById(R.id.server_test_title_tv);
        cancelBtn.setOnClickListener(this);
        testConnection(null);


    }

    public void testConnection(String fsdf) {
        if(NetWorkJudge.isNetworkAvailable(this)){
            String url=BASE_IP_ST+"/api/smartsz/onlinecheck";
            SysApplication sysApplication= (SysApplication) getApplication();
            sysApplication.volleyGet(url,this,1);
        }else {
            titleTv.setText("连接失败");
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
        switch (v.getId()) {
            case R.id.server_test_cancel_btn:
                finish();
                break;
        }
    }

/*
    public void testConnection() {
        RetrofitFactory.getInstance().API()
                .testConnection()
                .compose(this.<BaseEntity>setThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        titleTv.setText(baseEntity.getMsg());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
*/

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
