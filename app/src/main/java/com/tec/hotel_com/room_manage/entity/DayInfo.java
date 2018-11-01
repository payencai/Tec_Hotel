package com.tec.hotel_com.room_manage.entity;

/**
 * 作者：凌涛 on 2018/8/22 17:39
 * 邮箱：771548229@qq..com
 */
public class DayInfo {

    private String id;
    private String roomtypeId;
    private String hotelId;
    private int totalNum;
    private String emptyNum;
    private int orderNum;
    private String enterTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomtypeId() {
        return roomtypeId;
    }

    public void setRoomtypeId(String roomtypeId) {
        this.roomtypeId = roomtypeId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getEmptyNum() {
        return emptyNum;
    }

    public void setEmptyNum(String emptyNum) {
        this.emptyNum = emptyNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    @Override
    public String toString() {
        return enterTime;
    }
}
