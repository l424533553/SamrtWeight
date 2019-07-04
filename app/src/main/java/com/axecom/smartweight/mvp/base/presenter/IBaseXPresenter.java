package com.axecom.smartweight.mvp.base.presenter;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    10:48
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IBaseXPresenter {
    /**
     * 判断 presenter 是否与 view 建立联系，防止出现内存泄露状况
     *
     * @return {@code true}: 联系已建立<br>{@code false}: 联系已断开
     */
    boolean isViewAttach();

    /**
     * 断开 presenter 与 view 直接的联系
     */
    void detachView();
}
