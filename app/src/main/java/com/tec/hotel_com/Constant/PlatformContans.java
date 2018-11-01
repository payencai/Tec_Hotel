package com.tec.hotel_com.Constant;

/**
 * 作者：凌涛 on 2018/8/6 09:38
 * 邮箱：771548229@qq..com
 */
public class PlatformContans {

    //    public static String root = "http://192.168.3.29:8083";//测试地址
//    public static String root = "http://47.107.55.94:8083";//正式地址
    public static String root = "https://www.ynshake.com:8443";//正式地址

    public static class HotelUser {
        public static final String login = root + "/hotel_user/login";
        public static final String updatePassword = root + "/hotel_user/updatePassword";
    }

    public static class Hotel {
        public static final String getMyInfo = root + "/hotel/getMyInfo";
        public static final String uploadImage = root + "/hotel/uploadImage";//更新酒店图片接口
        public static final String updateIntroduce = root + "/hotel/updateIntroduce";//酒店端更改介绍
        public static final String updateLocation = root + "/hotel/updateLocation";//更新地址
        public static final String updateContactNum = root + "/hotel/updateContactNum";//更新电话号码
        public static final String getUserOrdersForHotel = root + "/hotel/order/getOrdersForHotel";//酒店获取用户订单列表
        public static final String getLxsOrdersForHotel = root + "/hotel/order_of_travel_agency/getOrdersForHotel";//酒店获取旅行社订单列表

        public static final String getForUserHotel = root + "/hotel/order/getForHotel";//酒店获取用户订单详情
        public static final String getForLxsHotel = root + "/hotel/order_of_travel_agency/getForHotel";//酒店获取旅行社订单详情
        public static final String getNotice = root + "/push/hotel/getMyMessage";//酒店获取通知
        public static final String getOrderNum = root + "/hotel/order/getOrdersNumberForHotel";// 酒店获取订单数量
        public static final String getInnerImage = root + "/hotel/getInnerImage";// 酒店获取内景图片
        public static final String getOuterImage = root + "/hotel/getOuterImage";// 酒店获取外景图片
        public static final String upInnerImage = root + "/hotel/uploadInnerImage";// 酒店获取内景图片
        public static final String upOuterImage = root + "/hotel/uploadOuterImage";// 酒店获取外景图片
        public static final String getImageNum = root + "/hotel/getImagesNumber";// 酒店获取图片数量

    }

    public static class Roomtype {
        public static final String getMine = root + "/roomtype/getMine";//获取房型信息
        public static final String add = root + "/roomtype/add";//获取房型
        public static final String edit = root + "/roomtype/edit";//修改获取房型
        public static final String delete = root + "/roomtype/deleteById";//修改获取房

    }

    public static class Image {
        public static final String uploadImage = root + "/image/uploadImage";//上传图片
    }

    public static class Statistics {
        public static final String getOccupancyRateThisMonthForHotelApp = root + "/statistics/getOccupancyRateThisMonthForHotelApp";//上传图片
        public static final String countOrdersForHotelApp = root + "/statistics/countOrdersForHotelApp";//酒店app统计订单状况
        public static final String CountBydayForHotelApp = root + "/statistics/CountBydayForHotelApp";//酒店app按天统计数据
        public static final String countBydayForHotelApp = root + "/statistics/countBydayForHotelApp";//酒店app按天统计数据(参数格式yyyy-MM-dd)
        public static final String countBymonthForHotelApp = root + "/statistics/countBymonthForHotelApp";//酒店app按月统计数据(参数格式yyyy-MM)
        public static final String countByyearForHotelApp = root + "/statistics/countByyearForHotelApp";//酒店app按年统计数据(参数格式yyyy)
        public static final String countCountRecentlyForHotelApp = root + "/statistics/countCountRecentlyForHotelApp";//酒店app统计最近几天的营业额数据(参数格式yyyy)
    }

    public static class Roomnum {
        public static final String getRoomnumWithDateByRoomType = root + "/roomnum/getRoomnumWithDateByRoomType";//上传图片
        public static final String updateRoomnumByRoomTypeAndDate = root + "/roomnum/updateRoomnumByRoomTypeAndDate";//上传图片
    }


}
