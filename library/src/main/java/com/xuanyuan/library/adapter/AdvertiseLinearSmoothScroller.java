package com.xuanyuan.library.adapter;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;

/**
 * 作者：罗发新
 * 时间：2019/9/4 0004    星期三
 * 邮件：424533553@qq.com
 * 说明：
 */
public class AdvertiseLinearSmoothScroller extends LinearSmoothScroller {

        AdvertiseLinearSmoothScroller(Context context) {
            super(context);
        }
        /**
         *
         * @param viewStart RecyclerView的top位置
         * @param viewEnd RecyclerView的Bottom位置
         * @param boxStart item的top位置
         * @param boxEnd  item的bottom位置
         * @param snapPreference 滑动方向的识别
         * @return  返回的就是我们item置顶需要的偏移量
         */
        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return boxStart-viewStart;
        }

        /**
         * 此方法返回滚动每1px需要的时间,可以用来控制滚动速度
         * 即如果返回2ms，则每滚动1000px，需要2秒钟
         * @param displayMetrics  像素
         * @return  返回的像素点
         */
        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return super.calculateSpeedPerPixel(displayMetrics);
        }
}


