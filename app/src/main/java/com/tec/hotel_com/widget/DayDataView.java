package com.tec.hotel_com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tec.hotel_com.R;

/**
 * 作者：凌涛 on 2018/8/14 11:46
 * 邮箱：771548229@qq..com
 */
public class DayDataView extends View {
    private Paint mTextPaint;
    private Paint mCirclePaint;
    private int drawCircleCount = 0;//当前月的几号

    private int viewWidth = 0;
    private int viewHeight = 0;

    private int mWidtgSpace = 10;//行间距

    public DayDataView(Context context) {
        this(context, null);
    }

    public DayDataView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(2);//设置画笔宽度
        mTextPaint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        mTextPaint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.text_size_14sp));//设置文字大小

        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));
    }


    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
        invalidate();
    }


    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
        invalidate();
    }

    public void setDrawCircleCount(int drawCircleCount) {
        this.drawCircleCount = drawCircleCount;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        int min = Math.min(viewWidth, viewHeight);
        //得到正方形的大小
        int squareSize = min - mWidtgSpace;
        //得到圆心x坐标
        int x = viewWidth / 2;
        int y = viewHeight / 2;


        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.STROKE);

        //画圆
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));
        canvas.drawCircle(x, y, (squareSize / 2) - 3, mCirclePaint);
        canvas.drawCircle(x, y, (squareSize / 2) - 10, mCirclePaint);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#ff9500"));
        canvas.drawCircle(x, y, (squareSize / 2) - 10, mCirclePaint);

        //画月数
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        String text = drawCircleCount + "";
        Rect weekRounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), weekRounds);
        canvas.drawText(text, x, y + weekRounds.height() / 2, mTextPaint);
        drawCircleCount++;
    }
}
