package com.xuanyuan.library.base;

/**
 * 作者：罗发新
 * 时间：2019/4/25 0025    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public interface IBaseActivity {

    /**
     * 取消屏幕常亮标识
     */
    void cancelScreenOn();

    /**
     * 如果有标题栏，请设置标题，并设置返回键功能按钮  doBack
     */
    void setTitel();


}
