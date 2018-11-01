package com.tec.hotel_com.newhotel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.DataType;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.activity.LoginActivity;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.common.activity.NotifyActivity;
import com.tec.hotel_com.common.rv.FullyLinearLayoutManager;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.data_manage.activity.DataRecordActivity;
import com.tec.hotel_com.data_manage.entity.DayDataInfo;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.http.dispose.HttpCallback;
import com.tec.hotel_com.newhotel.bean.HotelRoomType;
import com.tec.hotel_com.newhotel.order.OrderManagerActivity;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.activity.OneMouthDataActivity;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.WeekDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.pub)
    TextView pub;
    @BindView(R.id.unin)
    TextView tv_unin;
    @BindView(R.id.refund)
    TextView tv_refund;
    @BindView(R.id.all)
    TextView tv_all;
    @BindView(R.id.ll_unin)
    LinearLayout ll_unin;
    @BindView(R.id.ll_refund)
    LinearLayout ll_refund;
    @BindView(R.id.ll_all)
    LinearLayout ll_all;
    @BindView(R.id.rv_roomtype)
    RecyclerView rv_rootType;
    @BindView(R.id.mana)
    FrameLayout mana;
    @BindView(R.id.notify)
    FrameLayout notify;
    @BindView(R.id.WeekDataView)
    WeekDataView mWeekDataView;
    private RVBaseAdapter<HotelRoomType> mBaseAdapter;

    @Override
    protected int getContentId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {

        mBaseAdapter = new RVBaseAdapter<HotelRoomType>() {
            @Override
            protected void onViewHolderBound(final RVBaseViewHolder holder, final int position) {
                holder.getView(R.id.recent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HotelRoomType hotelRoomType=getData().get(position);
                        RoomInfo roomInfo=setRoomInfo(hotelRoomType);
                        OneMouthDataActivity.startOneMonthActivity(HomeActivity.this, roomInfo);
                        //Toast.makeText(HomeActivity.this,"fdfdf",Toast.LENGTH_LONG).show();
                    }
                });
                holder.getView(R.id.roomtype).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HotelRoomType hotelRoomType=getData().get(position);
                        AddRoomActivity.startAddRoomOrChange(HomeActivity.this, 3, setRoomInfo(hotelRoomType));
                    }
                });

            }
        };
        mBaseAdapter.setHasStableIds(true);
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, OrderManagerActivity.class);
                intent.putExtra("type","0");
                startActivity(intent);
            }
        });
        ll_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, OrderManagerActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
        ll_unin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, OrderManagerActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRecordActivity.startDtaRecordActivity(HomeActivity.this, DataType.DayData);
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRecordActivity.startDtaRecordActivity(HomeActivity.this, DataType.MonthDta);
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRecordActivity.startDtaRecordActivity(HomeActivity.this, DataType.YearData);
            }
        });
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRoomActivity.startAddRoomOrChange(HomeActivity.this, 1, null);
            }
        });
        mana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(HomeActivity.this, MineActivity.class));
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NotifyActivity.class));
            }
        });
        rv_rootType.setNestedScrollingEnabled(false);
        rv_rootType.setLayoutManager(new FullyLinearLayoutManager(this));
        requestData();
        requestWeekData();

    }
    private RoomInfo setRoomInfo(HotelRoomType hotelRoomType){
        RoomInfo roomInfo=new RoomInfo();
        roomInfo.setBathroom(hotelRoomType.getBathroom());
        roomInfo.setBedNum(hotelRoomType.getBedNum());
        roomInfo.setBedType(hotelRoomType.getBedType());
        roomInfo.setBreakfast(hotelRoomType.getBreakfast());
        roomInfo.setCreateTime(hotelRoomType.getCreateTime());
        roomInfo.setDescribe(hotelRoomType.getDescribe());
        roomInfo.setFloor(hotelRoomType.getFloor());
        roomInfo.setHotelId(hotelRoomType.getHotelId());
        roomInfo.setHotelPrice(hotelRoomType.getHotelPrice());
        roomInfo.setId(hotelRoomType.getId());

        roomInfo.setImage1(hotelRoomType.getImage1());
        roomInfo.setImage2(hotelRoomType.getImage2());
        roomInfo.setImage3(hotelRoomType.getImage3());
        roomInfo.setImage4(hotelRoomType.getImage4());
        roomInfo.setImage5(hotelRoomType.getImage5());

        roomInfo.setImage1Url(hotelRoomType.getImage1Url());
        roomInfo.setImage2Url(hotelRoomType.getImage2Url());
        roomInfo.setImage3Url(hotelRoomType.getImage3Url());
        roomInfo.setImage4Url(hotelRoomType.getImage4Url());
        roomInfo.setImage5Url(hotelRoomType.getImage5Url());
        roomInfo.setWindow(hotelRoomType.getWindow());
        roomInfo.setIntnet(hotelRoomType.getIntnet());
        roomInfo.setLiveNum(hotelRoomType.getLiveNum());
        roomInfo.setUpdateTime(hotelRoomType.getUpdateTime());
        roomInfo.setManagePrice(hotelRoomType.getManagePrice());
        roomInfo.setName(hotelRoomType.getName());
        roomInfo.setRegulations(hotelRoomType.getRegulations());
        roomInfo.setRoomNum(hotelRoomType.getRoomNum());
        roomInfo.setSequence(hotelRoomType.getSequence()+"");
        roomInfo.setSetOutAmount(hotelRoomType.getSetOutAmount()+"");
        roomInfo.setIsCanCancel(hotelRoomType.getIsCanCancel());
        roomInfo.setIsDeleted(hotelRoomType.getIsDeleted());
        roomInfo.setIsUseCoupon(hotelRoomType.getIsUseCoupon());
        roomInfo.setIsUsed(hotelRoomType.getIsUsed());
        return  roomInfo;
    }
    private void processData(List<DayDataInfo> list) {
        int[] args = new int[7];
        String[] weeks = new String[7];
        for (int i = 0; i < list.size(); i++) {
            DayDataInfo dayDataInfo = list.get(i);
            int amount = dayDataInfo.getAmount();
            args[i] = amount;
            String date = dayDataInfo.getDate();
            String substring = date.substring(5);
            weeks[i] = substring;
        }

        mWeekDataView.setWeekStr(weeks);
        mWeekDataView.setWeekCount(args);

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
    private void requestData() {
        String token = App.getInstance().getUserEntity().getToken();
        HttpProxy.obtain().get(PlatformContans.Hotel.getOrderNum, token, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                JSONObject object = null;
                try {

                    object = new JSONObject(result);
                    String message = object.getString("message");
                    int resultCode = object.getInt("resultCode");
                    if(resultCode==0){
                        Log.e("num",result);
                        JSONObject data=object.getJSONObject("data");
                        tv_unin.setText(data.getInt("unIn")+"");
                        tv_refund.setText(data.getInt("refund")+"");
                        tv_all.setText(data.getInt("all")+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLtFailure(String error) {

            }
        });

        HttpProxy.obtain().get(PlatformContans.Roomtype.getMine, token, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("room", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getString("message");
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        Gson gson = new Gson();
                        List<HotelRoomType> list = new ArrayList<>();
                        JSONArray data = object.getJSONArray("data");
                        int length = data.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            HotelRoomType bean = gson.fromJson(item.toString(), HotelRoomType.class);
                            list.add(bean);
                        }
                        //

                        rv_rootType.setAdapter(mBaseAdapter);
                        mBaseAdapter.setDataByRemove(list);
                    } else {
                        ToaskUtil.showToast(HomeActivity.this, message);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestData();
    }
}
