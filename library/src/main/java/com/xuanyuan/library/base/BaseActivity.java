package com.xuanyuan.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.xuanyuan.library.MyPreferenceUtils;

import static com.xuanyuan.library.config.IConfig.SP_FLAG_BOOLEAN_SCREEN_STATE;

/**
 * 作者：罗发新
 * 时间：2019/4/24 0024    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */
public abstract class BaseActivity extends Activity implements IBaseActivity{

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        boolean screenState = MyPreferenceUtils.getBoolean(context, SP_FLAG_BOOLEAN_SCREEN_STATE, false);
        if (screenState) {
            // 保持屏幕长亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else {//擦除屏幕常亮标识
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }



    /**
     * 设置返回操作
     */
    public abstract void doBack(View view);

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
