package com.tec.hotel_com.order_manage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：凌涛 on 2018/10/13 18:13
 * 邮箱：771548229@qq..com
 */
public class UserHotelOrderDetailBean implements Serializable {
    
    private HotelOrderBean hotelOrder;
    private HotelBean hotel;
    private HotelCommentBean hotelComment;
    private List<RefundListBean> refundList;

    public HotelOrderBean getHotelOrder() {
        return hotelOrder;
    }

    public void setHotelOrder(HotelOrderBean hotelOrder) {
        this.hotelOrder = hotelOrder;
    }

    public HotelBean getHotel() {
        return hotel;
    }

    public void setHotel(HotelBean hotel) {
        this.hotel = hotel;
    }

    public HotelCommentBean getHotelComment() {
        return hotelComment;
    }

    public void setHotelComment(HotelCommentBean hotelComment) {
        this.hotelComment = hotelComment;
    }

    public List<RefundListBean> getRefundList() {
        return refundList;
    }

    public void setRefundList(List<RefundListBean> refundList) {
        this.refundList = refundList;
    }

    public static class HotelOrderBean  {

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
        private double hotelAmount;
        private String modeOfPayment;
        private String finishedType;
        private String finishedTime;
        private String refundTime;

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

        public double getHotelAmount() {
            return hotelAmount;
        }

        public void setHotelAmount(double hotelAmount) {
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

    public static class HotelBean {

        private String id;
        private String name;
        private String contactNumber;
        private String province;
        private String city;
        private String district;
        private String longitude;
        private String latitude;
        private String address;
        private String addressDetail;
        private String hotelRating;
        private String instr;
        private String oneWord;
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
        private String image6;
        private String image6Url;
        private String createTime;
        private String isDeleted;
        private String isUsed;
        private String commentData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }

        public String getHotelRating() {
            return hotelRating;
        }

        public void setHotelRating(String hotelRating) {
            this.hotelRating = hotelRating;
        }

        public String getInstr() {
            return instr;
        }

        public void setInstr(String instr) {
            this.instr = instr;
        }

        public String getOneWord() {
            return oneWord;
        }

        public void setOneWord(String oneWord) {
            this.oneWord = oneWord;
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

        public String getImage6() {
            return image6;
        }

        public void setImage6(String image6) {
            this.image6 = image6;
        }

        public String getImage6Url() {
            return image6Url;
        }

        public void setImage6Url(String image6Url) {
            this.image6Url = image6Url;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getIsUsed() {
            return isUsed;
        }

        public void setIsUsed(String isUsed) {
            this.isUsed = isUsed;
        }

        public String getCommentData() {
            return commentData;
        }

        public void setCommentData(String commentData) {
            this.commentData = commentData;
        }
    }

    public static class HotelCommentBean {

        private String id;
        private String name;
        private String image;
        private String homeType;
        private int score;
        private String content;
        private String hotelId;
        private String orderId;
        private int state;
        private String createTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getHomeType() {
            return homeType;
        }

        public void setHomeType(String homeType) {
            this.homeType = homeType;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHotelId() {
            return hotelId;
        }

        public void setHotelId(String hotelId) {
            this.hotelId = hotelId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }

    public static class RefundListBean {

        private String id;
        private String orderId;
        private double refundAmount;
        private String createTime;
        private String disposeTime;
        private String status;
        private String userId;
        private String hotelId;
        private String travelAgencyId;
        private String refundReason;
        private String rejectReason;
        private String type;
        private String categoryId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public double getRefundAmount() {
            return refundAmount;
        }

        public void setRefundAmount(double refundAmount) {
            this.refundAmount = refundAmount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDisposeTime() {
            return disposeTime;
        }

        public void setDisposeTime(String disposeTime) {
            this.disposeTime = disposeTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHotelId() {
            return hotelId;
        }

        public void setHotelId(String hotelId) {
            this.hotelId = hotelId;
        }

        public String getTravelAgencyId() {
            return travelAgencyId;
        }

        public void setTravelAgencyId(String travelAgencyId) {
            this.travelAgencyId = travelAgencyId;
        }

        public String getRefundReason() {
            return refundReason;
        }

        public void setRefundReason(String refundReason) {
            this.refundReason = refundReason;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
