package com.axecom.smartweight.mvp;

/**
 * 作者：罗发新
 * 时间：2019/4/12 0012    星期五
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IMainView extends IMainContract.IView {

    /**
     * 选择菜品
     *
     * @param position 菜品菜单列表索引
     */
    void selectOrderBean(int position);

    /**
     * 小键盘操作
     *
     * @param position 数字小键盘索引
     */
    void pressDigital(int position);


}
