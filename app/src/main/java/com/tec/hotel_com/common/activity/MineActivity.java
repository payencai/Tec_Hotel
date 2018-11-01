package com.tec.hotel_com.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.bean.HotelInfo;
import com.tec.hotel_com.common.recyclerDecoration.DividerGridItemDecoration;
import com.tec.hotel_com.common.recyclerDecoration.MyDividerItemDecoration;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.eventBean.UpdateMineUI;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.manager.ActivityManager;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.entity.RoomPhoto;
import com.tec.hotel_com.room_manage.entity.UpImageBean;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.utils.UserInfoSharedPre;
import com.tec.hotel_com.widget.CustomPopWindow;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.saveText)
    TextView saveText;

    @BindView(R.id.hotel_photo_rv)
    RecyclerView hotelPhotoRv;

    @BindView(R.id.scrollLayout)
    ScrollView scrollLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.synopsis)
    TextView synopsis;
    @BindView(R.id.out)
    TextView out;
    @BindView(R.id.in)
    TextView in;
    Handler mHandler = new Handler();
    private RVBaseAdapter<RoomPhoto> mAdapter;
    private List<RoomPhoto> mPhotoList = new ArrayList<>();
    private Set<String> innerOldImageKey = new HashSet<>();
    private Set<String> outerOldImageKey = new HashSet<>();
    //已选的图片
    public ArrayList<String> selected = new ArrayList<>();
    //上传成功的图片key
    public List<UpImageBean> upImgList = new ArrayList<>();
    private String upOuterStr = "";
    private String upInnerStr = "";
    //记录编辑的文字
    String recordEditString;
    public static final int REQUEST_CODE = 0;
    private HotelInfo mHotelInfo;
    private boolean isOut = true;

    @Override
    protected int getContentId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title.setText("管理中心");
        saveText.setVisibility(View.VISIBLE);
        saveText.setText("退出登录");

//        Glide.with(this).load(hotelInfo.getLogoUrl()).into(mMinePic);

