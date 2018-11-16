package com.axecom.smartweight.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.axecom.smartweight.R;
import com.shangtongyin.tools.serialport.IConstants_ST;

public class LockActivity extends AppCompatActivity implements IConstants_ST {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        sp.edit().putBoolean(LOCK_STATE, true).apply();
        initNetReceiver();
        close(this);

    }


    /**
     * 关闭 底部状态栏
     *
     * @param activity 需要关闭的activity
     */
    private void close(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

    private class NetBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果相等的话就说明网络状态发生了变化
            if (ACTION_UNLOCK_SOFT.equals(intent.getAction())) {
                sp.edit().putBoolean(LOCK_STATE, false).apply();
                LockActivity.this.finish();
            }
        }
    }

    private NetBroadcastReceiver recevier;
    private IntentFilter intentFilter;

    private void initNetReceiver() {
        recevier = new NetBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UNLOCK_SOFT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        registerReceiver(recevier, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recevier != null) {
            unregisterReceiver(recevier);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
