package com.tec.hotel_com.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tec.hotel_com.R;
import com.tec.hotel_com.manager.ActivityManager;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseOnCreate(savedInstanceState);
        if (isFullShow()) {
            //去除标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (setStatusBarTransparency()) {
            //只有5.1以上的系统支持
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
        if (windowActionBar()) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(getContentId());
        ActivityManager.getInstance().pushActivity(this);
        mUnbinder = ButterKnife.bind(this);//使用butterknife
        initView();
    }

    protected void setBackgroundDrakValue(float value) {
        if (value > 1) {
            value = 1;
        }
        if (value < 0) {
            value = 0;
        }
        Window mWindow = getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = value;
        mWindow.setAttributes(params);

    }

    /**
     * @return 每个activity的布局id
     */
    protected abstract @LayoutRes
    int getContentId();

    /**
     * activity 初始化view
     */
    protected abstract void initView();

    //需要savedInstanceState的子类自己去重写
    protected void baseOnCreate(Bundle savedInstanceState) {

    }

    /**
     * @return 是否全屏显示
     */
    protected boolean isFullShow() {
        return false;
    }

    protected boolean windowActionBar() {
        return false;
    }

    /**
     * @return 是否使状态栏透明，默认是false
     */
    protected boolean setStatusBarTransparency() {
        return false;
    }

    /**
     * 添加自定义分割线
     *
     * @param id
     */
    protected void addDividerItem(RecyclerView recyclerView, @DrawableRes int id) {
        if (id == 0) {
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            return;
        }
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider));
        divider.setDrawable(ContextCompat.getDrawable(this, id));
        recyclerView.addItemDecoration(divider);
    }

    /**
     * 打开loading
     */
    protected KyLoadingBuilder openLoadView(String showText) {
        KyLoadingBuilder loadingBuilder = new KyLoadingBuilder(this);
        loadingBuilder.setIcon(R.mipmap.loading_32dp);
        if (TextUtils.isEmpty(showText)) {
            loadingBuilder.setText("正在加载中...");
        } else {
            loadingBuilder.setText(showText);
        }
        //builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        loadingBuilder.show();
        return loadingBuilder;
    }
    protected void closeLoadView(KyLoadingBuilder loadingBuilder) {
        if (loadingBuilder != null) {
            loadingBuilder.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
        mUnbinder.unbind();
    }


}
