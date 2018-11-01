package com.tec.hotel_com.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29.
 */

public class HttpProxy implements IHttpProcessor {
    private static HttpProxy _instance;
    private static IHttpProcessor mIHttpProcessor = null;

    public static HttpProxy obtain() {
        if (_instance == null) {
            synchronized (HttpProxy.class) {
                if (_instance == null) {
                    _instance = new HttpProxy();
                }
            }
        }
        return _instance;
    }

    private HttpProxy() {
    }

    public static void init(IHttpProcessor httpProcessor) {
        mIHttpProcessor = httpProcessor;
    }

    @Override
    public void post(String url, Map<String, Object> bodyParams, ICallBack callBack) {
        mIHttpProcessor.post(url, bodyParams, callBack);
    }

    @Override
    public void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, ICallBack callBack) {
        mIHttpProcessor.post(url, headParams, bodyParams, callBack);
    }

    @Override
    public void post(String url, String tokenValue, Map<String, Object> bodyParams, ICallBack callBack) {
        mIHttpProcessor.post(url, tokenValue, bodyParams, callBack);
    }

    @Override
    public void post(String url, String tokenValue, String jsonString, ICallBack callBack) {
        mIHttpProcessor.post(url, tokenValue, jsonString, callBack);
    }

    @Override
    public void get(String url, Map<String, Object> headParams, ICallBack callBack) {
        mIHttpProcessor.get(url, headParams, callBack);
    }

    @Override
    public void get(String url, String tokenValue, Map<String, Object> headParams, ICallBack callBack) {
//        String newUrl = appendParams(url, headParams);
        mIHttpProcessor.get(url, tokenValue, headParams, callBack);
    }


    @Override
    public void get(String url, String token, ICallBack callBack) {
        mIHttpProcessor.get(url, token, callBack);
    }


}
