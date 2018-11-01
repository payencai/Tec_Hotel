package com.tec.hotel_com.room_manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseFragment;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.eventBean.InformHomeRefresh;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：凌涛 on 2018/8/6 11:03
 * 邮箱：771548229@qq..com
 */
public class HomeFragment2 extends BaseFragment {

    @BindView(R.id.leftText)
    TextView leftText;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mineBtn)
    ImageView mineBtn;
    @BindView(R.id.roomTypeRv)
    RecyclerView roomTypeRv;
    private RVBaseAdapter<RoomInfo> mBaseAdapter;



    @Override
    protected int getContentId() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MineActivity.class));
            }
        });
        title.setText("酒店");
        leftText.setVisibility(View.VISIBLE);
        leftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoomActivity.startAddRoomOrChange(getContext(), 1, null);
            }
        });

        mBaseAdapter = new RVBaseAdapter<RoomInfo>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        roomTypeRv.setLayoutManager(new LinearLayoutManager(getContext()));
        roomTypeRv.setAdapter(mBaseAdapter);
        requestData();
    }

    @Subscribe
    public void refreshUI(InformHomeRefresh refresh) {
        requestData();
    }

    private void requestData() {

        String token = App.getInstance().getUserEntity().getToken();
        HttpProxy.obtain().get(PlatformContans.Roomtype.getMine, token, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("getMine", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.getString("message");
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        Gson gson = new Gson();
                        List<RoomInfo> list = new ArrayList<>();
                        JSONArray data = object.getJSONArray("data");
                        int length = data.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject item = data.getJSONObject(i);
                            RoomInfo bean = gson.fromJson(item.toString(), RoomInfo.class);
                            list.add(bean);
                        }
                        mBaseAdapter.setDataByRemove(list);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
