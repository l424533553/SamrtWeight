package com.axecom.smartweight.service;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.axecom.smartweight.R;


/**
 * Created by C.Shawn on 2017/3/23 0023.
 *
 * @author C.Shawn 圆角控件  颜色自己填充
 * 使用：roundedCornerImageView.setBackgroundColor(context.getResources().getColor(item.getColor()));
 *
 *        <com.axecom.smartweight.service.RoundedCornerImageView
 *               android:src="@mipmap/tid"
 *               app:round_radius="@dimen/x45" />
 *
 *
 *     <declare-styleable name="RoundedCornerImageView">
 *         <attr name="round_radius" format="dimension" />
 *          <attr name="round_color" format="color" />
 *         <!--    <attr name="text_color" format="color" />
 *           <attr name="text_size" format="dimension" />-->
 *     </declare-styleable>
 */

public class RoundedCornerImageView extends android.support.v7.widget.AppCompatImageView {

    // 像素点
    // private final float density = getContext().getResources().getDisplayMetrics().density;
    private final float roundness;
    private static final int DEFAULT_RECT_ROUND_RADIUS = 0;

    public RoundedCornerImageView(Context context) {
//                super(context);
        this(context, null);
        //   init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
//                super(context, attrs);
        this(context, attrs, 0);
        //   init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取attr文件下，名为RoundedCornerImageView
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedCornerImageView, defStyle, 0);
        //获取值
        roundness = ta.getDimensionPixelSize(R.styleable.RoundedCornerImageView_round_radius, dip2px());
        ta.recycle();

        //  init();
    }

    @Override
    public void draw(Canvas canvas) {
        //创建bitmap
        final Bitmap composedBitmap;
        final Bitmap originalBitmap;
        //创建画布
        final Canvas composedCanvas;
        final Canvas originalCanvas;
        final Paint paint;
        final int height;
        final int width;

        width = getWidth();

        height = getHeight();
        //ARGB_4444 代表16位Alpha的位图
        //ARGB_8888 代表32位ARGB位图
        composedBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        originalBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        composedCanvas = new Canvas(composedBitmap);
        originalCanvas = new Canvas(originalBitmap);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        super.draw(originalCanvas);

        composedCanvas.drawARGB(0, 0, 0, 0);
        //指定RectF对象以及圆角半径来实现，该方法是绘制圆形的主要方法，同时也可以通过设置画笔的空心效果来绘制空心的圆形
        //thi,roundness 分别是x,y方向的圆角半径
        composedCanvas.drawRoundRect(new RectF(0, 0, width, height),
                this.roundness, this.roundness, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);

        canvas.drawBitmap(composedBitmap, 0, 0, new Paint());
    }

//    public float getRoundness() {
//        return this.roundness / this.density;
//    }
//
//    public void setRoundness(float roundness) {
//        this.roundness = roundness * this.density;
//    }

    //    private void init() {
//        setRoundness(5);
//    }
    //dp转px
    private int dip2px() {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (DEFAULT_RECT_ROUND_RADIUS * scale + 0.5f);
    }
}
