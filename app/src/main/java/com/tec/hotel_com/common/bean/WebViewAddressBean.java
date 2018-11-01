package com.tec.hotel_com.common.bean;

import java.io.Serializable;

/**
 * 作者：凌涛 on 2018/8/18 16:41
 * 邮箱：771548229@qq..com
 */
public class WebViewAddressBean implements Serializable{

    private String module;
    private LatlngBean latlng;
    private String poiaddress;
    private String poiname;
    private String cityname;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public LatlngBean getLatlng() {
        return latlng;
    }

    public void setLatlng(LatlngBean latlng) {
        this.latlng = latlng;
    }

    public String getPoiaddress() {
        return poiaddress;
    }

    public void setPoiaddress(String poiaddress) {
        this.poiaddress = poiaddress;
    }

    public String getPoiname() {
        return poiname;
    }

    public void setPoiname(String poiname) {
        this.poiname = poiname;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public static class LatlngBean implements Serializable {
        /**
         * lat : 22.94066
         * lng : 113.39154
         */

        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
