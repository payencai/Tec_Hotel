package com.tec.hotel_com.common.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.utils.ToaskUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;

import butterknife.BindView;

public class AddressWebViewActivity extends BaseActivity {

    @BindView(R.id.webViewContainer)
    FrameLayout webViewContainer;

    private String mUrl;
    private int mRequestCode;
    private WebView webView;

    public static void startWebView(Activity context, String url, int requestCode) {
        Intent intent = new Intent(context, AddressWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("requestCode", requestCode);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_address_web_view;
    }

    @Override
    protected void initView() {

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mRequestCode = intent.getIntExtra("requestCode", -1);
        if (TextUtils.isEmpty(mUrl)) {
            ToaskUtil.showToast(this, "地址不能为空");
            finish();
            return;
        }
        if (mRequestCode < 0) {
            ToaskUtil.showToast(this, "请求码不能空");
            finish();
            return;
        }

        webView = new WebView(getApplicationContext());
        webViewContainer.addView(webView);


        WebSettings ws = webView.getSettings();
        //允许javascript执行
        ws.setJavaScriptEnabled(true);
        ws.setDefaultTextEncodingName("UTF-8");
        webView.loadUrl(mUrl);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //开始加载网页时回调
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("onPageFinished", "------------:" + url);
                if (url.startsWith("http://www.test.com/?loc=")) {
                    try {
                        String[] split = url.split("loc=");
                        String s = split[1];
                        String strUTF8 = URLDecoder.decode(s, "UTF-8");
                        Intent intent = new Intent();
                        intent.putExtra("address", strUTF8);
                        setResult(mRequestCode, intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    webView.stopLoading();
                }
            }

            //网页加载结束时回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("onPageFinished", "onPageFinished: " + url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("google.sang", "onReceivedTitle: " + title);
            }

        });
    }


    @Override
    public void onBackPressed() {
        //如果网页可以后退，则网页后退
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中
            // if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再 // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading(); // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();


    }
}
