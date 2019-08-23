package com.xuanyuan.library.mvp.view;

import android.content.Context;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:10
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IBaseView {

    /**
     * 显示正在加载 view
     */
    void showLoading();

    /**
     * 关闭正在加载 view
     */
    void hideLoading();

    /**
     * @return  获取上下文对象
     */
    Context getMyContext();

    /**
     * 显示提示
     */
    void showToast(String msg);

    /**
     * 跳转Activity
     *
     * @param cls      跳转目的 Activity
     * @param isFinish 是否结束当前Activityl
     */
    void jumpActivity(Class<?> cls, boolean isFinish);
}
