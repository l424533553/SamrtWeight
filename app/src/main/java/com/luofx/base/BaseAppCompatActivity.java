package com.luofx.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.axecom.smartweight.R;
import com.axecom.smartweight.base.BusEvent;
import com.luofx.newclass.ActivityController;
import com.xuanyuan.library.MyLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@SuppressLint("Registered")
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseConstants {

    //是否 需要注册事件总线
    protected boolean isNeedRgEventBus;

    /**
     * true Or false 是否需要注册事件总线机制
     * 1.需要在 子类 的  super.onCreate(savedInstanceState); 之前调用
     * 2.需要写 有      @Subscribe 关键字的 事件接收处理方法，否则报错异常
     * void onEventMain(BusEvent event);
     * 3.設置 this.isNeedRgEventBus=  true or  false
     */
    protected abstract void setNeedRgEventBus();

    protected abstract void onEventMainBack(BusEvent event);

    @Subscribe
    public void onEventMain(BusEvent event) {
        onEventMainBack(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入到 Activity管理中
        ActivityController.addActivity(this);
        setNeedRgEventBus();
        if (isNeedRgEventBus) {
            MyLog.blue("开启注册");
            EventBus.getDefault().register(this);
        } else {
            MyLog.blue("未开启注册");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isNeedRgEventBus) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 返回点击  退出时间
     */
    protected long backTime;

    /**
     * 是否可以点击
     * 超过两秒 才能进行下一次点击操作
     */
    protected boolean isClickAble() {
        if (System.currentTimeMillis() - backTime > BACK_TIME_DEFAULT) {
            backTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * Activity 的带平滑动画 跳转
     */
    protected void startActivityWith(Intent intent, boolean isAinmain) {
        startActivity(intent);
        //是否需要开启动画(目前只有这种x轴平移动画,后续可以添加):
        if (isAinmain) {
            this.overridePendingTransition(R.anim.activity_translate_x_in, R.anim.activity_translate_x_out);
        }
    }
}







