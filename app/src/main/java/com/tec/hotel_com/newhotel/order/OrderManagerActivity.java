package com.tec.hotel_com.newhotel.order;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.activity.MineActivity;
import com.tec.hotel_com.order_manage.adapter.OrderVpAdapter;
import com.tec.hotel_com.order_manage.fragment.LxsOrderFragment;
import com.tec.hotel_com.order_manage.fragment.UserOrderFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderManagerActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.order_viewpager)
    ViewPager orderViewpager;

    FragmentActivity mActivity;
    @BindView(R.id.account_paid_text)
    TextView accountPaidText;
    @BindView(R.id.account_paid_view)
    View accountPaidView;
    @BindView(R.id.obligation_text)
    TextView obligationText;
    @BindView(R.id.obligation_view)
    View obligationView;
    @BindView(R.id.mine)
    ImageView mine;
    private List<Fragment> mFragments;
    private OrderVpAdapter mVpAdapter;

    @BindView(R.id.lineTablayout)
    TabLayout lineTablelayout;

    private static final String[] titles = new String[]{"用户订单", "旅行社订单"};


    @Override
    protected int getContentId() {
        return R.layout.activity_order_manager;
    }

    @Override
    protected void initView() {
        back.setVisibility(View.GONE);
        mine.setVisibility(View.VISIBLE);
        title.setText("订单");
        initFragment();
    }
    public String getType(){
        String type="";
        type=getIntent().getStringExtra("type");
        return type;
    }
    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new UserOrderFragment());
        mFragments.add(new LxsOrderFragment());

        mVpAdapter = new OrderVpAdapter(getSupportFragmentManager(), mFragments,titles);
        orderViewpager.setAdapter(mVpAdapter);
        lineTablelayout.setupWithViewPager(orderViewpager);
        orderViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", "onPageSelected: " + position);
                setFragmentUI(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setFragmentUI(0);
    }


    @OnClick({R.id.account_paid_layout, R.id.obligation_layout, R.id.mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine:
                startActivity(new Intent(this, MineActivity.class));
                break;
            case R.id.account_paid_layout:
                setFragmentUI(0);

                break;
            case R.id.obligation_layout:
                setFragmentUI(1);
                break;
        }
    }

    private void setFragmentUI(int type) {
        orderViewpager.setCurrentItem(type);
        switch (type) {
            case 0:
                accountPaidText.setSelected(true);
                accountPaidView.setVisibility(View.VISIBLE);
                obligationText.setSelected(false);
                obligationView.setVisibility(View.GONE);
                break;
            case 1:
                accountPaidText.setSelected(false);
                accountPaidView.setVisibility(View.GONE);
                obligationText.setSelected(true);
                obligationView.setVisibility(View.VISIBLE);
                break;
        }

    }
}
