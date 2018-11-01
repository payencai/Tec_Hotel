package com.tec.hotel_com.data_manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.DataType;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseFragment;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.data_manage.activity.DataRecordActivity;
import com.tec.hotel_com.data_manage.entity.DayDataInfo;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.WeekDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者：凌涛 on 2018/8/6 11:07
 * 邮箱：771548229@qq..com
 */
public class DataFragment extends BaseFragment {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.OccupancyRate)
    TextView OccupancyRate;
    @BindView(R.id.mine)
    ImageView mine;
    @BindView(R.id.amountTotal)
    TextView amountTotalTV;
    @BindView(R.id.orderTotal)
    TextView orderTotalTV;
    @BindView(R.id.monthAmountTotal)
    TextView monthAmountTotalTV;
    @BindView(R.id.monthOrderTotal)
    TextView monthOrderTotalTV;

    @BindView(R.id.WeekDataView)
    WeekDataView mWeekDataView;



    @Override
    protected int getContentId() {
        return R.layout.fragment_data_manager;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.GONE);
        title.setText("酒店");
        mine.setVisibility(View.VISIBLE);

        requstOccupancyRate();
        getCountOrdersForHotelApp();
        requestWeekData();
    }

    private void requestWeekData() {

        HttpProxy.obtain().get(PlatformContans.Statistics.countCountRecentlyForHotelApp,
                App.getInstance().getUserEntity().getToken(), new ICallBack() {
                    @Override
                    public void OnLtSuccess(String result) {
                        LogUtil.Log("countCount", result);
                        try {
                            JSONObject object = new JSONObject(result);
                            int resultCode = object.getInt("resultCode");
                            if (resultCode == 0) {
                                JSONArray data = object.getJSONArray("data");
                                int length = data.length();
                                Gson gson = new Gson();
                                List<DayDataInfo> list = new LinkedList<>();
                                for (int i = 0; i < length; i++) {
                                    JSONObject item = data.getJSONObject(i);
                                    DayDataInfo bean = gson.fromJson(item.toString(), DayDataInfo.class);
                                    list.add(bean);
                                }

                                processData(list);
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

    private void processData(List<DayDataInfo> list) {
        int[] args = new int[7];
        String[] weeks = new String[7];
        for (int i = 0; i < list.size(); i++) {
            DayDataInfo dayDataInfo = list.get(i);
            int amount = dayDataInfo.getAmount();
            args[i] = amount;
//            if (i % 2 == 0) {
//                args[i] = i * 100;
//            } else {
//                args[i] = i ;
//            }
//            args[i] = 10 ;
            String date = dayDataInfo.getDate();
            String substring = date.substring(5);
            weeks[i] = substring;
        }
        mWeekDataView.setWeekCount(args);
        mWeekDataView.setWeekStr(weeks);

    }


    @OnClick({R.id.dayData, R.id.monthData, R.id.yearData, R.id.mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dayData:
                DataRecordActivity.startDtaRecordActivity(getContext(), DataType.DayData);
                break;
            case R.id.monthData:
                DataRecordActivity.startDtaRecordActivity(getContext(), DataType.MonthDta);
                break;
            case R.id.yearData:
                DataRecordActivity.startDtaRecordActivity(getContext(), DataType.YearData);
                break;
            case R.id.mine:
                startActivity(new Intent(getContext(), MineActivity.class));
                break;
        }
    }


    /**
     * 酒店app统计本月入住率
     */
    private void requstOccupancyRate() {
        HttpProxy.obtain().get(PlatformContans.Statistics.getOccupancyRateThisMonthForHotelApp, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("OccupancyRate", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        double data = object.getDouble("data");
                        String str = (data * 100) + "%";
                        OccupancyRate.setText(str);
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

    /**
     * 酒店app统计订单状况
     */
    private void getCountOrdersForHotelApp() {
        HttpProxy.obtain().get(PlatformContans.Statistics.countOrdersForHotelApp, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("countOrdersForHotel", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        JSONObject today = data.getJSONObject("today");
                        JSONObject thisMonth = data.getJSONObject("thisMonth");
                        int orderTotal = today.getInt("orderTotal");
                        double amountTotal = today.getDouble("amountTotal");
                        int monthOrderTotal = thisMonth.getInt("orderTotal");
                        double monthAmountTotal = thisMonth.getDouble("amountTotal");

                        amountTotalTV.setText(amountTotal + "");
                        orderTotalTV.setText(orderTotal + "");
                        monthAmountTotalTV.setText(monthAmountTotal + "");
                        monthOrderTotalTV.setText(monthOrderTotal + "");


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

    /**
     * 酒店app按天统计数据
     */
    private void getCountBydayForHotelApp() {

        Map<String, Object> parame = new HashMap<>();
        parame.put("date", "");
        HttpProxy.obtain().get(PlatformContans.Statistics.CountBydayForHotelApp, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("CountBydayForHotel", result);
            }

            @Override
            public void onLtFailure(String error) {

            }
        });

    }

}
