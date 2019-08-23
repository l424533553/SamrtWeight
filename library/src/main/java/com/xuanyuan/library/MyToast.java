package com.xuanyuan.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 说明：Toast 工具类
 * 作者：User_luo on 2018/5/7 17:04
 * 邮箱：424533553@qq.com
 */
public class MyToast {

    private static Toast toast;

    /**
     * show toast
     *
     * @param context context
     * @param msg     message string
     */
    @SuppressLint("ShowToast")
    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toastNetError(Context context) {
        Toast.makeText(context.getApplicationContext(), "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
    }

    public static void showError(Context context, String info) {

        Toast toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
//        LinearLayout layout = (LinearLayout) toast.getView();
//        layout.setBackgroundColor(Color.RED);
        TextView v = toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.RED);
        v.setTextSize(18);
        toast.setText(info);
        toast.show();
    }

    /**
     * @param coordinatorLayout 一般View为 coordinatorLayout
     */
    private void showSnackbar(View coordinatorLayout) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Action 被点击", Snackbar.LENGTH_SHORT);
        snackbar.setText("动态文本");//动态设置文本显示内容
        snackbar.setActionTextColor(Color.RED);//动态设置Action文本的颜色
        snackbar.setDuration(5000);//动态设置显示时间

        View snackbarView = snackbar.getView();//获取Snackbar显示的View对象
        //获取显示文本View,并设置其显示颜色
        ((TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.BLUE);
        //获取Action文本View，并设置其显示颜色
        ((TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLUE);
        //设置Snackbar的背景色
        snackbarView.setBackgroundColor(Color.GREEN);

        //设置Snackbar显示的位置
        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(params.width, params.height);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;//垂直居中
        snackbarView.setLayoutParams(layoutParams);

        snackbar.show();

    }
}
