package com.axecom.smartweight.mvp.base.presenter;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:33
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IBasePresenter extends IBaseXPresenter {
    /**
     * 取消网络请求
     *
     * @param tag 网络请求标记
     */
    void cancel(Object tag);

    /**
     * 取消所有的网络请求
     */
    void cancelAll();

}




