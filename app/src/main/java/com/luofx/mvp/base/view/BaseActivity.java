package com.luofx.mvp.base.view;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:12
 * 邮件：424533553@qq.com
 * 说明：
 */

import android.app.ProgressDialog;

import com.luofx.mvp.base.presenter.IBaseXPresenter;
import com.xuanyuan.library.mvp.view.IBaseView;

public abstract class BaseActivity<P extends IBaseXPresenter> extends BaseXActivity<P> implements IBaseView {
    // 加载进度框
    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading() {
//        ......
    }

    @Override
    public void hideLoading() {
//        ......
    }

    @Override
    public void showToast(String msg) {
//        ......
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}

