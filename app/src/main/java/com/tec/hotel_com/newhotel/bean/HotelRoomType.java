package com.tec.hotel_com.newhotel.bean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseCell;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.room_manage.activity.AddRoomActivity;
import com.tec.hotel_com.room_manage.activity.OneMouthDataActivity;

import java.io.Serializable;

public class HotelRoomType extends RVBaseCell implements Serializable {

    /**
     * bathroom : string
     * bedNum : string
     * bedType : string
     * breakfast : string
     * createTime : 2018-10-24T06:26:04.474Z
     * describe : string
     * floor : string
     * hotelId : string
     * hotelPrice : 0
     * id : string
     * image1 : string
     * image1Url : string
     * image2 : string
     * image2Url : string
     * image3 : string
     * image3Url : string
     * image4 : string
     * image4Url : string
     * image5 : string
     * image5Url : string
     * intnet : string
     * isCanCancel : string
     * isDeleted : string
     * isUseCoupon : string
     * isUsed : string
     * liveNum : string
     * managePrice : 0
     * managePriceCeiling : 0
     * name : string
     * regulations : string
     * roomNum : 0
     * sequence : 0
     * setOutAmount : 0
     * updateTime : 2018-10-24T06:26:04.474Z
     * window : string
     */

    private String bathroom;
    private String bedNum;
    private String bedType;
    private String breakfast;
    private String createTime;
    private String describe;
    private String floor;
    private String hotelId;
    private double hotelPrice;
    private String id;
    private String image1;
    private String image1Url;
    private String image2;
    private String image2Url;
    private String image3;
    private String image3Url;
    private String image4;
    private String image4Url;
    private String image5;
    private String image5Url;
    private String intnet;
    private String isCanCancel;
    private String isDeleted;
    private String isUseCoupon;
    private String isUsed;
    private String liveNum;
    private double managePrice;
    private double managePriceCeiling;
    private String name;
    private String regulations;
    private int roomNum;
    private int sequence;
    private int setOutAmount;
    private String updateTime;
    private String window;

    public HotelRoomType(Object o) {
        super(null);
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getBedNum() {
        return bedNum;
    }

    public void setBedNum(String bedNum) {
        this.bedNum = bedNum;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public double getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(double hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage1Url() {
        return image1Url;
    }

    public void setImage1Url(String image1Url) {
        this.image1Url = image1Url;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2Url() {
        return image2Url;
    }

    public void setImage2Url(String image2Url) {
        this.image2Url = image2Url;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage3Url() {
        return image3Url;
    }

    public void setImage3Url(String image3Url) {
        this.image3Url = image3Url;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage4Url() {
        return image4Url;
    }

    public void setImage4Url(String image4Url) {
        this.image4Url = image4Url;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage5Url() {
        return image5Url;
    }

    public void setImage5Url(String image5Url) {
        this.image5Url = image5Url;
    }

    public String getIntnet() {
        return intnet;
    }

    public void setIntnet(String intnet) {
        this.intnet = intnet;
    }

    public String getIsCanCancel() {
        return isCanCancel;
    }

    public void setIsCanCancel(String isCanCancel) {
        this.isCanCancel = isCanCancel;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsUseCoupon() {
        return isUseCoupon;
    }

    public void setIsUseCoupon(String isUseCoupon) {
        this.isUseCoupon = isUseCoupon;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(String liveNum) {
        this.liveNum = liveNum;
    }

    public double getManagePrice() {
        return managePrice;
    }

    public void setManagePrice(double managePrice) {
        this.managePrice = managePrice;
    }

    public double getManagePriceCeiling() {
        return managePriceCeiling;
    }

    public void setManagePriceCeiling(double managePriceCeiling) {
        this.managePriceCeiling = managePriceCeiling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegulations() {
        return regulations;
    }

    public void setRegulations(String regulations) {
        this.regulations = regulations;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSetOutAmount() {
        return setOutAmount;
    }

    public void setSetOutAmount(int setOutAmount) {
        this.setOutAmount = setOutAmount;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roomtype, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, Context context, RVBaseAdapter adapter) {
        final Context mContext = holder.itemView.getContext();
        holder.setText(R.id.type, name);
        holder.setText(R.id.cash, "￥" + hotelPrice + "");
        holder.setText(R.id.tv_num, roomNum + "间");

        Glide.with(mContext).load(image1Url).into(holder.getImageView(R.id.image));
//        holder.getView(R.id.image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, AddRoomActivity.class));
//            }
//        });

    }
}
