package com.axecom.smartweight.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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
    //    private boolean cheatFlag;
    private TextView tvNoteTitle;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        time = System.currentTimeMillis();
        Log.i("123456", "onCreate=" + time);
        context = this;
        tvNoteTitle = findViewById(R.id.tvNoteTitle);

        findViewById(R.id.ivLoginSys).setOnClickListener(v -> {
            Intent intent2 = new Intent(context, SystemLoginActivity.class);
            intent2.putExtra(JUMP_FROM_LOCK, true);
            startActivity(intent2);
        });

        close(this);
        EventBus.getDefault().register(this);//解除事件总线问题
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除事件总线问题
        Log.i("123456", "onDestroy=" + time);
    }

    //定义处理接收的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(BaseBusEvent event) {
        // 解锁软件
        if (ACTION_UNLOCK_SOFT.equals(event.getEventType())) {
            ;
            if (!MyPreferenceUtils.getSp(context).getBoolean(INTENT_CHEATFLAG, false)) {
                LockActivity.this.finish();
            }
        } else if (WEIGHT_CLEAN_CHEAT.equals(event.getEventType())) {
            String data = (String) event.getOther();
            if (data.contains("R0")) {
                //去除标志成功
                MyPreferenceUtils.getSp(context).edit().putBoolean(INTENT_CHEATFLAG, false).apply();
                tvNoteTitle.setVisibility(View.GONE);
                if (!MyPreferenceUtils.getSp(context).getBoolean(LOCK_STATE, false)) {
                    LockActivity.this.finish();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("123456", "onStart=" + time);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isCheat = MyPreferenceUtils.getSp(context).getBoolean(INTENT_CHEATFLAG, false);
        if (isCheat) {
            tvNoteTitle.setVisibility(View.VISIBLE);
        } else {
            tvNoteTitle.setVisibility(View.GONE);
        }

        Log.i("123456", "onResume=" + time);
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

}
