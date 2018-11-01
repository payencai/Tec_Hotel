package com.tec.hotel_com;

import android.app.Application;
import android.content.Context;

import com.tec.hotel_com.JPush.JpushConfig;
import com.tec.hotel_com.common.bean.UserEntity;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.processor.OkHttpProcessor;


/**
 * Created by pengying on 2017/3/9.
 */
public class App extends Application {
    private static App instance;
    private static Context context;
    private UserEntity mUserEntity;
    private static final String TAG = "App--->>>";
    public static final boolean ISDEBUG = true;//开启调试模式
    public static  boolean isLogin;
    //在自己的Application中添加如下代码
    @Override
    public void onCreate() {
        instance = this;
        context = this;
        JpushConfig.getInstance().initJpush();
        HttpProxy.init(new OkHttpProcessor());
        super.onCreate();
    }


    public void setUserInfo(UserEntity entity) {
        this.mUserEntity = entity;
    }

    public UserEntity getUserEntity() {
        return mUserEntity;
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

}
