package com.tec.hotel_com.data_manage.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.DataType;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.abs.AbsBaseActivity;
import com.tec.hotel_com.common.rv.base.Cell;
import com.tec.hotel_com.data_manage.entity.RecordDataBean;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRecordActivity extends AbsBaseActivity<RecordDataBean> {

    private DataType mDataType;
    private TextView title;

    /**
     * @param context
     * @param dataType 加载什么数据
     */
    public static void startDtaRecordActivity(Context context, DataType dataType) {

        Intent intent = new Intent(context, DataRecordActivity.class);
        intent.putExtra("loadDataType", dataType);
        context.startActivity(intent);

    }

//    @Override
//    protected int getContentId() {
//        return R.layout.activity_data_record;
//    }

    @Override
    public void onRecyclerViewInitialized() {
        Intent intent = getIntent();
        mDataType = ((DataType) intent.getSerializableExtra("loadDataType"));
        if (mDataType == null) {
            finish();
            return;
        }
        beforeLoad();
    }

    private void beforeLoad() {
        switch (mDataType) {
            case DayData:
                requestDayData();
                title.setText("日数据");
                break;
            case MonthDta:
                requestMonthData();
                title.setText("月数据");
                break;
            case YearData:
                requestYearData();
                title.setText("年度数据");
                break;
        }
    }

    @Override
    public void onPullRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        beforeLoad();
        hideLoadMore();
    }

    @Override
    protected List<Cell> getCells(List list) {
        return null;
    }

    /**
     * 初始化日数据
     */
    private void requestDayData() {
        List<RecordDataBean> mList = new ArrayList<>();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String day = dayFormat.format(new Date());
        String month = monthFormat.format(new Date());
        int maxDay = Integer.parseInt(day);
        int curMonth = Integer.parseInt(month);

        for (int i = 1; i <= maxDay; i++) {
            RecordDataBean recordDataBean = new RecordDataBean();
            recordDataBean.dateType = 0;
            recordDataBean.setCurMonth(curMonth);
            recordDataBean.setDay(i);
            mList.add(recordDataBean);
        }
        mBaseAdapter.setDataByRemove(mList);

    }

    /**
     * 初始化月数据
     */
    private void requestMonthData() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String yearStr = yearFormat.format(new Date());
        String monthStr = monthFormat.format(new Date());

        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);

        List<RecordDataBean> mList = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            RecordDataBean recordDataBean = new RecordDataBean();
            recordDataBean.setYear(year);
            recordDataBean.dateType = 1;
            recordDataBean.setCurMonth(i);
            mList.add(recordDataBean);
        }
        mBaseAdapter.setDataByRemove(mList);
    }

    private void requestYearData() {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String yearStr = yearFormat.format(new Date());
        String monthStr = monthFormat.format(new Date());

        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);


        List<RecordDataBean> mList = new ArrayList<>();
        for (int i = year - 10; i <= year; i++) {
            RecordDataBean recordDataBean = new RecordDataBean();
            recordDataBean.setYear(i);
            recordDataBean.dateType = 2;
            mList.add(recordDataBean);
        }
        mBaseAdapter.setDataByRemove(mList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View addToolbar() {
        View view = LayoutInflater.from(this).inflate(R.layout.abs_toolbar_hotel_layout, null);
        view.findViewById(R.id.mineBtn).setVisibility(View.GONE);
        title = ((TextView) view.findViewById(R.id.title));
        ImageView backImg = (ImageView) view.findViewById(R.id.back);
        backImg.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return view;
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

}
