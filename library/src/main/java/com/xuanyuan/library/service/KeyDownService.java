package com.xuanyuan.library.service;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * 作者：罗发新
 * 时间：2019/7/4 0004    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class KeyDownService extends AccessibilityService {
    private static final String TAG = "RRR";
    int flag = 0;

    @Override
    protected synchronized boolean onKeyEvent(KeyEvent event) {
        int key = event.getKeyCode();
        Log.i(TAG, "onKeyEvent=="+key);
        switch (key) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.i(TAG, "KEYCODE_VOLUME_DOWN");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.i(TAG, "KEYCODE_VOLUME_UP");
                break;
            case 131:
                boolean isFirst=isForeground(this, "BetaActivity");
                Log.i(TAG, "返回=="+isFirst);
                break;

        }
        return super.onKeyEvent(event);
    }

    public  boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }





    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String pkgName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;

        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "RRR::onCreate");
        super.onCreate();
    }
}


