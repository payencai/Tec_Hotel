package com.tec.hotel_com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.eventBean.UpMonthDate;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.room_manage.entity.DayInfo;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 作者：凌涛 on 2018/5/24 10:22
 * 邮箱：771548229@qq..com
 */
public class MonthDataFormView extends View {

    private Paint mTextPaint;
    private Paint mMonthPaint;
    private Paint mCirclePaint;
    private int mHeightSize = 700;
    private String mTime;
    private int curMouthNum;
    private int mRowSize;//每列的大小
    private int mLineSize;//每行的大小
    private int noDrawCount = 0;//每个月空出多个格不画
    private int mCurrentMonth;//当月的月数
    private int mWidtgSpace = 10;//行间距
    private int drawCircleCount = 0;//画的次数
    private int monthDataTextSize = 0;//文字大小

    private int firstDownX = 0;
    private int firstDownY = 0;

    private int lastUpX = 0;
    private int lastUpY = 0;
    private boolean isLastMonth = false;
    private Map<String, Point> locationMap = new HashMap<>();
    private static Map<String, String> monthlyWeekMap = new HashMap<>();
    private RoomInfo roomInfo;


    private boolean line3 = false;
    private boolean line4 = false;
    private boolean line5 = false;
    private boolean line6 = false;
    private boolean line7 = false;
    private boolean line8 = false;

    private List<DayInfo> mList = new ArrayList<>();

    public static final String[] week = new String[]{"", "日", "一", "二", "三", "四", "五", "六",};
    //当前年份
    private int mCurYear;

    public void setList(List<DayInfo> list) {
        this.mList = list;
        invalidate();
    }

    //一个月签到的集合表
    public void setIsSign(Map<Integer, Boolean> isSign) {
        if (this.isSign != null) {
            this.isSign.clear();
        }
        this.isSign = isSign;
        invalidate();
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
        invalidate();
    }

    private Map<Integer, Boolean> isSign;

    public MonthDataFormView(Context context) {
        this(context, null);
    }

