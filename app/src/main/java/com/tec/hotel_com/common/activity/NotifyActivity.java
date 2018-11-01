package com.tec.hotel_com.common.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.bean.Notify;
import com.tec.hotel_com.common.rv.abs.AbsBaseActivity;
import com.tec.hotel_com.common.rv.base.Cell;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NotifyActivity extends AbsBaseActivity<Notify> {

    private int page = 1;
    private boolean isLoadMore = false;

    @Override
    public void onRecyclerViewInitialized() {
        addDividerItem(0);
        page = 1;
        isLoadMore = false;
        requestData();
    }

    @Override
    public void onPullRefresh() {
        page = 1;
        isLoadMore = false;
        requestData();
    }

    @Override
    public void onLoadMore() {
        page++;
        isLoadMore = true;
        requestData();
    }

    @Override
    protected List<Cell> getCells(List<Notify> list) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    private void requestData() {
        Map<String, Object> parame = new HashMap<>();
        parame.put("page", page);

        HttpProxy.obtain().get(PlatformContans.Hotel.getNotice, App.getInstance().getUserEntity().getToken(),parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isLoadMore) {
                    mBaseAdapter.hideLoadMore();
                }
                Log.e("notify", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray beanList = data.getJSONArray("beanList");
                        int length = beanList.length();
                        List<Notify> list = new LinkedList<>();
                        Gson gson = new Gson();
                        int pageNum = data.getInt("pageNum");

                        for (int i = 0; i < length; i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            Notify bean = gson.fromJson(item.toString(), Notify.class);
                            list.add(bean);
                        }
                        if (list.size() == 0 && pageNum != 1) {
                            //ToaskUtil.showToast(NotifyActivity.this, "真的到底了");
                            return;
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            mBaseAdapter.setData(list);
                        } else {
                            mBaseAdapter.setDataByRemove(list);
                        }
                    } else {
                        ToaskUtil.showToast(NotifyActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isLoadMore) {
                    mBaseAdapter.hideLoadMore();
                }
            }
        });

    }

    @Override
    public View addToolbar() {
        View view = LayoutInflater.from(this).inflate(R.layout.toobar_head_layout, null);
        view.findViewById(R.id.back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.title)).setText("通知中心");

        return view;
    }
}
