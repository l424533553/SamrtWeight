package com.axecom.smartweight.mvp.base.presenter;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:41
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface HttpResponseListener<T> {
    /**
     * 网络请求成功
     *
     * @param tag 请求的标记
     * @param t   返回的数据
     */
    void onSuccess(Object tag, T t);

    /**
     * 网络请求失败
     *
     * @param tag   请求的标记
     * @param error 请求失败时，返回的信息类
     */
    void onFailure(Object tag, Object error);
}