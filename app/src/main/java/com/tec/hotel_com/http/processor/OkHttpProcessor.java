package com.tec.hotel_com.http.processor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import com.tec.hotel_com.App;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.http.IHttpProcessor;
import com.tec.hotel_com.utils.NetworkUtil;
import com.tec.hotel_com.utils.ToaskUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by LT on 2017/12/12.
 */

public class OkHttpProcessor implements IHttpProcessor {

    public Handler mHandler = new Handler(Looper.myLooper());
    //{"resultCode":6666,"message":"登录信息已过期,请重新登录"}

    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new TokenInterceptor())
            .build();

    @Override
    public void post(String url, Map<String, Object> bodyParams, final ICallBack callBack) {
        FormBody body = buildBody(bodyParams);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnLtSuccess(result);
                    }
                });
            }
        });

    }


    @Override
    public void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, final ICallBack callBack) {
        FormBody body = buildBody(bodyParams);
        final Request request = addHeaders(headParams).post(body).url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.filtrationSuccess(result, request);
                    }
                });
            }
        });
    }

    @Override
    public void post(String url, String tokenValue, Map<String, Object> bodyParams, ICallBack callBack) {
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", tokenValue);
        this.post(url, tokenMap, bodyParams, callBack);
    }

    @Override
    public void post(String url, String tokenvalue, String jsonString, final ICallBack callBack) {
        MediaType jsonType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody body = RequestBody.create(jsonType, jsonString);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", tokenvalue);
        final Request request = addHeaders(tokenMap).url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.filtrationSuccess(result, request);
                    }
                });
            }
        });
    }


    @Override
    public void get(String url, Map<String, Object> headParams, final ICallBack callBack) {
        String newUrl = appendParams(url, headParams);
        Log.d("metchantdata", "get: " + newUrl);
        Request request = new Request.Builder().url(newUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnLtSuccess(result);
                    }
                });
            }
        });
    }

    @Override
    public void get(String url, String tokenValue, Map<String, Object> headParams, final ICallBack callBack) {
        String newUrl = appendParams(url, headParams);
        final Request request = addTokenHeader(tokenValue).url(newUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        callBack.OnSuccess(request);
                        callBack.filtrationSuccess(result, request);
                    }
                });
            }
        });

    }

    @Override
    public void get(String url, String token, final ICallBack callBack) {
        final Request request = addTokenHeader(token).url(url).build();
        request.url();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        hintFailure();
                        callBack.onLtFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        callBack.OnSuccess(request);
                        callBack.filtrationSuccess(result, request);
                    }
                });
            }
        });

    }

    private FormBody buildBody(Map<String, Object> bodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (bodyMap == null) {
            return builder.build();
        }

        for (String key : bodyMap.keySet()) {
            builder.add(key, bodyMap.get(key) + "");
        }
        return builder.build();
    }

    private Request.Builder addHeaders(Map<String, Object> headMap) {
        Request.Builder builder = new Request.Builder();
        if (headMap == null || headMap.size() == 0) {
            return builder;
        }
        for (String key : headMap.keySet()) {
            builder.addHeader(key, headMap.get(key) + "");
        }
        return builder;
    }

    private Request.Builder addTokenHeader(String token) {
        Request.Builder builder = new Request.Builder();
        if (TextUtils.isEmpty(token)) {
            return builder;
        }
        builder.addHeader("token", token);
        return builder;
    }

    private static String appendParams(String url, Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        StringBuffer result = new StringBuffer();
        result.append(url + "?");
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = params.get(key) + "";
            result.append(buildKeyValue(key, value, false));
            result.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = params.get(tailKey) + "";
        result.append(buildKeyValue(tailKey, tailValue, true));
        return result.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }


    private void hintFailure() {
        Context context = App.getContext();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            ToaskUtil.showToast(context, "当前设备无可用网络，请检查网络");
        } else {
            ToaskUtil.showToast(context, "数据获取失败，请重新再试");
        }
    }
}
