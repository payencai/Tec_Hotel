package com.tec.hotel_com.room_manage.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.common.rv.abs.AbsBaseFragment;
import com.tec.hotel_com.common.rv.base.Cell;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：凌涛 on 2018/8/6 11:03
 * 邮箱：771548229@qq..com
 */
public class HomeFragment extends AbsBaseFragment<RoomInfo> {
    private  boolean isLoadMore;
    @Override
    public void onRecyclerViewInitialized() {
        requestData();
    }

    @Override
    public void onPullRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoadMore() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mBaseAdapter.hideLoadMore();
        isLoadMore = true;
        requestData();
    }

    @Override
    public void startLoadData() {

    }


    @Override
    protected List<Cell> getCells(List<RoomInfo> list) {
        return null;
    }

    @Override
    public View addToolbar() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.abs_toolbar_hotel_layout, null);
//        view.findViewById(R.id.mineBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), MineActivity.class));
//            }
//        });
//        TextView leftText = view.findViewById(R.id.leftText);
//        leftText.setVisibility(View.VISIBLE);
//        leftText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), AddRoomActivity.class));
//            }
//        });
        return view;
    }

    private void requestData() {

        if (isLoadMore) {
            isLoadMore = false;
            hideLoadMore();
            return;
        }

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

}
