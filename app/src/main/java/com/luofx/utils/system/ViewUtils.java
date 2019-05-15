package com.luofx.utils.system;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

/**
 * author: luofaxin
 * date： 2018/11/5 0005.
 * email:424533553@qq.com
 * describe:
 */
public class ViewUtils {

    /**
     * 关闭 底部状态栏
     *
     * @param activity 需要关闭的activity
     */
    private void close(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
    }

    /**
     * 关闭 导航栏 ，点击输入框时导航栏会 显示出来
     */
    private void close2(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);
    }


    /**
     * 暂时隐藏 底部导航栏
     */
    public static void setNavigationBar(Activity activity, int visible) {
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible) {
            decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

}
