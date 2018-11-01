package com.tec.hotel_com.room_manage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.tec.hotel_com.App;
import com.tec.hotel_com.Constant.PlatformContans;
import com.tec.hotel_com.R;
import com.tec.hotel_com.base.BaseActivity;
import com.tec.hotel_com.common.recyclerDecoration.DividerGridItemDecoration;
import com.tec.hotel_com.common.recyclerDecoration.MyDividerItemDecoration;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.eventBean.InformHomeRefresh;
import com.tec.hotel_com.eventBean.UpdataImgKey;
import com.tec.hotel_com.http.HttpProxy;
import com.tec.hotel_com.http.ICallBack;
import com.tec.hotel_com.room_manage.entity.RoomInfo;
import com.tec.hotel_com.room_manage.entity.RoomPhoto;
import com.tec.hotel_com.room_manage.entity.UpImageBean;
import com.tec.hotel_com.utils.LogUtil;
import com.tec.hotel_com.utils.ToaskUtil;
import com.tec.hotel_com.widget.KyLoadingBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddRoomActivity extends BaseActivity {


    public static final int REQUEST_CODE = 0;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.imgSelectRv)
    RecyclerView imgSelectRv;
    @BindView(R.id.houseType)
    EditText houseType;
    @BindView(R.id.roomNumber)
    EditText roomNumber;
    @BindView(R.id.roomPrice)
    EditText roomPrice;
    @BindView(R.id.bedType)
    EditText bedType;
    @BindView(R.id.bathroom)
    EditText bathroom;
    @BindView(R.id.network)
    EditText network;
    @BindView(R.id.describe)
    EditText describe;
    @BindView(R.id.breakfast)
    ImageView breakfast;
    @BindView(R.id.unBreakfast)
    ImageView unBreakfast;
    @BindView(R.id.canCancel)
    ImageView canCancel;
    @BindView(R.id.unCanCancel)
    ImageView unCanCancel;
    @BindView(R.id.coupon)
    ImageView coupon;
    @BindView(R.id.unCoupon)
    ImageView unCoupon;
    @BindView(R.id.bedNumber)
    EditText bedNumber;
    @BindView(R.id.level1)
    EditText level1;
    @BindView(R.id.level2)
    EditText level2;
    @BindView(R.id.canPutNumber)
    EditText canPutNumber;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.saveText)
    TextView del;
    @BindView(R.id.submitLayout)
    LinearLayout submitLayout;

    //是否有早餐，默认为都为有
    private boolean isBreakfast = true;
    //是否可取消
    private boolean isCanCancel = true;
    //是否有优惠券
    private boolean isCoupon = true;


    private RVBaseAdapter<RoomPhoto> mAdapter;
    private List<RoomPhoto> mPhotoList;
    private Handler mHandler = new Handler();

    //上传成功的图片key
    public List<UpImageBean> upImgList = new ArrayList<>();

    //已选的图片
    public ArrayList<String> selected = new ArrayList<>();

    private int intoType = -1;//进入类型，1为添加房型，2为浏览，3为修改房型
    private KyLoadingBuilder mAddRoomLoadView;
    private RoomInfo mRoomInfo;

    public static void startAddRoomOrChange(Context context, int intoType, RoomInfo roomInfo) {
        Intent intent = new Intent(context, AddRoomActivity.class);
        intent.putExtra("intoType", intoType);
        intent.putExtra("roomInfo", roomInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_add_room;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        intoType = intent.getIntExtra("intoType", -1);
        mRoomInfo = ((RoomInfo) intent.getSerializableExtra("roomInfo"));
        mPhotoList = new ArrayList<>();
        if (intoType == 1) {
            title.setText("添加房型");
            mPhotoList.add(new RoomPhoto());
            breakfast.setSelected(true);
            canCancel.setSelected(true);
            coupon.setSelected(true);
        } else if (intoType == 2) {
            title.setText("房型详情");
            setImageView2();
            setUI(false);
        } else if (intoType == 3) {
            del.setText("删除");
            del.setVisibility(View.VISIBLE);
            del.setTextColor(getResources().getColor(R.color.red));

            title.setText("修改房型");
            setImageView3();
            setUI(true);
        }
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(mRoomInfo.getId());
            }
        });

        imgSelectRv.setLayoutManager(new GridLayoutManager(this, 3));
        //设置分隔线
        imgSelectRv.addItemDecoration(new MyDividerItemDecoration(10));
        //设置增加或删除条目的动画
        imgSelectRv.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RVBaseAdapter<RoomPhoto>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onViewClick(RVBaseViewHolder holder, final int position) {
                ImageView delImg = holder.getImageView(R.id.del);
                final RoomPhoto roomPhoto = getData().get(position);

                delImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeSelectphoto(roomPhoto);
                    }
                });
            }
        };
        mAdapter.setData(mPhotoList);
        imgSelectRv.setAdapter(mAdapter);

    }

    private void delete(String id) {
        String token = App.getInstance().getUserEntity().getToken();
        Map<String, Object> parame = new HashMap<>();
        parame.put("roomtypeId", id);
        HttpProxy.obtain().post(PlatformContans.Roomtype.delete, token, parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("getMine", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {

                        ToaskUtil.showToast(AddRoomActivity.this, "删除成功");
                        EventBus.getDefault().post(new InformHomeRefresh());
                        finish();
                    } else {
                        ToaskUtil.showToast(AddRoomActivity.this, message);
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

    private void setImageView2() {
        String image1Url = mRoomInfo.getImage1Url();
        String image2Url = mRoomInfo.getImage2Url();
        String image3Url = mRoomInfo.getImage3Url();
        String image4Url = mRoomInfo.getImage4Url();
        String image5Url = mRoomInfo.getImage5Url();


        addImageUrl2(image1Url);
        addImageUrl2(image2Url);
        addImageUrl2(image3Url);
        addImageUrl2(image4Url);
        addImageUrl2(image5Url);

    }

    private void setImageView3() {
        String image1Url = mRoomInfo.getImage1Url();
        String image2Url = mRoomInfo.getImage2Url();
        String image3Url = mRoomInfo.getImage3Url();
        String image4Url = mRoomInfo.getImage4Url();
        String image5Url = mRoomInfo.getImage5Url();

        //Key
        String image1 = mRoomInfo.getImage1();
        String image2 = mRoomInfo.getImage2();
        String image3 = mRoomInfo.getImage3();
        String image4 = mRoomInfo.getImage4();
        String image5 = mRoomInfo.getImage5();

        addImageKeyToList(image1, image1Url);
        addImageKeyToList(image2, image2Url);
        addImageKeyToList(image3, image3Url);
        addImageKeyToList(image4, image4Url);
        addImageKeyToList(image5, image5Url);

        if (mPhotoList.size() < 5) {//添加最后一张图片
            RoomPhoto roomPhoto = new RoomPhoto();
            mPhotoList.add(roomPhoto);
        }


    }

    private void addImageKeyToList(String key, String url) {
        if (!TextUtils.isEmpty(key)) {
            if (!key.equals("null")) {

                upImgList.add(new UpImageBean(key, url));

                RoomPhoto roomPhoto = new RoomPhoto();
                roomPhoto.setImgUrl(url);
                roomPhoto.canDel = true;
                roomPhoto.isEditImgKey = true;
                roomPhoto.key = key;
                mPhotoList.add(roomPhoto);

            }
        }
    }

    private void addImageUrl2(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.equals("null")) {
                RoomPhoto roomPhoto = new RoomPhoto();
                roomPhoto.setImgUrl(url);
                roomPhoto.canDel = true;
                roomPhoto.isShowNetwork = true;
                mPhotoList.add(roomPhoto);
            }
        }
    }

    private void setUI(boolean isShowView) {
        if (isShowView) {
            submitLayout.setVisibility(View.VISIBLE);
        } else {
            submitLayout.setVisibility(View.GONE);
        }
        houseType.setText(mRoomInfo.getName());
        roomNumber.setText(mRoomInfo.getRoomNum() + "");
        roomPrice.setText(mRoomInfo.getHotelPrice() + "");
        bedType.setText(mRoomInfo.getBedType());
        network.setText(mRoomInfo.getIntnet());
        describe.setText(mRoomInfo.getDescribe());
        bathroom.setText(mRoomInfo.getBathroom());
        String breakfast = mRoomInfo.getBreakfast();
//        int isBrea = Integer.parseInt(breakfast);
        if (!breakfast.equals("0")) {
            this.breakfast.setSelected(true);
            unBreakfast.setSelected(false);
            isBreakfast = true;
        } else {
            this.breakfast.setSelected(false);
            unBreakfast.setSelected(true);
            isBreakfast = false;
        }

        String isCanCancel = mRoomInfo.getIsCanCancel();
        int isCancel = Integer.parseInt(isCanCancel);
        if (isCancel == 1) {
            canCancel.setSelected(true);
            unCanCancel.setSelected(false);
            this.isCanCancel = true;
        } else {
            canCancel.setSelected(false);
            unCanCancel.setSelected(true);
            this.isCanCancel = false;
        }

        String isUseCoupon = mRoomInfo.getIsUseCoupon();
//        int isUseCoupone = Integer.parseInt(isUseCoupon);
        if (!TextUtils.isEmpty(isUseCoupon)) {
            if (isUseCoupon.equals("1")) {
                coupon.setSelected(true);
                unCoupon.setSelected(false);
                isCoupon = true;
            } else {
                coupon.setSelected(false);
                unCoupon.setSelected(true);
                isCoupon = false;
            }
        } else {
            coupon.setSelected(false);
            unCoupon.setSelected(true);
            isCoupon = false;
        }

        bedNumber.setText(mRoomInfo.getBedNum());
        canPutNumber.setText(mRoomInfo.getLiveNum());
        try {
            String floor = mRoomInfo.getFloor();
            String[] split = floor.replace("层", "").split("-");
            level1.setText(split[0]);
            level2.setText(split[1]);

        } catch (Exception e) {

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
                mPhotoList.add(roomPhoto);
                selected.add(imgUrl);
            }
            if (mPhotoList.size() < 5) {//添加最后一张图片
                RoomPhoto roomPhoto = new RoomPhoto();
                mPhotoList.add(roomPhoto);
            }
            mAdapter.setDataByRemove(mPhotoList);
        }
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
        if (totalSize < 5) {//添加最后一张图片
            RoomPhoto roomPhoto = new RoomPhoto();
            mPhotoList.add(roomPhoto);
        }
        mAdapter.setDataByRemove(mPhotoList);
    }

    @OnClick({R.id.back, R.id.breakfast, R.id.unBreakfast, R.id.canCancel, R.id.unCanCancel, R.id.coupon, R.id.unCoupon, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.breakfast:
                breakfast.setSelected(true);
                unBreakfast.setSelected(false);
                isBreakfast = true;
                break;
            case R.id.unBreakfast:
                breakfast.setSelected(false);
                unBreakfast.setSelected(true);
                isBreakfast = false;
                break;
            case R.id.canCancel:
                canCancel.setSelected(true);
                unCanCancel.setSelected(false);
                isCanCancel = true;
                break;
            case R.id.unCanCancel:
                canCancel.setSelected(false);
                unCanCancel.setSelected(true);
                isCanCancel = false;
                break;
            case R.id.coupon:
                coupon.setSelected(true);
                unCoupon.setSelected(false);
                isCoupon = true;
                break;
            case R.id.unCoupon:
                coupon.setSelected(false);
                unCoupon.setSelected(true);
                isCoupon = false;
                break;
            case R.id.submit:
                if (selected.size() == 0) {
                    if (intoType == 1) {
                        ToaskUtil.showToast(this, "必须至少有一张房型图");
                        return;
                    }
                }
                String houseTypeName = houseType.getEditableText().toString().replace(" ", "");
                String roomNumberStr = roomNumber.getEditableText().toString().replace(" ", "");
                String roomPriceStr = roomPrice.getEditableText().toString().replace(" ", "");
                String bedTypeStr = bedType.getEditableText().toString().replace(" ", "");
                String bathroomStr = bathroom.getEditableText().toString().replace(" ", "");
                String describeStr = describe.getEditableText().toString().replace(" ", "");
                String bedNumberStr = bedNumber.getEditableText().toString().replace(" ", "");
                String level1Str = level1.getEditableText().toString().replace(" ", "");
                String level1Str2 = level2.getEditableText().toString().replace(" ", "");
                String canPutNumberStr = canPutNumber.getEditableText().toString().replace(" ", "");
                String networkStr = network.getEditableText().toString().replace(" ", "");
                if (!checkFrom(houseTypeName, roomNumberStr, roomPriceStr, bedTypeStr, bathroomStr
                        , describeStr, bedNumberStr, level1Str, level1Str2, canPutNumberStr, networkStr)) {
                    return;
                }
                submit.setEnabled(false);
                commitImage();
                break;

        }
    }

    private boolean checkFrom(String houseTypeName, String roomNumberStr
            , String roomPriceStr, String bedTypeStr, String bathroomStr
            , String describeStr, String bedNumberStr, String level1Str, String level1Str2, String canPutNumberStr, String networkStr) {

        if (TextUtils.isEmpty(houseTypeName)) {
            ToaskUtil.showToast(this, "请输入房型");
            return false;
        }
        if (TextUtils.isEmpty(roomNumberStr)) {
            ToaskUtil.showToast(this, "请输入房型数量");
            return false;
        }
        if (TextUtils.isEmpty(roomPriceStr)) {
            ToaskUtil.showToast(this, "请输入房型价钱");
            return false;
        }
        if (TextUtils.isEmpty(bedTypeStr)) {
            ToaskUtil.showToast(this, "请输入床型");
            return false;
        }
        if (TextUtils.isEmpty(bathroomStr)) {
            ToaskUtil.showToast(this, "请输入卫浴样式");
            return false;
        }

        if (TextUtils.isEmpty(networkStr)) {
            ToaskUtil.showToast(this, "请输入网络类型");
            return false;
        }
//        if (TextUtils.isEmpty(describeStr)) {
//            ToaskUtil.showToast(this, "请输入房型描述");
//            return false;
//        }

        if (TextUtils.isEmpty(bedNumberStr)) {
            ToaskUtil.showToast(this, "请输入房型床数量");
            return false;
        }

        if (TextUtils.isEmpty(level1Str)) {
            ToaskUtil.showToast(this, "请输入房型楼层");
            return false;
        }

        if (TextUtils.isEmpty(level1Str2)) {
            ToaskUtil.showToast(this, "请输入房型楼层");
            return false;
        }

        if (Integer.parseInt(level1Str) > Integer.parseInt(level1Str2)) {
            ToaskUtil.showToast(this, "楼层输入错误");
            return false;
        }

        if (TextUtils.isEmpty(canPutNumberStr)) {
            ToaskUtil.showToast(this, "请输入可住人数");
            return false;
        }

        return true;
    }

    private void requestAddRoomType() {
        String houseTypeName = houseType.getEditableText().toString().replace(" ", "");
        String roomNumberStr = roomNumber.getEditableText().toString().replace(" ", "");
        String roomPriceStr = roomPrice.getEditableText().toString().replace(" ", "");
        String bedTypeStr = bedType.getEditableText().toString().replace(" ", "");
        String bathroomStr = bathroom.getEditableText().toString().replace(" ", "");
        String describeStr = describe.getEditableText().toString().replace(" ", "");
        String bedNumberStr = bedNumber.getEditableText().toString().replace(" ", "");
        String level1Str = level1.getEditableText().toString().replace(" ", "");
        String level1Str2 = level2.getEditableText().toString().replace(" ", "");
        String canPutNumberStr = canPutNumber.getEditableText().toString().replace(" ", "");
        String networkStr = network.getEditableText().toString().replace(" ", "");
        String token = App.getInstance().getUserEntity().getToken();


        Map<String, Object> parame = new HashMap<>();
        parame.put("hotelPrice", roomPriceStr);
        parame.put("name", houseTypeName);
        if (!TextUtils.isEmpty(describeStr)) {
            parame.put("describe", describeStr);
        }
        for (int i = 1; i <= upImgList.size(); i++) {
            String key = upImgList.get(i - 1).key;
            parame.put("image" + i, key);
        }
        if (upImgList.size() < 5) {
            for (int i = upImgList.size() + 1; i < 6; i++) {
                parame.put("image" + i, "");
            }
        }
        parame.put("roomNum", roomNumberStr);
        parame.put("bedNum", bedNumberStr);
        parame.put("floor", level1Str + "-" + level1Str2 + "层");
        parame.put("bedType", bedTypeStr);
        parame.put("liveNum", canPutNumberStr);
        parame.put("bathroom", bathroomStr);
        parame.put("intnet", networkStr);
        parame.put("breakfast", isBreakfast ? "有" : "无");
        parame.put("isCanCancel", isCanCancel ? 1 : 2);
//        parame.put("isUseCoupon", isCoupon ? "有" : "无");
        if (intoType == 3) {
            parame.put("id", mRoomInfo.getId());
        }


        String url = "";
        if (intoType == 1) {
            url = PlatformContans.Roomtype.add;
        } else if (intoType == 3) {
            url = PlatformContans.Roomtype.edit;
        }

        HttpProxy.obtain().post(url, token, parame, new ICallBack() {
            @Override
            public void OnLtSuccess(String result) {
                LogUtil.Log("getMine", result);
                count = 0;
                submit.setEnabled(true);
                closeLoadView(mAddRoomLoadView);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        upImgList.clear();
                        if (intoType == 1) {
                            ToaskUtil.showToast(AddRoomActivity.this, "添加成功");
                        } else if (intoType == 3) {
                            ToaskUtil.showToast(AddRoomActivity.this, "修改成功");
                        }

                        EventBus.getDefault().post(new InformHomeRefresh());

                        finish();
                    } else {
                        ToaskUtil.showToast(AddRoomActivity.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLtFailure(String error) {
                count = 0;
                submit.setEnabled(true);
                closeLoadView(mAddRoomLoadView);
                upImgList.clear();
            }
        });

    }

    //上传图片的数量次数
    private int count = 0;

    private void commitImage() {
        mAddRoomLoadView = openLoadView("添加中...");
        if (selected.size() != 0) {
            for (String filepath : selected) {
                upImage(PlatformContans.Image.uploadImage, filepath);
            }
        } else {
            if (intoType == 3) {
                requestAddRoomType();
            }
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
                        Toast.makeText(AddRoomActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                LogUtil.Log("requestAddRoomType", "onResponse: " + string);
                try {
                    JSONObject object = new JSONObject(string);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        String imgKey = object.getString("data");
                        upImgList.add(new UpImageBean(imgKey, null));
                        count++;
                        if (count == selected.size()) {
                            count = 0;
                            Log.e("commit", count + "");
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestAddRoomType();
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
}
