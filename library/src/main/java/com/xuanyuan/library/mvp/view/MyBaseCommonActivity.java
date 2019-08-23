package com.xuanyuan.library.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:10
 * 邮件：424533553@qq.com
 * 说明：基础公共Activity,有一些共有的性能方法
 */
public class MyBaseCommonActivity extends Activity implements IBaseView {
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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


}