    public MonthDataFormView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthDataFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("ResourceAsColor")
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MonthDataFormView);
        monthDataTextSize = ta.getDimensionPixelSize(R.styleable.MonthDataFormView_monthDataTextSize, R.dimen.text_size_14sp);
        isLastMonth = ta.getBoolean(R.styleable.MonthDataFormView_isLastMouth, false);
        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(2);//设置画笔宽度
        mTextPaint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        mTextPaint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        mTextPaint.setTextSize(monthDataTextSize);//设置文字大小

        mMonthPaint = new Paint();
        mMonthPaint.setStrokeWidth(2);//设置画笔宽度
        mMonthPaint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        mMonthPaint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        mMonthPaint.setColor(Color.BLACK);
        mMonthPaint.setTextAlign(Paint.Align.CENTER);//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
        mMonthPaint.setTextSize(context.getResources().getDimension(R.dimen.text_size_18sp));//设置文字大小

        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.parseColor("#7b7b7b"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        curMouthNum = Integer.parseInt(sdf2.format(new Date()));
        mCurYear = Integer.parseInt(sdf3.format(new Date()));
        if (!isLastMonth) {
            mTime = sdf.format(new Date());
        } else {
            mTime = mCurYear + "年" + (curMouthNum + 1) + "月";
        }
        getDays(mCurYear + ".1.1", mCurYear + ".12.31");
//        for (String s : monthlyWeekMap.keySet()) {
//            Log.d("monthlyWeekMap", "init: " + s + "月的一号：" + monthlyWeekMap.get(s));
//        }
        getCurrentMonthWeek();

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = mHeightSize;
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        //设置测量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = (int) event.getX();
                firstDownY = (int) event.getY();
                lastUpX = (int) event.getX();
                lastUpY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                lastUpX = (int) event.getX();
                lastUpY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_UP:
//                lastUpX = (int) event.getX();
//                lastUpY = (int) event.getY();
//
//                if (firstDownX == lastUpX && firstDownY == lastUpY) {
//                    calculateClickLocation();
//                }

                lastUpX = (int) event.getX();
                lastUpY = (int) event.getY();
                int offestx = lastUpX - firstDownX;
                int offesty = lastUpY - firstDownY;

                if (Math.abs(offestx) <= 10 && Math.abs(offesty) <= 10) {
                    calculateClickLocation();
                }
                return true;
        }
        return false;
    }

    private void calculateClickLocation() {
        int i = lastUpY / mLineSize;//第j行
        int j = lastUpX / mRowSize;//第i列
        if (i >= 2) {
            if (i == 2 && j < noDrawCount) {
                return;
            }
            String flag = "i=" + i + "j=" + j;
//            String s = locationMap.get(flag);
            Point point = locationMap.get(flag);
            if (point == null) {
                return;
            }
            String s = point.getText();
            if (!TextUtils.isEmpty(s)) {
//                int index = getCurDayRoomNumber(Integer.parseInt(s));
                int index = point.getIndex();
                if (index != -1) {
                    showSynopsisEditWindow(this, point);
                } else {
//                    ToaskUtil.showToast(getContext(), "点击了第" + i + "行,第" + j + "列，号数：" + s);
                    ToaskUtil.showToast(getContext(), "只能修改最近一个月内的数据");
                }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawCircleCount = 0;
        line3 = false;
        line4 = false;
        line5 = false;
        line6 = false;
        line7 = false;
        line8 = false;
        mRowSize = getWidth() / 8;//每一列的宽度
        mLineSize = getHeight() / 8;
        mTextPaint.setColor(Color.BLACK);
        Rect textBounds = new Rect();

        mTextPaint.getTextBounds(mTime, 0, mTime.length(), textBounds);
        canvas.drawText(mTime, getWidth() / 2, mLineSize / 2 + textBounds.height() / 2, mTextPaint);
//        for (int i = 0; i < 7; i++) {
//            canvas.drawLine(i * mRowSize, 0, i * mRowSize, getHeight(), mTextPaint);
//        }
//        for (int i = 0; i < 9; i++) {
//            canvas.drawLine(0, i * mLineSize, getWidth(), i * mLineSize, mTextPaint);
//        }

        for (int i = 1; i < 8; i++) {//行数
            for (int j = 0; j < 8; j++) {//列数
                if (i == 1) {
                    String s = week[j];
                    Rect weekRounds = new Rect();
                    mTextPaint.getTextBounds(s, 0, s.length(), weekRounds);
                    int left = j * mRowSize + mRowSize / 2;
                    canvas.drawText(s, left, mLineSize + mLineSize / 2 + weekRounds.height() / 2, mTextPaint);
                } else {//其他都是第二行往后
                    if (i == 2) {
                        if (j < noDrawCount) {
                            continue;
                        } else {
                            drawCircle(canvas, i, j);
                        }
                    } else {
                        drawCircle(canvas, i, j);
                    }
                }
            }
        }
    }

    private void drawCircle(Canvas canvas, int i, int j) {
        if (drawCircleCount >= mCurrentMonth) {
            return;
        }
        int left = j * mRowSize;//列宽
        int top = (i + 1) * mLineSize;
        int right = left + mRowSize;
        int bottom = top + mLineSize;
        Rect rect = new Rect(left, top, right, bottom);
        int min = Math.min(mRowSize, mLineSize);
        //得到正方形的大小
        int squareSize = min - mWidtgSpace;
        //得到圆心x坐标
        int x = j * mRowSize + mRowSize / 2;
        int y = i * mLineSize + mLineSize / 2;
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        int averageNum = mLineSize / 3;
//        if (i == 1 && !line1) {
//            drawLineHead(canvas, left, i, averageNum);
//            return;
//        }
//        if (i == 2 && !line2) {
//            line2 = true;
//            drawLineHead(canvas, left, i, averageNum);
//            return;
//        }
        if (i == 3 && !line3) {
            line3 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }
        if (i == 4 && !line4) {
            line4 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }
        if (i == 5 && !line5) {
            line5 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }
        if (i == 6 && !line6) {
            line6 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }
        if (i == 7 && !line7) {
            line7 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }
        if (i == 8 && !line8) {
            line8 = true;
            drawLineHead(canvas, left, i, averageNum);
            return;
        }


        int isCurDaySign = drawCircleCount + 1;

        //本月的号数
        String text = isCurDaySign + "";
        Rect weekRounds = new Rect();
        mMonthPaint.getTextBounds(text, 0, text.length(), weekRounds);

        canvas.drawText(text, left + (mRowSize / 2), (i * mLineSize) + averageNum / 2 + weekRounds.height() / 2, mMonthPaint);

        //画房间数量
        int index = getCurDayRoomNumber(isCurDaySign);
        String totalNum = "0";
        String orderNum = "0";
        if (index != -1) {
            DayInfo dayInfo = mList.get(index);
            totalNum = dayInfo.getTotalNum() + "";
            orderNum = dayInfo.getOrderNum() + "";
        }
//

        Rect tatalText = new Rect();
        mMonthPaint.getTextBounds(totalNum, 0, totalNum.length(), tatalText);
        if (index != -1) {
            mTextPaint.setColor(Color.parseColor("#333333"));
        } else {
            mTextPaint.setColor(Color.parseColor("#dddddd"));
        }
        mTextPaint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_12sp));
        canvas.drawText(totalNum, left + (mRowSize / 2), (i * mLineSize) + 3 * (averageNum / 2), mTextPaint);

        //画订单数量
        Rect orderRect = new Rect();
        mMonthPaint.getTextBounds(orderNum, 0, orderNum.length(), orderRect);
        if (index != -1) {
            mTextPaint.setColor(Color.parseColor("#ff5426"));
        } else {
            mTextPaint.setColor(Color.parseColor("#dddddd"));
        }
//        int nextInt = new Random().nextInt(50);
        canvas.drawText(orderNum, left + (mRowSize / 2), (i * mLineSize) + 4 * (averageNum / 2) + weekRounds.height() / 2, mTextPaint);

        String flag = "i=" + i + "j=" + j;
        locationMap.put(flag, new Point(text, Integer.parseInt(totalNum), Integer.parseInt(orderNum), index));
        drawCircleCount++;
    }

    private void drawLineHead(Canvas canvas, int left, int i, int averageNum) {
        String text = "房数";
        Rect weekRounds = new Rect();
        mMonthPaint.getTextBounds(text, 0, text.length(), weekRounds);
        mTextPaint.setColor(Color.parseColor("#888888"));
        mTextPaint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_12sp));
        canvas.drawText(text, left + (mRowSize / 2), (i * mLineSize) + 3 * (averageNum / 2), mTextPaint);
        canvas.drawText("单数", left + (mRowSize / 2), (i * mLineSize) + 4 * (averageNum / 2) + weekRounds.height() / 2, mTextPaint);

    }

    private void getCurrentMonthWeek() {
        String format1 = "";
        if (!isLastMonth) {
            format1 = monthlyWeekMap.get(curMouthNum + "");
            mCurrentMonth = getCurrentMonthDay();
        } else {
            format1 = monthlyWeekMap.get((curMouthNum + 1) + "");
            mCurrentMonth = getMonthLastDay(mCurYear, curMouthNum + 1);
        }

        if (format1.endsWith("日") || format1.endsWith("SUN")) {
            noDrawCount = 0;
        } else if (format1.endsWith("一") || format1.equals("MON")) {
            noDrawCount = 1;
        } else if (format1.endsWith("二") || format1.equals("TUE")) {
            noDrawCount = 2;
        } else if (format1.endsWith("三") || format1.equals("WED")) {
            noDrawCount = 3;
        } else if (format1.endsWith("四") || format1.equals("THU")) {
            noDrawCount = 4;
        } else if (format1.endsWith("五") || format1.equals("FRI")) {
            noDrawCount = 5;
        } else {
            noDrawCount = 6;
        }
        noDrawCount += 1;
    }

    /**
     * 获取当月的 天数
     */
    private static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static void getDays(String from, String to) {
        Calendar calendar = Calendar.getInstance();
        String[] array = {from, to};
        Date[] ds = new Date[array.length];
        for (int i = 0; i < array.length; i++) {
            String[] fs = array[i].split("[^\\d]+");
            calendar.set(Integer.parseInt(fs[0]), Integer.parseInt(fs[1]) - 1, Integer.parseInt(fs[2]));
            ds[i] = calendar.getTime();
        }
        for (Date x = ds[0]; x.compareTo(ds[1]) <= 0; ) {
            calendar.setTime(x);
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            String[] week = "日一二三四五六".split("");
            if (calendar.get(Calendar.DATE) == 1) {
//                System.out.println(calendar.get(Calendar.YEAR) + "的" + (calendar.get(Calendar.MONTH) + 1) + "月1号是 星期" + week[today - 1]);
                monthlyWeekMap.put((calendar.get(Calendar.MONTH) + 1) + "", "星期" + week[today]);
            }
            calendar.add(Calendar.MONTH, 1);
            x = calendar.getTime();
        }
    }

    private void showSynopsisEditWindow(View view, Point point) {
        View brandView = LayoutInflater.from(getContext()).inflate(R.layout.pw_updata_room_num_edit, null);
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(brandView)
                .sizeByPercentage(getContext(), 0.8f, 0f)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .setBgDarkAlpha(0.5f)
                .create();
        handlerSynopsisEditWindow(brandView, customPopWindow, point);
        customPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void handlerSynopsisEditWindow(View view, final CustomPopWindow customPopWindow, final Point point) {
        ((TextView) view.findViewById(R.id.dayNum)).setText(point.text + "日");
        final EditText editText = (EditText) view.findViewById(R.id.synopsis_edit);
        editText.setText(point.getRoomTotal() + "");
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getEditableText().toString();
                if (roomInfo == null) {
                    return;
                }
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }

                updata(number, point);
            }
        });

    }

    /**
     * 得到当前天对应的list的下标
     *
     * @return
     */
    public int getCurDayRoomNumber(int dayNumber) {
        int curNumber = -1;
        try {
            if (mList.isEmpty()) {
                return curNumber;
            }
            for (int i = 0; i < mList.size(); i++) {
                DayInfo dayInfo = mList.get(i);
                String enterTime = dayInfo.getEnterTime();
                String[] split = enterTime.split(" ");
                String ymdString = split[0];
                String[] yms = ymdString.split("-");
                String y = yms[0];
                String m = yms[1];
                String d = yms[2];
                if (!isLastMonth) {
                    if (Integer.parseInt(y) == mCurYear && Integer.parseInt(m) == curMouthNum && Integer.parseInt(d) == dayNumber) {
                        curNumber = i;
                        return curNumber;
                    }
                } else {
                    if (Integer.parseInt(y) == mCurYear && Integer.parseInt(m) == curMouthNum + 1 && Integer.parseInt(d) == dayNumber) {
                        curNumber = i;
                        return curNumber;
                    }
                }

            }
        } catch (Exception e) {

        }


        return curNumber;
    }

    private static class Point {
        private String text;
        private int roomTotal;
        private int singular;
        private int index;//在列表的下标，不存在则为-1

        public Point(String text, int roomTotal, int singular, int index) {
            this.text = text;
            this.roomTotal = roomTotal;
            this.singular = singular;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRoomTotal() {
            return roomTotal;
        }

        public void setRoomTotal(int roomTotal) {
            this.roomTotal = roomTotal;
        }

        public int getSingular() {
            return singular;
        }

        public void setSingular(int singular) {
            this.singular = singular;
        }
    }

    private void updata(String num, Point point) {
        int index = point.index;
        DayInfo dayInfo = mList.get(index);
        String enterTime = dayInfo.getEnterTime();
        String data = enterTime.split(" ")[0];

        Map<String, Object> parame = new HashMap<>();
        parame.put("roomTypeId", roomInfo.getId());
        parame.put("number", num);
        parame.put("date", data);
        HttpProxy.obtain().post(PlatformContans.Roomnum.updateRoomnumByRoomTypeAndDate, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("updateRoomnum", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        //重新请求更新数据
                        EventBus.getDefault().post(new UpMonthDate());
                        ToaskUtil.showToast(getContext(), "提交成功");

                    } else {
                        ToaskUtil.showToast(getContext(), message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });

    }
}
