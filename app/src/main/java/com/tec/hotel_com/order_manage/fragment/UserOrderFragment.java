package com.tec.hotel_com.order_manage.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.common.rv.abs.AbsBaseFragment;
import com.tec.hotel_com.common.rv.base.Cell;
import com.tec.hotel_com.eventBean.PhoneCallBack;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.newhotel.order.OrderManagerActivity;
import com.tec.hotel_com.order_manage.entity.UserOrderInfo;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
public class UserOrderFragment extends AbsBaseFragment<UserOrderInfo> {

    private int page = 1;
    private boolean isLoadMore = false;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    @Override
    public void onRecyclerViewInitialized() {
        EventBus.getDefault().register(this);
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
    protected List<Cell> getCells(List<UserOrderInfo> list) {
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
        HttpProxy.obtain().get(PlatformContans.Hotel.getUserOrdersForHotel, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("userOrderTag", result);
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
                        List<UserOrderInfo> list = new ArrayList<>();

                        int pageNum = data.getInt("pageNum");
                        for (int i = 0; i < length; i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            UserOrderInfo bean = gson.fromJson(item.toString(), UserOrderInfo.class);
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

//    @Override
//    public View addToolbar() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.toobar_head_layout, null);
//        ((TextView) view.findViewById(R.id.title)).setText("订单");
//        ImageView mineImg = (ImageView) view.findViewById(R.id.mine);
//        view.findViewById(R.id.back).setVisibility(View.GONE);
//        mineImg.setVisibility(View.VISIBLE);
//        mineImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), MineActivity.class));
//            }
//        });
//        return view;
//    }

    private String mTel;

    @Subscribe
    public void CallTel(PhoneCallBack phoneCallBack) {
        mTel = phoneCallBack.getTel();
        Log.d("CallTel", "CallTel: " + phoneCallBack.getTel());
        if (TextUtils.isEmpty(mTel)) {
            ToaskUtil.showToast(getContext(), "号码为空");
            return;
        }
        checkPower();
    }

    private void checkPower() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            callPhone();
        }
    }

    @SuppressLint("MissingPermission")
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + mTel);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                // Permission Denied
                Toast.makeText(getContext(), "权限拒绝", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
