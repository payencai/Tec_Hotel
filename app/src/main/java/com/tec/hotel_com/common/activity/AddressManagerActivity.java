package com.tec.hotel_com.common.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.bean.WebViewAddressBean;
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

public class AddressManagerActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.address)
    TextView addressText;
    @BindView(R.id.doorNumString)
    EditText doorNumString;

    private static int ADDRESS_REQUEST_CODE = 1 << 1;
    private WebViewAddressBean mAddressBean;
    private KyLoadingBuilder mSaveAddressView;

    @Override
    protected int getContentId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        String addressDetail = intent.getStringExtra("addressDetail");
        try {
            addressText.setText(address);
            doorNumString.setText(addressDetail);
        } catch (Exception e) {

        }

        title.setText("选择地址");

    }

    @OnClick({R.id.back, R.id.save, R.id.selectAddressLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                if (mAddressBean == null) {
                    ToaskUtil.showToast(this, "请选择地址");
                    return;
                }
                mSaveAddressView = openLoadView("");
                updataAddress();
                break;
            case R.id.selectAddressLayout:
//                SelectAddressActivity.startSelectAddressActivity(this, "address", RQUEST_ADDRESS_CODE, consignSite.getText().toString());
                AddressWebViewActivity.startWebView(this, "http://120.79.176.228:8080/gaote-web/map/index.html", ADDRESS_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == ADDRESS_REQUEST_CODE) {
                String address = data.getStringExtra("address");
                mAddressBean = new Gson().fromJson(address, WebViewAddressBean.class);
                addressText.setText(mAddressBean.getPoiaddress());
            }
        }
    }

    private void updataAddress() {

        Map<String, Object> parame = new HashMap<>();
        WebViewAddressBean.LatlngBean latlng = mAddressBean.getLatlng();
        parame.put("longitude", latlng.getLng());
        parame.put("latitude", latlng.getLat());
        parame.put("address", mAddressBean.getPoiaddress());
        String detailString = doorNumString.getEditableText().toString().replace(" ", "");
        parame.put("addressDetail", detailString);
        HttpProxy.obtain().post(PlatformContans.Hotel.updateLocation, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                closeLoadView(mSaveAddressView);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        ToaskUtil.showToast(AddressManagerActivity.this, "保存成功");
                        EventBus.getDefault().post(new UpdateMineUI());
                        finish();
                    } else {
                        ToaskUtil.showToast(AddressManagerActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                closeLoadView(mSaveAddressView);

            }
        });
    }
}
