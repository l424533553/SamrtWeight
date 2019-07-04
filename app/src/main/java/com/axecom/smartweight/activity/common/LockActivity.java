package com.axecom.smartweight.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.axecom.smartweight.R;
import com.axecom.smartweight.activity.setting.SystemLoginActivity;
import com.axecom.smartweight.config.IConstants;
import com.axecom.smartweight.entity.system.BaseBusEvent;
import com.xuanyuan.library.MyPreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.axecom.smartweight.config.IEventBus.WEIGHT_CLEAN_CHEAT;

public class LockActivity extends AppCompatActivity implements IConstants {
    private Context context;
    private boolean cheatFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        context = this;
        MyPreferenceUtils.getSp(context).edit().putBoolean(LOCK_STATE, true).apply();
        cheatFlag = getIntent().getBooleanExtra(INTENT_CHEATFLAG, false);
        if (cheatFlag) {
            findViewById(R.id.tvNoteTitle).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tvNoteTitle).setVisibility(View.GONE);
        }

        findViewById(R.id.ivLoginSys).setOnClickListener(v -> {
            Intent intent2 = new Intent(context, SystemLoginActivity.class);
            startActivityForResult(intent2, RC_SYS_SET);

        });

        close(this);
        EventBus.getDefault().register(this);//解除事件总线问题
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_SYS_SET) {
                Intent intent = new Intent(this, SystemSettingsActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseBusEvent event) {
        // 解锁软件
        if (ACTION_UNLOCK_SOFT.equals(event.getEventType())) {
            if (!cheatFlag) {
                MyPreferenceUtils.getSp(context).edit().putBoolean(LOCK_STATE, false).apply();
                LockActivity.this.finish();
            }
        } else if (WEIGHT_CLEAN_CHEAT.equals(event.getEventType())) {
            String data= (String) event.getOther();
            if(data.contains("R0")){
                //去除标志成功
                MyPreferenceUtils.getSp(context).edit().putBoolean(INTENT_CHEATFLAG, false).apply();
                LockActivity.this.finish();
            }
        }
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

//    private class NetBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //如果相等的话就说明网络状态发生了变化
//            if (ACTION_UNLOCK_SOFT.equals(intent.getAction())) {
//                sp.edit().putBoolean(LOCK_STATE, false).apply();
//                LockActivity.this.finish();
//            }
//        }
//    }

//    private NetBroadcastReceiver recevier;
//    private IntentFilter intentFilter;
//    private void initNetReceiver() {
//        recevier = new NetBroadcastReceiver();
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(ACTION_UNLOCK_SOFT);
//    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (recevier != null) {
//            unregisterReceiver(recevier);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
