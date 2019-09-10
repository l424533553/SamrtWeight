package com.xuanyuan.library.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 作者：罗发新
 * 时间：2019/9/4 0004    星期三
 * 邮件：424533553@qq.com
 * 说明：使用该 LayoutManager 后，
 * 可以解决recycler.smoothScrollToPosition(position) 让其滑动并置顶（不重置不会指定的）
 */

public class AdvertiseLinearLayoutManager extends LinearLayoutManager {
    public AdvertiseLinearLayoutManager(Context context) {
        super(context);
    }

    public AdvertiseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AdvertiseLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        AdvertiseLinearSmoothScroller linearSmoothScroller =
                new AdvertiseLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}

