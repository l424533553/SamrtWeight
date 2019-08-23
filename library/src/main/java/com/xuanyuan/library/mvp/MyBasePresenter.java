package com.xuanyuan.library.mvp;

/**
 * 作者：罗发新
 * 时间：2019/3/27 0027    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */
public class MyBasePresenter {

    private long timeLong;//记录返回点击时间

    /**
     * 记录时间，每操作超过5s方可进行对应操作
     */
    public boolean isClickAble() {
        // 2s 内
        if (System.currentTimeMillis() - timeLong > 5000L) {
            timeLong = System.currentTimeMillis();
            return true;
//            Toast.makeText(this, "再按一次返回退出！", Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
    }

}
