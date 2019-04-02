package com.axecom.smartweight.ui.uiutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.axecom.smartweight.base.SysApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;


/**
 * Created by Longer on 2016/10/26.
 */
public class UIUtils {
    /**
     * 得到Resource对象
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    /**
     * 得到主线程的一个handler
     */
    public static Handler getMainThreadHandler() {
        return SysApplication.getHandler();
    }


    /**
     * 得到应用程序的包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取屏幕宽度:
    public static int getScreenWidthPixels(Activity activity) {
        //获取屏幕的宽高的像素
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕宽度:
    public static int getScreenHighPixels(Activity activity) {
        //获取屏幕的高的像素
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    //获取栈顶的activity
    public static String getTopActivity(Activity context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return (runningTaskInfos.get(0).topActivity).getClassName();
        else
            return null;
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Paint paint = new Paint();
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= 1000) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static float formatNumber(String currencyCode, float num) {
        BigDecimal bd = new BigDecimal(num);
        if (currencyCode.contains("KRW")) {
            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        } else if (currencyCode.contains("TWD")) {
            bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        } else if (currencyCode.contains("CNY")) {
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return bd.floatValue();
    }


    private static String getNumberFormat(float price, NumberFormat nf, boolean rounding) {
        String formatprice = "";
        if (rounding)
            nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(true);
        formatprice = nf.format(price);

        return formatprice;
    }

    private static String hinComma(String formatprice) {
        return formatprice.replaceAll(",", "");
    }

    private static synchronized Properties getPro(Context context, InputStreamReader in) {
        if (pro != null) {
            return pro;
        }
        try {
            in = new InputStreamReader(context.getAssets().open("currency.properties"));
            pro = new Properties();
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pro;
    }

    private static Properties pro;

    public static void fixScrollEditText(ScrollView scrollView) {
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }
}
