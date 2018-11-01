package com.tec.hotel_com.common.activity;

import android.widget.TextView;


import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AgreementActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getContentId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected void initView() {
        title.setText("新旅盟协议");
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
