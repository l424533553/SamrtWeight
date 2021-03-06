package com.axecom.smartweight.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.axecom.smartweight.R;
import com.axecom.smartweight.ui.uiutils.UIUtils;
import com.axecom.smartweight.ui.uiutils.ViewUtils;
import com.luofx.newclass.ActivityController;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Longer on 2016/10/26.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    //    private SweetAlertDialog mSweetAlertDialog;
    private View mMenuRoot;
    private SweetAlertDialog mSweetAlertDialog;

    protected BaseActivity() {
    }


    // 测试功能
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Intent bannerIntent = new Intent(this, BannerService.class);
//        startService(bannerIntent);
        if (Build.VERSION.SDK_INT > 19) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }
        //
        View view = setInitView();
        setContentView(view);
        //这里这一段会影响弹出的dialog型的Activity，故暂时注释掉
        //getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ViewUtils mViewUtils = new ViewUtils(this);
        //获取屏幕的宽高的像素
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        SysApplication.mWidthPixels = dm.widthPixels;
//        SysApplication.mHeightPixels = dm.heightPixels;
        ActivityController.addActivity(this);

    }

    public abstract void initView();


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param et 隐藏软键盘
     */
    public void Hidekeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
        if (isOpen) {
            ((InputMethodManager) et.getContext().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showSoftInput(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public void hideSoftInput() {
        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (m.isActive()) {
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param et 显示软键盘
     */
    public void Showkeyboard(EditText et) {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(et, 0);

    }


    public abstract View setInitView();

    private PopupWindow mPopupWindow;

    private void switchMenu() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this);
            //mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setContentView(mMenuRoot);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mMenuRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchMenu();
                }
            });

        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAsDropDown(null);
        }
    }

    public void showLoading(String titleText, int type) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    public void showLoading() {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    private void showLoading(String titleText, String confirmText) {
        if (mSweetAlertDialog != null) {
            mSweetAlertDialog.dismiss();
        }
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);
        mSweetAlertDialog.setConfirmText(confirmText);

        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }


    public void showLoading(String titleText, String confirmText, long times) {

        showLoading(titleText, confirmText);
        UIUtils.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoading();
            }
        }, times);
    }

    public void showLoading(String titleText) {
        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mSweetAlertDialog.setTitleText(titleText);


        mSweetAlertDialog.setCancelable(true);
        mSweetAlertDialog.show();
    }

    private void closeLoading() {
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing()) {
            mSweetAlertDialog.dismissWithAnimation();
        }
    }


    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setEditText(EditText editText, int position, String text, int type) {
        if (position == 9) {
            if (!TextUtils.isEmpty(editText.getText()))
                editText.setText(editText.getText().subSequence(0, editText.getText().length() - 1));
        } else {
            editText.setText(editText.getText() + text);
        }
    }


    public String getMonthTime(String specifiedDay, String format, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);// HH:mm:ss
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM", Locale.CHINA).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setTime(date);
        if (type == 1)
            c.add(Calendar.MONTH, -1);
        if (type == 2)
            c.add(Calendar.MONTH, +1);
        if (type == 3)
            c.add(Calendar.DAY_OF_MONTH, -1);
        if (type == 4)
            c.add(Calendar.DAY_OF_MONTH, +1);
        Date m = c.getTime();
        return simpleDateFormat.format(m);
    }

    public void scrollTo(final ListView listView, final int position) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPosition(position);
            }
        });
    }


    public void setmMenuRoot(View mMenuRoot) {
        this.mMenuRoot = mMenuRoot;
    }
}
