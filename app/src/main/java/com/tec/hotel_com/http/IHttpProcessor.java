package com.tec.hotel_com.http;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/29.
 */

public interface IHttpProcessor {
    void post(String url, Map<String, Object> bodyParams, ICallBack callBack);

    //headParams就是头信息
    void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, ICallBack callBack);

    void post(String url, String tokenValue, Map<String, Object> bodyParams, ICallBack callBack);

    void post(String url, String tokenValue, String jsonString, ICallBack callBack);


    //没有token 所以不需要isRegainToken参数
    void get(String url, Map<String, Object> headParams, ICallBack callBack);

    //有token
    void get(String url, String tokenValue, Map<String, Object> headParams, ICallBack callBack);

    void get(String url, String token, ICallBack callBack);
}
