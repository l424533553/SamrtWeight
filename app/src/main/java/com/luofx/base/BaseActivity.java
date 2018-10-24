package com.luofx.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity implements IBaseConstants {

    /**
     * 返回点击  退出时间
     */
    protected long backTime;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 超过两秒 才能进行下一次点击操作
     */
    protected  boolean isClickAble(){
        if (System.currentTimeMillis() - backTime > BACK_TIME_DEFAULT) {
            backTime = System.currentTimeMillis();
            return true;
        }
        return  false;
    }




}
