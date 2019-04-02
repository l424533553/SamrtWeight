package com.luofx.mvp.base.view;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:50
 * 邮件：424533553@qq.com
 * 说明：基类的实现层
 */

import android.app.Activity;

import com.luofx.mvp.base.presenter.IBaseXPresenter;

public abstract class BaseXActivity<P extends IBaseXPresenter> extends Activity implements IBaseXView {
    /**
     * 控制器
     */
    private P mPresenter;

    /**
     * 创建 Presenter
     */
    public abstract P onBindPresenter();

    /**
     * 获取 Presenter 对象，在需要获取时才创建`Presenter`，起到懒加载作用
     */
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    @Override
    public Activity getSelfActivity() {
        return this;
    }

    /**
     * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}

