//package com.axecom.smartweight.mvp.base.presenter;
//
//import android.support.annotation.NonNull;
//
//import com.xuanyuan.library.mvp.view.IBaseView;
//
///**
// * 作者：罗发新
// * 时间：2018/12/21 0021    11:35
// * 邮件：424533553@qq.com
// * 说明：
// */
//public  class  BasePresenter<V extends IBaseView, T> extends BaseXPresenter<V> implements IBasePresenter, HttpResponseListener<T> {
//
//    public BasePresenter(@NonNull V view) {
//        super(view);
//    }
//
//    @Override
//    public void cancel(@NonNull Object tag) {
////        ......
//    }
//
//    @Override
//    public void cancelAll() {
////        ......
//    }
//
//    /**
//     * 来自于 HttpResponseListener
//     */
//    @Override
//    public void onSuccess(Object tag, T t) {
//
//    }
//
//    @Override
//    public void onFailure(Object tag, Object error) {
//
//    }
//
//}
//
//
