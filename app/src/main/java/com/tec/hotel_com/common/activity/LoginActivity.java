package com.tec.hotel_com.common.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.JPush.JpushConfig;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.bean.UserEntity;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.newhotel.HomeActivity;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.utils.UserInfoSharedPre;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.submit)
    Button submit;



    @BindView(R.id.find_password_text)
    TextView findPasswordBtn;
    @BindView(R.id.agreement)
    TextView agreement;

    private KyLoadingBuilder mLoginLoadView;

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        Intent intent = getIntent();
        boolean isUpdatePsw = intent.getBooleanExtra("isUpdatePsw", false);
        if (isUpdatePsw) {
            ToaskUtil.showToast(this, "修改密码成功，请重新登录");
        }
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        String username = (String) intance.getUserInfoFiledValue("username");
        String password = (String) intance.getUserInfoFiledValue("password");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            requestLogin(username, password);
        }

        agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }


    @OnClick({R.id.submit,R.id.find_password_text, R.id.apply_text, R.id.agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_password_text:
                startActivity(new Intent(this, FindPasswordActivity.class));
                break;
            case R.id.submit:
                String acc = account.getEditableText().toString().replace(" ", "");
                String psw = password.getEditableText().toString().replace(" ", "");
                if (App.ISDEBUG) {
                    acc = "18408889500";
                    psw = "123456";
                }

                if (checkFrom(acc, psw)) {
                    requestLogin(acc, psw);
                }
                break;
            case R.id.apply_text:
                WebViewActivity.starUi(this, "www.baidu.com", "垃圾");
                break;
            case R.id.agreement:
                startActivity(new Intent(this, AgreementActivity.class));
                break;
        }

    }

    private void requestLogin(String acc, final String psw) {
        submit.setEnabled(false);
        mLoginLoadView = openLoadView("");
        Map<String, Object> parame = new HashMap<>();
        parame.put("username", acc);
        parame.put("password", psw);
        HttpProxy.obtain().post(PlatformContans.HotelUser.login, parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                submit.setEnabled(true);
                closeLoadView(mLoginLoadView);
                LogUtil.Log("loginInfo", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int resultCode = jsonObject.optInt("resultCode");
                    String message = jsonObject.getString("message");
                    if (resultCode == 0) {
                        String data = jsonObject.optJSONObject("data").toString();
                        Gson gson = new Gson();
                        App.isLogin=true;
                        UserEntity entity = gson.fromJson(data.toString(), UserEntity.class);
                        entity.setPassword(psw);
                        JpushConfig.getInstance().setAlias(entity.getSystemId());
                        JpushConfig.getInstance().setTag(entity.getSystemId());
                        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(LoginActivity.this);
                        intance.saveUserInfo(entity);
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        ToaskUtil.showToast(LoginActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                submit.setEnabled(true);
                closeLoadView(mLoginLoadView);
            }
        });
    }

    private boolean checkFrom(String acc, String psw) {
        if (TextUtils.isEmpty(acc)) {
            ToaskUtil.showToast(this, "请输入账号");
            return false;
        }
        if (TextUtils.isEmpty(psw)) {
            ToaskUtil.showToast(this, "请输入密码");
            return false;
        }
        return true;
    }
}
