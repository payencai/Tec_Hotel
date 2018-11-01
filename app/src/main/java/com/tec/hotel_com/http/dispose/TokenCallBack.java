package com.tec.hotel_com.http.dispose;


import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/7/12 17:56
 * 邮箱：771548229@qq..com
 */
public abstract class TokenCallBack extends ICallBack {

    private static final String url = "http://47.106.164.34/memen/useuser/login";

    public abstract void OnTokenSuccess(String result);

    public abstract void OnTokenFailure(String result);

    public int count = 0;

    @Override
    public void OnLtSuccess(String result) {
        LogUtil.Log(result);
        try {
            JSONObject object = new JSONObject(result);
            int resultCode = object.getInt("resultCode");
            if (resultCode == 0) {
                count++;
                if (count >= 3) {
                    count = 0;
                    OnTokenFailure("登录失败");
                    return;
                }
                requestLogin(url, "17306676089", "123456");
            } else {
                OnTokenSuccess(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLtFailure(String error) {
        OnTokenFailure(error);
    }

    private void requestLogin(String url, String tel, final String psw) {
        Map<String, Object> params = new HashMap<>();
        params.put("telephone", tel);
        params.put("password", psw);
        params.put("equipment", "deviceOnlyId");
//        HttpProxy.obtain().get(url, params, this);

    }
}
