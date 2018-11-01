package com.tec.hotel_com.http.dispose;

import com.google.gson.Gson;
import com.tec.hotel_com.http.ICallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/9/29.
 */

public abstract class HttpCallback<Result> extends ICallBack {
    @Override
    public void OnLtSuccess(String result) {
        Gson gson = new Gson();
        Class<?> cls = analysusClazInfo(this);
        Result objectResult = (Result) gson.fromJson(result, cls);
        onSuccess(objectResult);
    }

    public abstract void onSuccess(Result result);
    public abstract void onFailure2(String error);

    public static Class<?> analysusClazInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void onLtFailure(String error) {
        onFailure2(error);
    }
}
