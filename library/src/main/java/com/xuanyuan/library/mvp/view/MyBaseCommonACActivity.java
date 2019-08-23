package com.xuanyuan.library.mvp.view;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.xuanyuan.library.help.ActivityController;

import static com.xuanyuan.library.config.IConfig.EVENT_BUS_COMMON;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:10
 * 邮件：424533553@qq.com
 * 说明：基础公共Activity,有一些共有的性能方法
 */
public abstract class MyBaseCommonACActivity extends AppCompatActivity implements IBaseView {
    protected Context context;

    /**
     * 观察者实例
     */
    protected Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            onLiveEvent(s);
        }
    };

    /**
     * LiveDataBus   事件总线
     *
     * @param type 事件类型
     */
    protected abstract void onLiveEvent(String type);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //观察者注册,订阅消息  ，不需要主动取消订阅
        LiveEventBus.get().with(EVENT_BUS_COMMON, String.class).observeForever(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveEventBus.get().with(EVENT_BUS_COMMON, String.class).removeObserver(observer);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public Context getMyContext() {
        return context;
    }

    @Override
    public void showToast(String msg) {

    }

    /**
     * 跳转 Activity
     *
     * @param cls      跳转的类
     * @param isFinish 是否结束当前Activity，  true:是
     */
    public void jumpActivity(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }



    /**
     * 记录点击时间
     */
    private long recordClickTime;
    /**
     * 是否关闭所有的activity
     */
    public boolean finishAll() {
        // 2s 内
        if (System.currentTimeMillis() - recordClickTime > 2000) {
            recordClickTime = System.currentTimeMillis();
            return false;
        } else {
            ActivityController.finishAll(true);
            return true;
        }
    }

}
