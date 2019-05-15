package com.luofx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.axecom.smartweight.R;

import java.text.DecimalFormat;

/**
 * author: luofaxin
 * date： 2018/11/1 0001.
 * email:424533553@qq.com
 * describe:
 * 圆盘计步图表  ,可以自定义要显示的内容  ，操作和正常的控件一样
 * 使用   color_progress_view.update(stepCount, 1000)  可以正常更新
 *
 */
public class CircleProgressBar extends View {

    private final RectF mWheelRect = new RectF();
    private Paint mDefaultWheelPaint;
    private Paint mFinishWheelPaint;
    private Paint mCenterWheelPaint;
    private Paint mTitlePaint, mStepPaint, mTargetPaint;
    private float mSweepAnglePer;
    private float mPercent;
    private int mStepNum, mCurrStepNum;
    private BarAnimation mAnim;
    private int mMaxStepNum;// 默认最大步数
    private float mStepY;
    private float mTargetY;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("#.0");// 格式为保留小数点后一位
    public static String GOAL_STEP;
    public static String PERCENT;
    private final Context context;

    public CircleProgressBar(Context context) {
        super(context);
        init(null, 0);
        this.context = context;
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
        this.context = context;
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        this.context = context;
    }

    private void init(AttributeSet attrs, int defStyle) {
        mFinishWheelPaint = new Paint();
        mFinishWheelPaint.setColor(Color.rgb(100, 113, 205));
        mFinishWheelPaint.setStyle(Paint.Style.STROKE);// 空心
        mFinishWheelPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
        mFinishWheelPaint.setAntiAlias(true);// 去锯齿

        mCenterWheelPaint = new Paint();
        mCenterWheelPaint.setColor(Color.rgb(243, 243, 243));
        mCenterWheelPaint.setStyle(Paint.Style.STROKE);
        mCenterWheelPaint.setStrokeCap(Paint.Cap.ROUND);
        mCenterWheelPaint.setAntiAlias(true);

        mDefaultWheelPaint = new Paint();
        mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
        mDefaultWheelPaint.setAntiAlias(true);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setColor(Color.WHITE);

        mStepPaint = new Paint();
        mStepPaint.setAntiAlias(true);
        mStepPaint.setColor(Color.WHITE);

        mTargetPaint = new Paint();
        mTargetPaint.setAntiAlias(true);
        mTargetPaint.setColor(Color.WHITE);
        mAnim = new BarAnimation();
    }

    private SweepGradient sweepGradient;

    /**
     * @param canvas 画笔
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mWheelRect, 0, 359, false, mDefaultWheelPaint);
        canvas.drawArc(mWheelRect, 0, 359, false, mCenterWheelPaint);
        canvas.drawArc(mWheelRect, 90, mSweepAnglePer, false, mFinishWheelPaint);
//		canvas.drawText("步数", mWheelRect.centerX() - (mTitlePaint.measureText("步数") / 2), mTitleY,
//				mTitlePaint);
        canvas.drawText(mCurrStepNum + "",
                mWheelRect.centerX() - (mStepPaint.measureText(String.valueOf(mCurrStepNum)) / 2), mStepY,
                mStepPaint);
        String description;
        float percent = getPercent();
        if (percent > 0.5) {
            description = "严重污染";
        } else if (percent < 0.5) {
            description = "中等污染";
        } else {
            description = "普通污染";
        }

        canvas.drawText(description, mWheelRect.centerX() -
                (mTargetPaint.measureText(description) / 2), mTargetY, mTargetPaint);
        int[] colors = {context.getResources().getColor(R.color.color_gray), context.getResources().getColor(R.color.color_gray2_text),
                context.getResources().getColor(R.color.colorAccent),
                context.getResources().getColor(R.color.title_color_dark), context.getResources().getColor(R.color.color_red),
                context.getResources().getColor(R.color.message_color_dark)};
        if (sweepGradient == null) {
            sweepGradient = new SweepGradient(mWheelRect.centerX(), mWheelRect.centerY(), colors, null);
        }
        mFinishWheelPaint.setShader(sweepGradient);
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  宽
     * @param heightMeasureSpec 高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        float mCircleStrokeWidth = getTextScale(15, min);// 圆弧的宽度
        float pressExtraStrokeWidth = getTextScale(10, min);// 圆弧离矩形的距离
        mWheelRect.set(mCircleStrokeWidth + pressExtraStrokeWidth, mCircleStrokeWidth + pressExtraStrokeWidth,
                min - mCircleStrokeWidth - pressExtraStrokeWidth, min - mCircleStrokeWidth - pressExtraStrokeWidth);// 设置矩形
        mTitlePaint.setTextSize(getTextScale(60, min));
        mStepPaint.setTextSize(getTextScale(120, min));
        mTargetPaint.setTextSize(getTextScale(40, min));
        float mTitleY = getTextScale(170, min);
        mStepY = getTextScale(300, min);
        mTargetY = getTextScale(380, min);
        mFinishWheelPaint.setStrokeWidth(mCircleStrokeWidth);
        mCenterWheelPaint.setStrokeWidth(mCircleStrokeWidth);
        mDefaultWheelPaint.setStrokeWidth(mCircleStrokeWidth - getTextScale(2, min));
        mDefaultWheelPaint.setShadowLayer(getTextScale(10, min), 0, 0, Color.rgb(127, 127, 127));// 设置阴影
    }

    /**
     * 进度条动画
     *
     * @author Administrator
     */
    public class BarAnimation extends Animation {
        /**
         * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
         * 然后调用postInvalidate()不停的绘制view。
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//			if (interpolatedTime < 1.0f) {
//				mPercent = Float.parseFloat(mDecimalFormat.format(interpolatedTime * mStepNum * 100f / mMaxStepNum));// 将浮点值四舍五入保留一位小数
//				mSweepAnglePer = interpolatedTime * mStepNum * 360 / mMaxStepNum;
//				mCurrStepNum = (int) (interpolatedTime * mStepNum);
//			} else {
            mPercent = Float.parseFloat(mDecimalFormat.format(mStepNum * 100f / mMaxStepNum));// 将浮点值四舍五入保留一位小数
            if (mPercent > 100.0f) {
                mPercent = 100.0f;
            }
            PERCENT = String.valueOf(mPercent);
            mSweepAnglePer = (float)(mStepNum * 360 / mMaxStepNum);
            mCurrStepNum = mStepNum;
//			}

            requestLayout();
        }
    }

    public float getPercent() {
        return mPercent;
    }

    /**
     * 根据控件的大小改变绝对位置的比例
     *
     * @param n 宽高比例
     * @param m 长
     */
    public float getTextScale(float n, float m) {
        return n / 500 * m;
    }

    /**
     * 更新步数和设置一圈动画时间
     *
     * @param stepCount 更新步数
     * @param time      时间
     */
    public void update(int stepCount, int time) {
        this.mStepNum = stepCount;
        mAnim.setDuration(time);
        // setAnimationTime(time);
        this.startAnimation(mAnim);
    }

    /**
     * @param stepNum 最大的步数
     */
    public void setMaxStepNum(int stepNum) {
        mMaxStepNum = stepNum;
        GOAL_STEP = String.valueOf(mMaxStepNum);
    }

    public void setColor(int color) {
        mFinishWheelPaint.setColor(color);
        mStepPaint.setColor(color);
    }

    /**
     * 设置动画时间
     *
     * @param time 动画时间
     */
    public void setAnimationTime(int time) {
        mAnim.setDuration(time * mStepNum / mMaxStepNum);// 按照比例设置动画执行时间
    }

}
