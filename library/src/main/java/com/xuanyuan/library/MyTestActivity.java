package com.xuanyuan.library;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.xuanyuan.library.base.BaseAppCompatActivity;

public class MyTestActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        findViewById(R.id.btnStartJobService).setOnClickListener(this);
    }

    @Override
    public void cancelScreenOn() {

    }

    @Override
    public void setTitel() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

    }
}
