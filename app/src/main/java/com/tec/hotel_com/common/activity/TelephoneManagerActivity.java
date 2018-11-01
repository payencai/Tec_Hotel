package com.tec.hotel_com.common.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.eventBean.UpdateMineUI;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TelephoneManagerActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.telephone_edit)
    EditText telephoneEdit;
    @BindView(R.id.save)
    Button save;
    private KyLoadingBuilder mUpdateTelView;
    private String mTel;


    @Override
    protected int getContentId() {
        return R.layout.activity_telephone_manager;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mTel = intent.getStringExtra("tel");
        telephoneEdit.setText(mTel);
        title.setText("编辑电话号码");
    }

    @OnClick({R.id.back, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                String tel = telephoneEdit.getEditableText().toString().replace(" ", "");
                if (checkFrom(tel)) {
                    mUpdateTelView = openLoadView("");
                    updateTel(tel);
                }
                break;
        }
    }

    private boolean checkFrom(String tel) {
        if (TextUtils.isEmpty(tel)) {
            ToaskUtil.showToast(this, "电话不能为空");
            return false;
        }
        return true;
    }

    private void updateTel(String tel) {
        Map<String, Object> parame = new HashMap<>();
        parame.put("contactNum", tel);
        HttpProxy.obtain().post(PlatformContans.Hotel.updateContactNum, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                closeLoadView(mUpdateTelView);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        ToaskUtil.showToast(TelephoneManagerActivity.this, "更新成功");
                        EventBus.getDefault().post(new UpdateMineUI());
                        finish();
                    } else {
                        ToaskUtil.showToast(TelephoneManagerActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                closeLoadView(mUpdateTelView);

            }
        });
    }
}
