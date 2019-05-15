package com.luofx.mvp.base.presenter;
import android.support.annotation.NonNull;

import com.luofx.mvp.base.view.IBaseXView;

import java.lang.ref.WeakReference;
/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:58
 * 邮件：424533553@qq.com
 * 说明：
 */
public class BaseXPresenter<V extends IBaseXView> implements IBaseXPresenter {
    // 防止 Activity 不走 onDestory() 方法，所以采用弱引用来防止内存泄漏
    private WeakReference<V> mViewRef;

    public BaseXPresenter(@NonNull V view) {
        attachView(view);
    }

    private void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    public V getView() {
        return mViewRef.get();
    }

    /**
     * @return 判断是否 还有连接
     */
    @Override
    public boolean isViewAttach() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }





}

