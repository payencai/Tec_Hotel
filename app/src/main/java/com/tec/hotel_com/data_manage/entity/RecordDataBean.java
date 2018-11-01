package com.tec.hotel_com.data_manage.entity;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseCell;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/8/7 15:35
 * 邮箱：771548229@qq..com
 */
public class RecordDataBean extends RVBaseCell {

    private List<RecordDataItemInfo> mDataItemInfos;
    public int dateType = -1;//0为日数据，1为月数据，2为年数据
    private int year;
    private int curMonth;
    private int day;

    public RecordDataBean() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_data_rv_layout, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {
        final RecyclerView recordRvItemRv = (RecyclerView) holder.getView(R.id.record_data_rv);
        if (dateType == 0) {
            holder.setText(R.id.dateType, curMonth + "月" + day + "日");
        } else if (dateType == 1) {
            holder.setText(R.id.dateType, year + "年" + curMonth + "月");

        } else if (dateType == 2) {
            holder.setText(R.id.dateType, year + "年");

        }


        recordRvItemRv.setLayoutManager(new LinearLayoutManager(context));
        recordRvItemRv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        final RVBaseAdapter<RecordDataItemInfo> roomItemAdapter = new RVBaseAdapter<RecordDataItemInfo>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        roomItemAdapter.setData(mDataItemInfos);
        recordRvItemRv.setAdapter(roomItemAdapter);

        final ImageView openOrCloseImg = holder.getImageView(R.id.openOrClose);
        openOrCloseImg.setSelected(true);
        openOrCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openOrCloseImg.isSelected()) {
                    openOrCloseImg.setSelected(false);
                    recordRvItemRv.setVisibility(View.VISIBLE);
                    if (dateType == 0) {
                        requestDayDate(roomItemAdapter);
                    } else if (dateType == 1) {
                        requestMonthDate(roomItemAdapter);
                    } else if (dateType == 2) {
                        requestYearDate(roomItemAdapter);
                    }
                } else {
                    openOrCloseImg.setSelected(true);
                    recordRvItemRv.setVisibility(View.GONE);
                }
            }
        });
        recordRvItemRv.setVisibility(View.GONE);
    }

    private void requestDayDate(final RVBaseAdapter<RecordDataItemInfo> roomItemAdapter) {

        String date = getDateString();
        Map<String, Object> parame = new HashMap<>();
        parame.put("date", date);
        HttpProxy.obtain().get(PlatformContans.Statistics.countBydayForHotelApp, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {

                LogUtil.Log("countBydayForHotelApp", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        List<RecordDataItemInfo> list = new ArrayList<>();
                        Gson gson = new Gson();
                        int length = data.length();
                        RecordDataItemInfo itemInfo = new RecordDataItemInfo();
                        itemInfo.setRoomtypeName("房型");
                        itemInfo.setOccupancyRate("入住率");
                        itemInfo.setOrderNumSum("订单数");
                        itemInfo.setPeopleNum("入住人数");
                        list.add(itemInfo);

                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            RecordDataItemInfo bean = gson.fromJson(item.toString(), RecordDataItemInfo.class);
                            list.add(bean);
                        }
                        roomItemAdapter.setDataByRemove(list);
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


    private void requestMonthDate(final RVBaseAdapter<RecordDataItemInfo> roomItemAdapter) {

        String date = getDateString();
        Log.d("requestMonthDate", "requestMonthDate: " + date);
        Map<String, Object> parame = new HashMap<>();
        parame.put("date", date);
        HttpProxy.obtain().get(PlatformContans.Statistics.countBymonthForHotelApp, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {

                LogUtil.Log("countBydayForHotelApp", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        List<RecordDataItemInfo> list = new ArrayList<>();
                        Gson gson = new Gson();
                        int length = data.length();
                        RecordDataItemInfo itemInfo = new RecordDataItemInfo();
                        itemInfo.setRoomtypeName("房型");
                        itemInfo.setOccupancyRate("入住率");
                        itemInfo.setOrderNumSum("订单数");
                        itemInfo.setPeopleNum("入住人数");
                        list.add(itemInfo);

                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            RecordDataItemInfo bean = gson.fromJson(item.toString(), RecordDataItemInfo.class);
                            list.add(bean);
                        }
                        roomItemAdapter.setDataByRemove(list);
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

    private void requestYearDate(final RVBaseAdapter<RecordDataItemInfo> roomItemAdapter) {
        String date = getDateString();
        Map<String, Object> parame = new HashMap<>();
        parame.put("date", date);
        HttpProxy.obtain().get(PlatformContans.Statistics.countByyearForHotelApp, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {

                LogUtil.Log("countBydayForHotelApp", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        List<RecordDataItemInfo> list = new ArrayList<>();
                        Gson gson = new Gson();
                        int length = data.length();
                        RecordDataItemInfo itemInfo = new RecordDataItemInfo();
                        itemInfo.setRoomtypeName("房型");
                        itemInfo.setOccupancyRate("入住率");
                        itemInfo.setOrderNumSum("订单数");
                        itemInfo.setPeopleNum("入住人数");
                        list.add(itemInfo);

                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            RecordDataItemInfo bean = gson.fromJson(item.toString(), RecordDataItemInfo.class);
                            list.add(bean);
                        }
                        roomItemAdapter.setDataByRemove(list);
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

    public List<RecordDataItemInfo> getDataItemInfos() {
        return mDataItemInfos;
    }

    public void setDataItemInfos(List<RecordDataItemInfo> dataItemInfos) {
        mDataItemInfos = dataItemInfos;
    }

    public String getDateString() {
        String date = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String yStr = format.format(new Date());
        String mStr = getFormatString(curMonth);
        if (dateType == 0) {
            String dStr = getFormatString(day);
            date = yStr + "-" + mStr + "-" + dStr;
        } else if (dateType == 1) {
            date = year + "-" + mStr;
        } else if (dateType == 2) {
            date = year + "";
        }


        return date;
    }

    public String getFormatString(int date) {
        String result = "";

        if (date < 10) {
            result = "0" + date;
        } else {
            result = "" + date;

        }
        return result;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCurMonth() {
        return curMonth;
    }

    public void setCurMonth(int curMonth) {
        this.curMonth = curMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public static class RecordDataItemInfo extends RVBaseCell {


        private String roomtypeId;
        private String roomtypeName;//房型
        private String occupancyRate;//入住率
        private String orderNumSum;//订单数
        private String peopleNum;//入住人数


        public RecordDataItemInfo() {
            super(null);
        }

        @Override
        public int getItemType() {
            return 0;
        }

        @Override
        public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_data_item_rv_layout, parent, false);
            return new RVBaseViewHolder(view);
        }


        @Override
        public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {
            holder.setText(R.id.roomtypeName, roomtypeName);
            String result = "";
            try {
                double value = Double.parseDouble(occupancyRate);
                result = (value * 100) + "%";
            } catch (Exception e) {
                result = "入住率";
            }
            holder.setText(R.id.occupancyRate, result);
            holder.setText(R.id.orderNumSum, orderNumSum);
            holder.setText(R.id.peopleNum, peopleNum);
        }


        public String getRoomtypeId() {
            return roomtypeId;
        }

        public void setRoomtypeId(String roomtypeId) {
            this.roomtypeId = roomtypeId;
        }

        public String getRoomtypeName() {
            return roomtypeName;
        }

        public void setRoomtypeName(String roomtypeName) {
            this.roomtypeName = roomtypeName;
        }

        public String getOccupancyRate() {
            return occupancyRate;
        }

        public void setOccupancyRate(String occupancyRate) {
            this.occupancyRate = occupancyRate;
        }

        public String getOrderNumSum() {
            return orderNumSum;
        }

        public void setOrderNumSum(String orderNumSum) {
            this.orderNumSum = orderNumSum;
        }

        public String getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(String peopleNum) {
            this.peopleNum = peopleNum;
        }
    }
}
