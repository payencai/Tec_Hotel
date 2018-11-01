package com.tec.hotel_com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tec.hotel_com.R;

/**
 * 作者：凌涛 on 2018/8/6 14:19
 * 邮箱：771548229@qq..com
 */
public class UpperHarfCircularView extends RelativeLayout {

    private Paint mPaint;
    //圆角半径
    private int circularRadius;
    //背景颜色
    private int bgColor;


    public UpperHarfCircularView(Context context) {
        this(context, null);
    }

    public UpperHarfCircularView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpperHarfCircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("ResourceAsColor")
    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UpperHarfCircularView);
        bgColor = ta.getColor(R.styleable.UpperHarfCircularView_bgColor, Color.parseColor("#ffa726"));
        circularRadius = ta.getDimensionPixelSize(R.styleable.UpperHarfCircularView_circularRadius, R.dimen.fbutton_default_shadow_height);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawColor(bgColor);
    }
}
