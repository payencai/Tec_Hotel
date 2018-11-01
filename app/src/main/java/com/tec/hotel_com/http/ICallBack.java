package com.tec.hotel_com.http;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.common.activity.LoginActivity;
import com.tec.hotel_com.common.bean.UserEntity;
import com.tec.hotel_com.http.processor.OkHttpProcessor;
import com.tec.hotel_com.manager.ActivityManager;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.NetworkUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.utils.UserInfoSharedPre;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/29.
 */

public abstract class ICallBack {

//    private static final String url = PlatformContans.rootUrl;

    public int count = 0;
    protected Request request;

    public Handler mHandler = new Handler();

    public abstract void OnLtSuccess(String result);

    public abstract void onLtFailure(String error);

    public void filtrationSuccess(String result, Request request) {
        LogUtil.Log("filtrationSuccess", result);
        try {
            JSONObject object = new JSONObject(result);
            int resultCode = object.getInt("resultCode");
            //如果是token过期,重新请求token
            //这里可能存在重复请求过程（死循环请求）
            if (resultCode == 6666) {
                count++;
                if (count > 3) {
                    count = 0;
                    onLtFailure("获取token失败");
                    return;
                }
                this.request = request;
                loginObtainUserInfo();
            } else {//请求成功的正常数据
                count = 0;
                OnLtSuccess(result);//如果不是重新获取token数据的，直接返回
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginObtainUserInfo() {
        final App instance = App.getInstance();
        UserEntity entity = instance.getUserEntity();
        String password = entity.getPassword();
        if (TextUtils.isEmpty(password) || entity == null) {
            ActivityManager.getInstance().finishAllActivity();
            instance.startActivity(new Intent(instance, LoginActivity.class));
            return;
        }
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("username", entity.getUsername());
        hasMap.put("password", entity.getPassword());
        HttpProxy.obtain().post(PlatformContans.HotelUser.login, hasMap, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("loginInfo", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int resultCode = jsonObject.optInt("resultCode");
                    if (resultCode == 0) {
                        String data = jsonObject.optJSONObject("data").toString();
                        Gson gson = new Gson();
                        UserEntity entity = gson.fromJson(data.toString(), UserEntity.class);
                        App application = App.getInstance();
                        String password = application.getUserEntity().getPassword();
                        entity.setPassword(password);
                        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(application);
                        intance.saveUserInfo(entity);
                        //重新登录成功，重新获取token
                        retrieveData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLtFailure(String error) {
                if (!NetworkUtil.isNetworkAvailable(App.getContext())) {
                    ToaskUtil.showToast(App.getContext(), "当前设备无可用网络，请检查。");
                } else {
                    ToaskUtil.showToast(App.getContext(), "数据请求失败，请重新再试。");
                }
            }
        });
    }


    /**
     * 重新获取数据
     */
    private void retrieveData() {
        Call call = OkHttpProcessor.client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String message = e.getMessage();
                        onLtFailure(message);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        OnLtSuccess(result);
                    }
                });
            }
        });
    }
}