//        scrollLayout.fullScroll(ScrollView.FOCUS_UP);
        hotelPhotoRv.setLayoutManager(new GridLayoutManager(this, 3));
        //设置分隔线
        hotelPhotoRv.addItemDecoration(new MyDividerItemDecoration(15));
        //设置增加或删除条目的动画
        hotelPhotoRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RVBaseAdapter<RoomPhoto>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

                final RoomPhoto roomPhoto = getData().get(position);
                roomPhoto.setOnDelClickListener(new RoomPhoto.OnDelClickListener() {
                    @Override
                    public void onClick(String key, int pos) {
                        Log.e("del",key);
                        if(isOut){
                            mPhotoList.remove(pos);
                            outerOldImageKey.remove(key);
                            mAdapter.setDataByRemove(mPhotoList);
                            upOuterStr="";
                            upLoadOuterImage();
                        }else{
                            mPhotoList.remove(pos);
                            innerOldImageKey.remove(key);
                            mAdapter.setDataByRemove(mPhotoList);
                            upInnerStr="";
                            upLoadInnerImage();
                        }
                    }
                });

            }

            @Override
            protected void onViewClick(RVBaseViewHolder holder, final int position) {
                if (!isLoadFinish) {
                    return;
                }
                ImageView delImg = holder.getImageView(R.id.del);
                final RoomPhoto roomPhoto = getData().get(position);
                Log.e("del","del");
//                delImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                    }
//                });
                roomPhoto.setOnDelClickListener(new RoomPhoto.OnDelClickListener() {
                    @Override
                    public void onClick(String key, int pos) {
                        Log.e("del","bbbb");

                    }
                });
            }
        };
        mAdapter.setData(mPhotoList);
        hotelPhotoRv.setAdapter(mAdapter);
        getImageCount();
        requestHotelInfo();
        getOuterImage();
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOut) {
                    isOut = true;
                    out.setTextColor(getResources().getColor(R.color.red));
                    in.setTextColor(getResources().getColor(R.color.color_333));
                    getOuterImage();
                }
            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOut) {
                    isOut = false;
                    in.setTextColor(getResources().getColor(R.color.red));
                    out.setTextColor(getResources().getColor(R.color.color_333));
                    getInnerImage();
                }
            }
        });
    }

    private void requestHotelInfo() {

        HttpProxy.obtain().get(PlatformContans.Hotel.getMyInfo, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        Gson gson = new Gson();
                        mHotelInfo = gson.fromJson(data.toString(), HotelInfo.class);
                        isLoadFinish = true;
                        upMineUI(mHotelInfo);
                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });

    }

    private void getImageCount() {
        HttpProxy.obtain().get(PlatformContans.Hotel.getImageNum, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    Log.e("num", result);
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        String tv_outer = "外景图片(" + data.getInt("outer") + ")";
                        String tv_inner = "内景图片(" + data.getInt("inner") + ")";
                        out.setText(tv_outer);
                        in.setText(tv_inner);
                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });
    }

    private void upLoadInnerImage() {
        for (String key : innerOldImageKey) {
            if (TextUtils.isEmpty(upInnerStr))
                upInnerStr =key;
            else
                upInnerStr = upInnerStr + "," + key;
        }
        Log.e("inner",upInnerStr);
        Map<String, Object> params = new HashMap<>();
        params.put("image", upInnerStr);
        HttpProxy.obtain().post(PlatformContans.Hotel.upInnerImage, App.getInstance().getUserEntity().getToken(), params, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    closeLoadView(mAddRoomLoadView);
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        innerOldImageKey.clear();
                        upInnerStr="";
                        getImageCount();
                        getInnerImage();
                        ToaskUtil.showToast(MineActivity.this, "更新成功");
                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });
    }

    private void upLoadOuterImage() {
        for (String key : outerOldImageKey) {
            if (TextUtils.isEmpty(upOuterStr))
                upOuterStr =key;
            else
                upOuterStr = upOuterStr + "," + key;
        }

        Log.e("upouter", upOuterStr);
        Map<String, Object> params = new HashMap<>();
        params.put("image", upOuterStr);
        HttpProxy.obtain().post(PlatformContans.Hotel.upOuterImage, App.getInstance().getUserEntity().getToken(), params, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    closeLoadView(mAddRoomLoadView);
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        outerOldImageKey.clear();
                        upOuterStr="";
                        ToaskUtil.showToast(MineActivity.this, "更新成功");
                        getImageCount();
                        getOuterImage();

                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });
    }

    private void getOuterImage() {
        selected.clear();
        upImgList.clear();
        mPhotoList.clear();
        isLoadFinish = false;
        HttpProxy.obtain().get(PlatformContans.Hotel.getOuterImage, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Log.e("outer", result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject outer = (JSONObject) data.get(i);
                            String url = outer.getString("imageUrl");
                            String key = outer.getString("imageKey");
                            if (!"null".equals(key) && !TextUtils.isEmpty(key)) {
                                 outerOldImageKey.add(key);
                            }
                            addOuterImage(key, url);
                            ;
                        }
                        RoomPhoto roomPhoto = new RoomPhoto();
                        mPhotoList.add(roomPhoto);
                        mAdapter.setDataByRemove(mPhotoList);

                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });

    }

    private void getInnerImage() {
        selected.clear();
        upImgList.clear();
        mPhotoList.clear();
        isLoadFinish = false;
        HttpProxy.obtain().get(PlatformContans.Hotel.getInnerImage, App.getInstance().getUserEntity().getToken(), new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Log.e("inner", result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject outer = (JSONObject) data.get(i);
                            String url = outer.getString("imageUrl");
                            String key = outer.getString("imageKey");
                            if (!"null".equals(key) && !TextUtils.isEmpty(key)) {
                                innerOldImageKey.add(key);
                            }
                            addOuterImage(key, url);
                        }
                        RoomPhoto roomPhoto = new RoomPhoto();
                        mPhotoList.add(roomPhoto);
                        mAdapter.setDataByRemove(mPhotoList);

                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {

            }
        });

    }

    //网络请求成功，
    private void upMineUI(HotelInfo hotelInfo) {
        name.setText(hotelInfo.getName());
        String Instr = hotelInfo.getInstr();
        if (!TextUtils.isEmpty(Instr)) {
            if (Instr.equals("null")) {
                synopsis.setText("");
            } else {
                synopsis.setText(Instr);
            }
        }

//        String image1Key = hotelInfo.getImage1();
//        String image2Key = hotelInfo.getImage2();
//        String image3Key = hotelInfo.getImage3();
//        String image4Key = hotelInfo.getImage4();
//        String image5Key = hotelInfo.getImage5();
//        String image6Key = hotelInfo.getImage6();
//
//        String image1Url = hotelInfo.getImage1Url();
//        String image2Url = hotelInfo.getImage2Url();
//        String image3Url = hotelInfo.getImage3Url();
//        String image4Url = hotelInfo.getImage4Url();
//        String image5Url = hotelInfo.getImage5Url();
//        String image6Url = hotelInfo.getImage6Url();
//
//        addOuterImage(image1Key, image1Url);
//        addOuterImage(image2Key, image2Url);
//        addOuterImage(image3Key, image3Url);
//        addOuterImage(image4Key, image4Url);
//        addOuterImage(image5Key, image5Url);
//        addOuterImage(image6Key, image6Url);
//
////        if (mPhotoList.size() < 6) {//添加最后一张图片
////            RoomPhoto roomPhoto = new RoomPhoto();
////            mPhotoList.add(roomPhoto);
////        }
//        //mAdapter.setDataByRemove(mPhotoList);

    }

    @Subscribe
    public void upDateUI(UpdateMineUI updateMineUI) {
        selected.clear();
        upImgList.clear();
        mPhotoList.clear();
        isLoadFinish = false;
        requestHotelInfo();
    }

    private void addOuterImage(String key, String url) {

        if (!TextUtils.isEmpty(key) && !"null".equals(key)) {
            upImgList.add(new UpImageBean(key, url));
            RoomPhoto roomPhoto = new RoomPhoto();
            roomPhoto.setImgUrl(url);
            roomPhoto.canDel = true;
            roomPhoto.isEditImgKey = true;
            roomPhoto.key = key;
            mPhotoList.add(roomPhoto);
        }


    }

    @OnClick({R.id.back, R.id.synopsis_edit_btn, R.id.minePositionLayout, R.id.mineTelLayout, R.id.saveText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.synopsis_edit_btn:
                showSynopsisEditWindow(view);
                break;
            case R.id.minePositionLayout:
                if (mHotelInfo == null) {
                    return;
                }
                Intent intent1 = new Intent(this, AddressManagerActivity.class);
                intent1.putExtra("address", mHotelInfo.getAddress());
                intent1.putExtra("addressDetail", mHotelInfo.getAddressDetail());

                startActivity(intent1);

                break;
            case R.id.mineTelLayout:
                if (mHotelInfo == null) {
                    return;
                }
                Intent intent = new Intent(this, TelephoneManagerActivity.class);
                String contactNumber = mHotelInfo.getContactNumber();
                if (!TextUtils.isEmpty(contactNumber)) {
                    if (contactNumber.equals("null")) {
                        contactNumber = "";
                    }
                } else {
                    contactNumber = "";
                }

                intent.putExtra("tel", contactNumber);
                startActivity(intent);
                break;
            case R.id.saveText:
                ActivityManager.getInstance().finishAllActivity();
                UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
                intance.clearUserInfo();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            mPhotoList.clear();
            selected.clear();
            int size = images.size();
            int len = upImgList.size();
            for (int i = 0; i < len; i++) {
                UpImageBean bean = upImgList.get(i);
                RoomPhoto roomPhoto = new RoomPhoto();
                roomPhoto.setImgUrl(bean.url);
                roomPhoto.canDel = true;
                roomPhoto.isEditImgKey = true;
                roomPhoto.key = bean.key;
                mPhotoList.add(roomPhoto);

            }

            for (int i = 0; i < size; i++) {
                RoomPhoto roomPhoto = new RoomPhoto();
                String imgUrl = images.get(i);
                roomPhoto.setImgUrl(imgUrl);
                roomPhoto.canDel = true;
                Log.d("onActivityResult", "onActivityResult: " + imgUrl);
                mPhotoList.add(roomPhoto);
                selected.add(imgUrl);
            }
            RoomPhoto roomPhoto = new RoomPhoto();
            mPhotoList.add(roomPhoto);
            mAdapter.setDataByRemove(mPhotoList);
            commitImage();

        }
    }

    //上传图片的数量次数
    private int count = 0;
    private KyLoadingBuilder mAddRoomLoadView;
    private boolean isLoadFinish = false;

    private void commitImage() {
        if (mAddRoomLoadView != null) {
            mAddRoomLoadView = null;
        }
        mAddRoomLoadView = openLoadView("添加中...");
        if (selected.size() != 0) {
            for (String filepath : selected) {
                upImage(PlatformContans.Image.uploadImage, filepath);
            }
        } else {
            updataHotelImage();
        }
    }

    private void upImage(String url, String filePath) {
        // Log.e("tag",url+filePath);
        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MineActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("requst", string);
                try {
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        String imgKey = object.getString("data");
                        if(isOut){
                            outerOldImageKey.add(imgKey);
                        }else{
                            innerOldImageKey.add(imgKey);
                        }
                        upImgList.add(new UpImageBean(imgKey, null));
                        count++;
                        if (count == selected.size()) {
                            count = 0;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isOut) {
                                        upLoadOuterImage();
                                    } else {
                                        Log.e("aaa","1111");
                                        upLoadInnerImage();
                                    }
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void changeSelectphoto(RoomPhoto roomPhotoSrc) {
        final String imgUrlSrc = roomPhotoSrc.getImgUrl();
        if (roomPhotoSrc.isEditImgKey) {
            for (int i = 0; i < upImgList.size(); i++) {
                UpImageBean upImageBean = upImgList.get(i);
                if (imgUrlSrc.equals(upImageBean.url)) {
                    upImgList.remove(upImageBean);
                }
            }
        } else {
            selected.remove(imgUrlSrc);
        }
        mPhotoList.clear();

        int sizeKeyNum = upImgList.size();
        int sizeSelectedNum = selected.size();

        int totalSize = sizeKeyNum + sizeSelectedNum;

        for (int i = 0; i < sizeKeyNum; i++) {
            RoomPhoto roomPhoto = new RoomPhoto();
            UpImageBean bean = upImgList.get(i);
            roomPhoto.setImgUrl(bean.url);
            roomPhoto.canDel = true;
            roomPhoto.isEditImgKey = true;
            roomPhoto.key = bean.key;
            mPhotoList.add(roomPhoto);
        }

        for (int i = 0; i < sizeSelectedNum; i++) {
            RoomPhoto roomPhoto = new RoomPhoto();
            String imgUrl = selected.get(i);
            roomPhoto.setImgUrl(imgUrl);
            roomPhoto.canDel = true;
            mPhotoList.add(roomPhoto);

        }
        if (totalSize < 6) {//添加最后一张图片
            RoomPhoto roomPhoto = new RoomPhoto();
            mPhotoList.add(roomPhoto);
        }
        mAdapter.setDataByRemove(mPhotoList);
        commitImage();
    }

    private void showSynopsisEditWindow(View view) {
        View brandView = LayoutInflater.from(this).inflate(R.layout.pw_synopsis_edit, null);
        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(brandView)
                .sizeByPercentage(this, 0.8f, 0f)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.style.CustomPopWindowStyle)
                .setBgDarkAlpha(0.5f)
                .create();
        handlerSynopsisEditWindow(brandView, customPopWindow);
        customPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void handlerSynopsisEditWindow(View view, final CustomPopWindow customPopWindow) {
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
            }
        });
        final EditText editText = (EditText) view.findViewById(R.id.synopsis_edit);
        recordEditString = synopsis.getText().toString();
        editText.setText(recordEditString);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recordEditString = s.toString();
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddRoomLoadView != null) {
                    mAddRoomLoadView = null;
                }
                mAddRoomLoadView = openLoadView("");
                recordEditString = "";
                updateIntroduce(editText.getEditableText().toString());
                if (customPopWindow != null) {
                    customPopWindow.dissmiss();
                }
            }
        });

    }

    private void updataHotelImage() {

        Map<String, Object> parame = new HashMap<>();
        for (int i = 1; i <= upImgList.size(); i++) {
            String key = upImgList.get(i - 1).key;
            parame.put("image" + i, key);
        }
        if (upImgList.size() < 6) {
            for (int i = upImgList.size() + 1; i < 7; i++) {
                parame.put("image" + i, "");
            }
        }

        HttpProxy.obtain().post(PlatformContans.Hotel.uploadImage, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                closeLoadView(mAddRoomLoadView);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        selected.clear();
                        upImgList.clear();
                        mPhotoList.clear();
                        isLoadFinish = false;
                        ToaskUtil.showToast(MineActivity.this, "更新成功");
                        requestHotelInfo();
                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLtFailure(String error) {
                closeLoadView(mAddRoomLoadView);

            }
        });

    }

    private void updateIntroduce(String introduce) {
        if (introduce == null) {
            introduce = "";
        }
        Map<String, Object> parame = new HashMap<>();
        parame.put("introduce", introduce);
        HttpProxy.obtain().post(PlatformContans.Hotel.updateIntroduce, App.getInstance().getUserEntity().getToken(), parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                closeLoadView(mAddRoomLoadView);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        selected.clear();
                        upImgList.clear();
                        mPhotoList.clear();
                        isLoadFinish = false;
                        requestHotelInfo();
                    } else {
                        ToaskUtil.showToast(MineActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                closeLoadView(mAddRoomLoadView);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
