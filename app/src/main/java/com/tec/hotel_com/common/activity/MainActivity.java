package com.tec.hotel_com.common.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tec.hotel_com.App;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.bean.UserEntity;
import com.tec.hotel_com.data_manage.fragment.DataFragment;
import com.tec.hotel_com.order_manage.fragment.OrderManagerFragment;
import com.tec.hotel_com.room_manage.fragment.HomeFragment2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_iv_home)
    ImageView mainIvHome;
    @BindView(R.id.main_tv_home)
    TextView mainTvHome;
    @BindView(R.id.main_iv_order)
    ImageView mainIvOrder;
    @BindView(R.id.main_tv_order)
    TextView mainTvOrder;
    @BindView(R.id.main_iv_data)
    ImageView mainIvData;
    @BindView(R.id.main_tv_data)
    TextView mainTvData;
    private List<Fragment> fragments;
    private FragmentManager fm;


    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fm = getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment2());
        fragments.add(new OrderManagerFragment());
//        fragments.add(new UserOrderFragment());
        fragments.add(new DataFragment());


        for (Fragment fragment : fragments) {
            if (!fragment.isAdded())
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        //显示主页
        resetStateForTagbar(R.id.main_home);
        hideAllFragment();
        showFragment(0);
    }
    private void resetStateForTagbar(int viewId) {
        selected();
        if (viewId == R.id.main_home) {
            mainIvHome.setSelected(true);
            mainTvHome.setSelected(true);
            return;
        }
        if (viewId == R.id.main_order) {
            mainIvOrder.setSelected(true);
            mainTvOrder.setSelected(true);
            return;
        }
        if (viewId == R.id.main_data) {
            mainIvData.setSelected(true);
            mainTvData.setSelected(true);
            return;
        }

    }
    //重置所有文本的选中状态
    public void selected() {
        mainIvHome.setSelected(false);
        mainIvOrder.setSelected(false);
        mainIvData.setSelected(false);

        mainTvHome.setSelected(false);
        mainTvOrder.setSelected(false);
        mainTvData.setSelected(false);

    }
    private void hideAllFragment() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    private void showFragment(int position) {
        fm.beginTransaction().show(fragments.get(position)).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.main_home, R.id.main_order, R.id.main_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_home:
                resetStateForTagbar(R.id.main_home);
                hideAllFragment();
                showFragment(0);
                break;
            case R.id.main_order:
                resetStateForTagbar(R.id.main_order);
                hideAllFragment();
                showFragment(1);
                break;
            case R.id.main_data:
//                if (isLogin()) {
                    resetStateForTagbar(R.id.main_data);
                    hideAllFragment();
                    showFragment(2);
//                }
                break;
        }
    }
    /**
     * 是否登录
     *
     * @return
     */
    private boolean isLogin() {
        UserEntity userEntity2 = App.getInstance().getUserEntity();
        if (userEntity2 == null) {
            resetStateForTagbar(R.id.main_home);
            hideAllFragment();
            showFragment(0);
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        }
        return true;
    }
}
