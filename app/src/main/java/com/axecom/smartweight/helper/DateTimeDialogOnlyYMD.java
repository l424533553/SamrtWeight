package com.axecom.smartweight.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.axecom.smartweight.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义 日期 选择 器  只能选择 日月年
 * Created by Administrator on 2016/6/22.
 */
public class DateTimeDialogOnlyYMD extends AlertDialog implements DatePicker.OnDateChangedListener, View.OnClickListener {

    private DatePicker mDatePicker;
    private MyOnDateSetListener mCallBack;

    private Button cancleButton, okButton;

    // 控制 日期
    private int width;
    // 是否 显示 日选择器   true 显示 ，false 隐藏
    private boolean isDayVisible = true;
    // 是否 显示 月选择器   true 显示 ，false 隐藏
    private boolean isMonthVisible = true;

    // 是否 显示 年选择器   true 显示 ，false 隐藏
    private boolean isYearVisible = true;


    protected DateTimeDialogOnlyYMD(Context context) {
        super(context);
    }

    protected DateTimeDialogOnlyYMD(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface MyOnDateSetListener {
        void onDateSet(Date date);
    }

    public DateTimeDialogOnlyYMD(Context context, MyOnDateSetListener callBack, boolean isDayVisible, boolean isMonthVisible, boolean isYearVisible) {
        super(context);
        this.isDayVisible = isDayVisible;
        this.isMonthVisible = isMonthVisible;
        this.isYearVisible = isYearVisible;
        this.mCallBack = callBack;
        this.isDayVisible = isDayVisible;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_date_picker_dialog, null);
        setView(view);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        LinearLayout buttonGroup = (LinearLayout) view.findViewById(R.id.buttonGroup);
        cancleButton = (Button) view.findViewById(R.id.cancelButton);
        okButton = (Button) view.findViewById(R.id.okButton);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mDatePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        cancleButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        // 是否 显示 年
        if (!this.isYearVisible) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
        }
        // 是否 显示 月
        if (!this.isMonthVisible) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
        }

        // 是否 显示 日
        if (!this.isDayVisible) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }


        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);
        buttonGroup.measure(width, height);
        mDatePicker.measure(width, height);
        if (buttonGroup.getMeasuredWidth() > mDatePicker.getMeasuredWidth()) {
            this.width = buttonGroup.getMeasuredWidth();
        } else {
            this.width = mDatePicker.getMeasuredWidth();
        }
        Log.i("testss", this.width + "measuredWidth");
    }

    /**
     * @param context
     * @param callBack
     * @param isDayVisible 是否 隐藏 日 选择  true 显示， false 隐藏
     */
    public DateTimeDialogOnlyYMD(Context context, MyOnDateSetListener callBack, boolean isDayVisible) {
        this(context, callBack, isDayVisible, true, true);
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (view.getId() == R.id.datePicker)
            mDatePicker.init(year, monthOfYear, dayOfMonth, this);
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker.clearFocus();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mDatePicker.getYear());
            calendar.set(Calendar.MONTH, mDatePicker.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
            calendar.getTime();
            mCallBack.onDateSet(calendar.getTime());
            Log.i("testss", mDatePicker.getYear() + "====" + (mDatePicker.getMonth() + 1) + "==" + mDatePicker.getDayOfMonth());
        }
    }

    public void hideOrShow() {
        if (this == null) {
            return;
        }
        if (!this.isShowing()) {
            this.show();
            // 设置 显示 的 宽高
            WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
            if (isDayVisible) {
                attributes.width = this.width + 100;
            } else {
                attributes.width = this.width / 2 * 3;
            }
            this.getWindow().setAttributes(attributes);
        } else {
            this.dismiss();
        }
    }


    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("year", mDatePicker.getYear());
        state.putInt("month", mDatePicker.getMonth());
        state.putInt("day", mDatePicker.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt("year");
        int start_month = savedInstanceState.getInt("month");
        int start_day = savedInstanceState.getInt("day");
        mDatePicker.init(start_year, start_month, start_day, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.okButton:
                tryNotifyDateSet();
                dismiss();
                break;
        }

    }


}
