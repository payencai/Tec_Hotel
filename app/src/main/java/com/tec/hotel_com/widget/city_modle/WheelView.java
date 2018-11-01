package com.tec.hotel_com.widget.city_modle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 作者：凌涛 on 2018/10/17 10:06
 * 邮箱：771548229@qq..com
 */
public class WheelView extends ScrollView {

    public static final String TAG = WheelView.class.getSimpleName();
    private Context context;
    private LinearLayout views;
    List<String> items;
    public static final int OFF_SET_DEFAULT = 1;
    int offset = 1;
    int displayItemCount;
    int selectedIndex = 1;
    int initialY;
    Runnable scrollerTask;
    int newCheck = 50;
    int itemHeight = 0;
    int[] selectedAreaBorder;
    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    Paint paint;
    int viewWidth;
    private WheelView.OnWheelViewListener onWheelViewListener;

    public WheelView(Context context) {
        super(context);
        this.init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }

    private List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> list) {
        if (null == this.items) {
            this.items = new ArrayList();
        }

        this.items.clear();
        this.items.addAll(list);

        for(int i = 0; i < this.offset; ++i) {
            this.items.add(0, "");
            this.items.add("");
        }

        this.initData();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;
        Log.d(TAG, "parent: " + this.getParent());
        this.setVerticalScrollBarEnabled(false);
        this.views = new LinearLayout(context);
        this.views.setOrientation(1);
        this.addView(this.views);
        this.scrollerTask = new Runnable() {
            public void run() {
                int newY = WheelView.this.getScrollY();
                if (WheelView.this.initialY - newY == 0) {
                    final int remainder = WheelView.this.initialY % WheelView.this.itemHeight;
                    final int divided = WheelView.this.initialY / WheelView.this.itemHeight;
                    if (remainder == 0) {
                        WheelView.this.selectedIndex = divided + WheelView.this.offset;
                        WheelView.this.onSeletedCallBack();
                    } else if (remainder > WheelView.this.itemHeight / 2) {
                        WheelView.this.post(new Runnable() {
                            public void run() {
                                WheelView.this.smoothScrollTo(0, WheelView.this.initialY - remainder + WheelView.this.itemHeight);
                                WheelView.this.selectedIndex = divided + WheelView.this.offset + 1;
                                WheelView.this.onSeletedCallBack();
                            }
                        });
                    } else {
                        WheelView.this.post(new Runnable() {
                            public void run() {
                                WheelView.this.smoothScrollTo(0, WheelView.this.initialY - remainder);
                                WheelView.this.selectedIndex = divided + WheelView.this.offset;
                                WheelView.this.onSeletedCallBack();
                            }
                        });
                    }
                } else {
                    WheelView.this.initialY = WheelView.this.getScrollY();
                    WheelView.this.postDelayed(WheelView.this.scrollerTask, (long)WheelView.this.newCheck);
                }

            }
        };
    }

    public void startScrollerTask() {
        this.initialY = this.getScrollY();
        this.postDelayed(this.scrollerTask, (long)this.newCheck);
    }

    private void initData() {
        this.displayItemCount = this.offset * 2 + 1;
        this.views.removeAllViews();
        Iterator var1 = this.items.iterator();

        while(var1.hasNext()) {
            String item = (String)var1.next();
            this.views.addView(this.createView(item));
        }

        this.refreshItemView(0);
    }

    private TextView createView(String item) {
        TextView tv = new TextView(this.context);
        tv.setLayoutParams(new LayoutParams(-1, -2));
        tv.setSingleLine(true);
        tv.setTextSize(2, 16.0F);
        tv.setText(item);
        tv.setGravity(17);
        int padding = this.dip2px(15.0F);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == this.itemHeight) {
            this.itemHeight = this.getViewMeasuredHeight(tv);
            Log.d(TAG, "itemHeight: " + this.itemHeight);
            this.views.setLayoutParams(new LayoutParams(-1, this.itemHeight * this.displayItemCount));
            android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams)this.getLayoutParams();
            this.setLayoutParams(new android.widget.LinearLayout.LayoutParams(lp.width, this.itemHeight * this.displayItemCount));
        }

        return tv;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        this.refreshItemView(t);
        if (t > oldt) {
            this.scrollDirection = 1;
        } else {
            this.scrollDirection = 0;
        }

    }

    private void refreshItemView(int y) {
        int position = y / this.itemHeight + this.offset;
        int remainder = y % this.itemHeight;
        int divided = y / this.itemHeight;
        if (remainder == 0) {
            position = divided + this.offset;
        } else if (remainder > this.itemHeight / 2) {
            position = divided + this.offset + 1;
        }

        int childSize = this.views.getChildCount();

        for(int i = 0; i < childSize; ++i) {
            TextView itemView = (TextView)this.views.getChildAt(i);
            if (null == itemView) {
                return;
            }

            if (position == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
            }
        }

    }

    private int[] obtainSelectedAreaBorder() {
        if (null == this.selectedAreaBorder) {
            this.selectedAreaBorder = new int[2];
            this.selectedAreaBorder[0] = this.itemHeight * this.offset;
            this.selectedAreaBorder[1] = this.itemHeight * (this.offset + 1);
        }

        return this.selectedAreaBorder;
    }

    public void setBackgroundDrawable(Drawable background) {
        if (this.viewWidth == 0) {
            this.viewWidth = ((Activity)this.context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + this.viewWidth);
        }

        if (null == this.paint) {
            this.paint = new Paint();
            this.paint.setColor(Color.parseColor("#83cde6"));
            this.paint.setStrokeWidth((float)this.dip2px(1.0F));
        }

        background = new Drawable() {
            public void draw(Canvas canvas) {
                canvas.drawLine((float)(WheelView.this.viewWidth * 1 / 6), (float)WheelView.this.obtainSelectedAreaBorder()[0], (float)(WheelView.this.viewWidth * 5 / 6), (float)WheelView.this.obtainSelectedAreaBorder()[0], WheelView.this.paint);
                canvas.drawLine((float)(WheelView.this.viewWidth * 1 / 6), (float)WheelView.this.obtainSelectedAreaBorder()[1], (float)(WheelView.this.viewWidth * 5 / 6), (float)WheelView.this.obtainSelectedAreaBorder()[1], WheelView.this.paint);
            }

            public void setAlpha(int alpha) {
            }

            public void setColorFilter(ColorFilter cf) {
            }

            public int getOpacity() {
                return 0;
            }
        };
        super.setBackgroundDrawable(background);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        this.viewWidth = w;
        this.setBackgroundDrawable((Drawable)null);
    }

    private void onSeletedCallBack() {
        if (null != this.onWheelViewListener) {
            this.onWheelViewListener.onSelected(this.selectedIndex, (String)this.items.get(this.selectedIndex));
        }

    }

    public void setSeletion(final int position) {
        this.selectedIndex = position + this.offset;
        this.post(new Runnable() {
            public void run() {
                WheelView.this.scrollTo(0, position * WheelView.this.itemHeight);
            }
        });
    }

    public void setSeletionSmooth(final int position) {
        this.selectedIndex = position + this.offset;
        this.post(new Runnable() {
            public void run() {
                WheelView.this.smoothScrollTo(0, position * WheelView.this.itemHeight);
            }
        });
    }

    public String getSeletedItem() {
        return (String)this.items.get(this.selectedIndex);
    }

    public int getSeletedIndex() {
        return this.selectedIndex - this.offset;
    }

    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 1) {
            this.startScrollerTask();
        }

        return super.onTouchEvent(ev);
    }

    public WheelView.OnWheelViewListener getOnWheelViewListener() {
        return this.onWheelViewListener;
    }

    public void setOnWheelViewListener(WheelView.OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, 0);
        int expandSpec = MeasureSpec.makeMeasureSpec(536870911, -2147483648);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

    public static class OnWheelViewListener {
        public OnWheelViewListener() {
        }

        public void onSelected(int selectedIndex, String item) {
        }
    }
}
