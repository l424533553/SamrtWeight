//package com.axecom.smartweight.carouselservice1.config;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.support.v7.widget.AppCompatTextView;
//import android.util.AttributeSet;
//
///**
// * author: luofaxin
// * date： 2018/11/13 0013.
// * email:424533553@qq.com
// * describe:  自定义的跑马灯
// */
//public class MyText extends AppCompatTextView {
//
//
//    public MyText(Context context) {
//        super(context);
//    }
//
//    public MyText(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public MyText(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//
//    @Override
//    public boolean isFocused() {
//        return true;
//    }
//
//    /*
//     * Window与Window间焦点发生改变时的回调
//     * */
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        if (hasWindowFocus)
//            super.onWindowFocusChanged(true);
//    }
//
//    /*
//     * 用于EditText抢注焦点的问题
//     * */
//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        if (focused) {
//            super.onFocusChanged(true, direction, previouslyFocusedRect);
//        }
//    }
//
//}
