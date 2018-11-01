package com.tec.hotel_com.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;

import butterknife.BindView;


/**
 * 新闻详情页面(H5页面)
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webViewFrameLayout)
    FrameLayout webViewFrameLayout;



    private String mUrl = "http://www.baidu.com";
    private WebView webView;
    private String mTitle = "";

    /**
     * 启动打印页面
     *
     * @param url 要打开的网页
     */
    public static void starUi(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    private void initSetting() {
        WebSettings ws = webView.getSettings();
        //允许javascript执行
        ws.setJavaScriptEnabled(true);
        //加载一个服务端网页
        webView.loadUrl(mUrl);
        //加载一个本地网页
//        webView.loadUrl("file:///android_asset/jm/index.html");

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
            }

            //网页加载结束时回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
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
    protected int getContentId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void initView() {
        webView = new WebView(getApplicationContext());
        webViewFrameLayout.addView(webView);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://www.baidu.com";
        }
        mTitle = intent.getStringExtra("title");
        initSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}
