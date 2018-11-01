package com.tec.hotel_com.room_manage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.eventBean.UpMonthDate;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.order_manage.adapter.OrderVpAdapter;
import com.tec.hotel_com.room_manage.entity.DayInfo;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.room_manage.fragment.CurDataFragment;
import com.tec.hotel_com.room_manage.fragment.LastDataFragment;
import com.tec.hotel_com.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OneMouthDataActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.dataViewpager)
    ViewPager dataViewpager;

    private List<Fragment> mFragments;
    private OrderVpAdapter mVpAdapter;
    private RoomInfo mRoomInfo;

    public static void startOneMonthActivity(Context context, RoomInfo roomInfo) {
        Intent intent = new Intent(context, OneMouthDataActivity.class);
        intent.putExtra("roomInfo", roomInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_one_mouth_data;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mRoomInfo = (RoomInfo) intent.getSerializableExtra("roomInfo");
        if (mRoomInfo == null) {
            finish();
            return;
        }
        title.setText("房间数量");

        initFragment();
        reqeustData();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new CurDataFragment(mRoomInfo));
        mFragments.add(new LastDataFragment(mRoomInfo));

        mVpAdapter = new OrderVpAdapter(getSupportFragmentManager(), mFragments,null);
        dataViewpager.setAdapter(mVpAdapter);
    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void reqeustData() {
        Map<String, Object> parame = new HashMap<>();
        parame.put("roomtypeId", mRoomInfo.getId());
        HttpProxy.obtain().get(PlatformContans.Roomnum.getRoomnumWithDateByRoomType, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("roomtypeId", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        List<DayInfo> list = new ArrayList<>();
                        int length = data.length();
                        Gson gson = new Gson();
                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            DayInfo dayInfo = gson.fromJson(item.toString(), DayInfo.class);
                            list.add(dayInfo);
                        }

                        EventBus.getDefault().post(list);

                    } else {

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

    @Subscribe
    public void upMonthData(UpMonthDate monthDate) {
        reqeustData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
