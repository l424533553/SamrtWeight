package com.axecom.smartweight.mvp.base.view;


import com.axecom.smartweight.mvp.base.presenter.IBaseXPresenter;
import com.xuanyuan.library.mvp.view.IBaseView;
/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    11:12
 * 邮件：424533553@qq.com
 * 说明：
 */
public abstract class BaseActivity<P extends IBaseXPresenter> extends BaseXActivity<P> implements IBaseView {
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

