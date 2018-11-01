package com.tec.hotel_com.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tec.hotel_com.R;


/**
 * 作者：凌涛 on 2018/8/21 16:41
 * 邮箱：771548229@qq..com
 */
public class WeekDataView extends View implements View.OnClickListener {

    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;

    private Paint mTextPaint;
    private int mRowSize;//每列的大小
    private int mLineSize;//每行的大小
    private int mPadding;
    private float textSize;
    //最近7天的最大数据
    private float recentMax;

    private String[] weekStr = new String[]{"07-25", "07-25", "07-25", "07-25", "07-25", "07-25"};
    private int[] weekCount = new int[]{0, 0, 0, 0, 0, 0, 0};
    private int mSize;
    private ValueAnimator mValueAnimator;

    public WeekDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setRecentMax();
    }

    public WeekDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setRecentMax();
    }

    public void setWeekStr(String[] weekStr) {
        this.weekStr = weekStr;
        invalidate();
    }
    public void setWeekCount(int[] weekCount) {
        this.weekCount = weekCount;
        setRecentMax();
    }

    private void setRecentMax() {
        int temp = weekCount[0];
        for (int i = 1; i < weekCount.length; i++) {
            if (weekCount[i] > temp) {
                temp = weekCount[i];
            }
        }
        recentMax = temp;
        Log.d("setRecentMax", "setRecentMax: " + recentMax);
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WeekDataView);
        mPadding = ta.getDimensionPixelSize(R.styleable.WeekDataView_mPadding, (int) context.getResources().getDimension(R.dimen.activity_vertical_margin));
        textSize = ta.getDimensionPixelSize(R.styleable.WeekDataView_textPaint, (int) context.getResources().getDimension(R.dimen.week_data_textpaint));
        ta.recycle();
        mSize = getContext().getResources().getDimensionPixelSize(R.dimen.week_data_textpaint);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.parseColor("#ffa726"));

        mPath = new Path();
        mDst = new Path();

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.setDuration(3000);
        mValueAnimator.setRepeatCount(0);
        mValueAnimator.start();

        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRowSize = (getWidth() - 2 * mPadding) / 8;//每一列的宽度
        mLineSize = (getHeight() - 2 * mPadding) / 8;




        mPath.rewind();
//        mDst.rewind();
        int alwaysEqual = 5 * mLineSize;
        mPath.moveTo(mPadding + mRowSize, mPadding + 2 * mLineSize + alwaysEqual * (recentMax - weekCount[0]) / recentMax);
        for (int i = 1; i < 7; i++) {
            mPath.lineTo(mPadding + mRowSize * (i + 1), mPadding + 2 * mLineSize + ((5 * mLineSize) * ((recentMax - weekCount[i]) / recentMax)));
        }
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);
        mLength = mPathMeasure.getLength();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setStrokeWidth(1);
        for (int i = 2; i < 8; i++) {//画竖线
            canvas.drawLine(i * mRowSize + mPadding, mPadding + mLineSize, i * mRowSize + mPadding, getHeight() - mPadding - mLineSize, mTextPaint);
        }
        for (int i = 2; i < 7; i++) {//画横线
            canvas.drawLine(mPadding + mRowSize, (i * mLineSize) + mPadding, getWidth() - mPadding, (i * mLineSize) + mPadding, mTextPaint);
        }

        mTextPaint.setColor(Color.parseColor("#9b9b9b"));
        mTextPaint.setStrokeWidth(4);
        //画竖线
        canvas.drawLine(mRowSize + mPadding, mPadding, mRowSize + mPadding, getHeight() - mLineSize - mPadding, mTextPaint);
        //画横线
        canvas.drawLine(mPadding + mRowSize, (7 * mLineSize) + mPadding + 3, getWidth() - mPadding, (7 * mLineSize) + mPadding + 3, mTextPaint);
        drawMoneyText(canvas);
        drawPath(canvas);

    }

    private void drawPath(Canvas canvas) {

        mDst.reset();
        // 硬件加速的BUG
        mDst.lineTo(0, 0);
        float stop = mLength * mAnimatorValue;
        mPathMeasure.getSegment(0, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
//        canvas.drawPath(mPath, mPaint);
    }

    private void drawMoneyText(Canvas canvas) {
        int offset = (int) (recentMax / 5);
        for (int i = 0; i < 7; i++) {
            String text = "";
            if (i == 0) {
                text = "(元)";
            } else {
                text = (((int) (recentMax - offset * (i - 1)))) + "";
            }
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), bounds);
            int w = (mRowSize - bounds.width()) / 2;
            int h = (mLineSize - bounds.height()) / 2;
            canvas.drawText(text, mPadding + w, mPadding + mLineSize * (i + 1) + (h / 2), mTextPaint);
        }

        mTextPaint.setTextSize(mSize);
        for (int i = 0; i < 6; i++) {
            String date = weekStr[i];
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(date, 0, date.length(), bounds);
            int w = bounds.width() / 2;
            int h = (mLineSize - bounds.height()) / 2;
            canvas.drawText(date, mPadding + mRowSize * (i + 2) - w, mPadding + 7 * mLineSize + h + bounds.height(), mTextPaint);
        }

    }

    @Override
    public void onClick(View v) {
        mValueAnimator.start();
    }
}
