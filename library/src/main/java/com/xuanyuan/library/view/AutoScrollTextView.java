package com.xuanyuan.library.view;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 单行文字走马灯效果
 * <p>
 * //启动公告滚动条   如果想改变跑马灯的文字内容或者文字效果，则在调用完setText方法之后，
 * 需要再调用一下init(WindowManager windowManager)方法，重新进行初始化和相关参数的计算。
 * AutoScrollTextView   autoScrollTextView = (AutoScrollTextView)findViewById(R.id.TextViewNotic);
 * autoScrollTextView.setText("rfasfasfafatgdfsgdsgdfsgdfsgdshsdfgdsghdfgjnfal房间卡减肥啦设计费类似飞机拉时间按国家啊发顺丰IJda");
 * autoScrollTextView.init(getWindowManager());
 * autoScrollTextView.startScroll();
 *
 *
 * author: luofaxin
 * date： 2018/9/27 0027.
 * email:424533553@qq.com
 * describe:
 */
@SuppressLint("AppCompatCustomView")
public class AutoScrollTextView extends TextView implements OnClickListener {
    public final static String TAG = AutoScrollTextView.class.getSimpleName();

    private float textLength = 0f;//文本长度
    private float step = 0f;//文字的横坐标
    private float y = 0f;//文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;//用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;//用于计算的临时变量
    public boolean isStarting;//是否开始滚动
    private Paint paint = null;//绘图样式
    private String text = "";//文本内容


    public AutoScrollTextView(Context context) {
        super(context);
        initView();
        isStarting = false;
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        isStarting = false;
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
        isStarting = false;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setOnClickListener(this);
    }

    /**
     * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
     */
    public void init(WindowManager windowManager) {
        paint = getPaint();
        text = getText().toString();
        textLength = paint.measureText(text);
        float viewWidth = getWidth();
        if (viewWidth == 0) {
            if (windowManager != null) {
                //该种方法过时了，以下两种方法都可用。
//                Display display = windowManager.getDefaultDisplay();
//                viewWidth =getWidth();

                Display display =windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                viewWidth = size.x;

//                DisplayMetrics metrics = new DisplayMetrics();
//                windowManager.getDefaultDisplay().getMetrics(metrics);
//                viewWidth = metrics.widthPixels;


            }
        }
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        y = getTextSize() + getPaddingTop();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        boolean isStarting = false;
        float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[]{isStarting});
            out.writeFloat(step);
        }


        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(null);
            step = in.readFloat();
        }
    }

    /**
     * 开始滚动
     */
    private void startScroll() {
        isStarting = true;
        invalidate();
    }

    /**
     * 停止滚动
     */
    private void stopScroll() {
        isStarting = false;
        invalidate();
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }
        step += 0.5;
        if (step > temp_view_plus_two_text_length)
            step = textLength;
        invalidate();

    }

    @Override
    public void onClick(View v) {
        if (isStarting)
            stopScroll();
        else
            startScroll();

    }

}