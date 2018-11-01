package com.tec.hotel_com.order_manage.entity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.rv.base.RVBaseAdapter;
import com.tec.hotel_com.common.rv.base.RVBaseCell;
import com.tec.hotel_com.common.rv.base.RVBaseViewHolder;
import com.tec.hotel_com.eventBean.PhoneCallBack;
import com.tec.hotel_com.order_manage.activity.UserHotelOrderDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者：凌涛 on 2018/8/6 11:06
 * 邮箱：771548229@qq..com
 */
public class UserOrderInfo extends RVBaseCell implements Serializable{

    private String id;
    private String nickname;
    private String orderNum;
    private String userId;
    private String productNum;
    private String status;
    private String isComment;
    private int days;
    private String travelAgencyRoomtypeId;
    private String roomtypeId;
    private String roomtypeName;
    private String firstDate;
    private String lastDate;
    private int orderRoomNum;
    private double amount;
    private String telephoneNum;
    private int peopleNum;
    private String createTime;
    private String updateTime;
    private String paymentTime;
    private String isDeleted;
    private String hotelId;
    private String hotelName;
    private String travelAgencyId;
    private String travelAgencyName;
    private String isInner;
    private String image1;
    private double price;
    private double hotelPrice;
    private double managePrice;
    private String name;
    private String descr;
    private String regulation;
    private String liveNum;
    private String bedNum;
    private String floor;
    private String bedType;
    private String window;
    private String bathroom;
    private String intnet;
    private String breakfast;
    private String isCanCancel;
    private String isUseCoupon;
    private String checkInPerson;
    private int hotelAmount;
    private String modeOfPayment;
    private String finishedType;
    private String finishedTime;
    private String refundTime;

    public UserOrderInfo() {
        super(null);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_order_rv_layout, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position, final Context context, RVBaseAdapter adapter) {
        holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHotelOrderDetailActivity.startHotelOrderDetailActivity(context, id,1);
            }
        });

        TextView orderIdText = holder.getTextView(R.id.orderId);
        orderIdText.setText("订单编号:" + orderNum);
        holder.getTextView(R.id.placeTime).setText("下单时间:" + paymentTime);
        holder.getTextView(R.id.price).setText("￥ " + amount);

        TextView stateTextView = holder.getTextView(R.id.state);
        if (status.equals("1")) {
            stateTextView.setText("待消费");
        } else if (status.equals("2")) {
            stateTextView.setText("退款中");
            stateTextView.setTextColor(Color.parseColor("#d7382d"));

        } else if (status.equals("3")) {
            stateTextView.setText("已退款");
            stateTextView.setTextColor(Color.GRAY);

        } else if (status.equals("4")) {
            stateTextView.setText("已完成");
            stateTextView.setTextColor(Color.parseColor("#09e6e2"));
        }

        TextView bookingTel = holder.getTextView(R.id.bookingTel);
        bookingTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        bookingTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回调到上一层，打电话
                Log.d("onClick", "onClick: 打电话");
                PhoneCallBack callBack = new PhoneCallBack();
                callBack.setTel(telephoneNum);
                EventBus.getDefault().post(callBack);
            }
        });

        holder.getTextView(R.id.hotelName).setText(hotelName);
        holder.getTextView(R.id.roomName).setText(roomtypeName);

        holder.getTextView(R.id.breakfast).setText(breakfast + " | " + bedType);
        holder.getTextView(R.id.bookingName).setText(checkInPerson);
        holder.getTextView(R.id.bookingTel).setText(telephoneNum);
        holder.getTextView(R.id.roomNumber).setText("3间");


        String firstDate = this.firstDate;
        String lastDate = this.lastDate;

        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date firstParse = null;
        Date endParse = null;
        try {
            firstParse = format0.parse(firstDate);
            endParse = format0.parse(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String start = format.format(firstParse);
        String end = format.format(endParse);


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getTravelAgencyRoomtypeId() {
        return travelAgencyRoomtypeId;
    }

    public void setTravelAgencyRoomtypeId(String travelAgencyRoomtypeId) {
        this.travelAgencyRoomtypeId = travelAgencyRoomtypeId;
    }

    public String getRoomtypeId() {
        return roomtypeId;
    }

    public void setRoomtypeId(String roomtypeId) {
        this.roomtypeId = roomtypeId;
    }

    public String getRoomtypeName() {
        return roomtypeName;
    }

    public void setRoomtypeName(String roomtypeName) {
        this.roomtypeName = roomtypeName;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getOrderRoomNum() {
        return orderRoomNum;
    }

    public void setOrderRoomNum(int orderRoomNum) {
        this.orderRoomNum = orderRoomNum;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTravelAgencyId() {
        return travelAgencyId;
    }

    public void setTravelAgencyId(String travelAgencyId) {
        this.travelAgencyId = travelAgencyId;
    }

    public String getTravelAgencyName() {
        return travelAgencyName;
    }

    public void setTravelAgencyName(String travelAgencyName) {
        this.travelAgencyName = travelAgencyName;
    }

    public String getIsInner() {
        return isInner;
    }

    public void setIsInner(String isInner) {
        this.isInner = isInner;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(double hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public double getManagePrice() {
        return managePrice;
    }

    public void setManagePrice(double managePrice) {
        this.managePrice = managePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public String getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(String liveNum) {
        this.liveNum = liveNum;
    }

    public String getBedNum() {
        return bedNum;
    }

    public void setBedNum(String bedNum) {
        this.bedNum = bedNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getIntnet() {
        return intnet;
    }

    public void setIntnet(String intnet) {
        this.intnet = intnet;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getIsCanCancel() {
        return isCanCancel;
    }

    public void setIsCanCancel(String isCanCancel) {
        this.isCanCancel = isCanCancel;
    }

    public String getIsUseCoupon() {
        return isUseCoupon;
    }

    public void setIsUseCoupon(String isUseCoupon) {
        this.isUseCoupon = isUseCoupon;
    }

    public String getCheckInPerson() {
        return checkInPerson;
    }

    public void setCheckInPerson(String checkInPerson) {
        this.checkInPerson = checkInPerson;
    }

    public int getHotelAmount() {
        return hotelAmount;
    }

    public void setHotelAmount(int hotelAmount) {
        this.hotelAmount = hotelAmount;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getFinishedType() {
        return finishedType;
    }

    public void setFinishedType(String finishedType) {
        this.finishedType = finishedType;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }
}
