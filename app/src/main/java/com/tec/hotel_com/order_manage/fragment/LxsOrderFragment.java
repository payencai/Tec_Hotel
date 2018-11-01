package com.tec.hotel_com.order_manage.fragment;

import android.util.Log;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.common.rv.abs.AbsBaseFragment;
import com.tec.hotel_com.common.rv.base.Cell;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.newhotel.order.OrderManagerActivity;
import com.tec.hotel_com.order_manage.entity.LxsOrderInfo;
import com.tec.hotel_com.order_manage.entity.UserOrderInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/8/7 10:31
 * 邮箱：771548229@qq..com
 */
public class LxsOrderFragment extends AbsBaseFragment<LxsOrderInfo> {

    private int page = 1;
    private boolean isLoadMore = false;

    @Override
    public void onRecyclerViewInitialized() {
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
    public void startLoadData() {

    }

    @Override
    protected List<Cell> getCells(List<LxsOrderInfo> list) {
        return null;
    }

    private void requestData() {
        Map<String, Object> parame = new HashMap<>();
        parame.put("page", page);
        OrderManagerActivity activity= (OrderManagerActivity) getActivity();
        String status=activity.getType();
        if(status.equals("0")){
        }else{
            parame.put("status",status);
            Log.e("status",status);
        }
        HttpProxy.obtain().get(PlatformContans.Hotel.getLxsOrdersForHotel, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("lxsOrderTag", result);
                mSwipeRefreshLayout.setRefreshing(false);
                if (isLoadMore) {
                    mBaseAdapter.hideLoadMore();
                }

                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {

                        JSONObject data = object.getJSONObject("data");
                        JSONArray beanList = data.getJSONArray("beanList");
                        int length = beanList.length();
                        Gson gson = new Gson();
                        List<LxsOrderInfo> list = new ArrayList<>();

                        int pageNum = data.getInt("pageNum");
                        for (int i = 0; i < length; i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            LxsOrderInfo bean = gson.fromJson(item.toString(), LxsOrderInfo.class);
                            list.add(bean);
                        }
                        if (list.size() == 0 && pageNum != 1) {
                            ToaskUtil.showToast(getContext(), "真的到底了");
                            return;
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            mBaseAdapter.setData(list);
                        } else {
                            mBaseAdapter.setDataByRemove(list);
                        }
                    } else {
                        ToaskUtil.showToast(getContext(), message);
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
}
