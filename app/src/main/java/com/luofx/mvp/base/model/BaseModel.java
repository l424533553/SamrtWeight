package com.luofx.mvp.base.model;

import android.support.annotation.NonNull;

import com.luofx.mvp.base.presenter.HttpResponseListener;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:43
 * 邮件：424533553@qq.com
 * 说明：
 */
public class BaseModel {
    /**
     * 发送网络请求
     *
     * @param observable 被观察者
     * @param callback   回调
     * @param <T>        请求
     */
    protected <T> void sendRequest(@NonNull Observable<T> observable, HttpResponseListener<T> callback) {
//        ......
    }

    /**
     * 发送网络请求
     *
     * @param tag        请求标识符
     * @param observable 被观察者
     * @param callback   回调
     * @param <T>        实体类
     */
    private <T> void sendRequest(@NonNull Object tag, @NonNull Observable<T> observable, HttpResponseListener callback) {
//        ......
    }

    private void sendRequest(@NonNull Object tag, HttpResponseListener callback) {
//        ......
    }

    /**
     * 发送网络请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>        实体类
     */
    protected <T> void sendRequest(@NonNull Observable<T> observable, @NonNull Observer<T> observer) {
//       ......

    }

    /**
     * 发送网络请求
     *
     * @param tag        请求标记
     * @param observable 被观察者
     * @param observer   观察者
     */
    protected <T> void sendRequest(@NonNull Object tag, @NonNull Observable<T> observable, @NonNull Observer<T> observer) {
//       ......

    }


}
