package com.tec.hotel_com.widget.city_modle.bean;

import java.util.ArrayList;

/**
 * 作者：凌涛 on 2018/10/17 10:08
 * 邮箱：771548229@qq..com
 */
public class CityBean {

    private String id;
    private String name;
    private String pinYin;
    private Double gisGcj02Lat;
    private Double gisGcj02Lng;
    private Double gisBd09Lat;
    private Double gisBd09Lng;
    private String zipcode;
    private ArrayList<DistrictBean> cityList;

    public CityBean() {
    }

    public String getId() {
        return this.id == null ? "" : this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYin() {
        return this.pinYin == null ? "" : this.pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public Double getGisGcj02Lat() {
        return this.gisGcj02Lat == null ? new Double(0.0D) : this.gisGcj02Lat;
    }

    public void setGisGcj02Lat(Double gisGcj02Lat) {
        this.gisGcj02Lat = gisGcj02Lat;
    }

    public Double getGisGcj02Lng() {
        return this.gisGcj02Lng == null ? new Double(0.0D) : this.gisGcj02Lng;
    }

    public void setGisGcj02Lng(Double gisGcj02Lng) {
        this.gisGcj02Lng = gisGcj02Lng;
    }

    public Double getGisBd09Lat() {
        return this.gisBd09Lat == null ? new Double(0.0D) : this.gisBd09Lat;
    }

    public void setGisBd09Lat(Double gisBd09Lat) {
        this.gisBd09Lat = gisBd09Lat;
    }

    public Double getGisBd09Lng() {
        return this.gisBd09Lng == null ? new Double(0.0D) : this.gisBd09Lng;
    }

    public void setGisBd09Lng(Double gisBd09Lng) {
        this.gisBd09Lng = gisBd09Lng;
    }

    public String getZipcode() {
        return this.zipcode == null ? "" : this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public ArrayList<DistrictBean> getCityList() {
        return this.cityList;
    }

    public void setCityList(ArrayList<DistrictBean> cityList) {
        this.cityList = cityList;
    }

    public String toString() {
        return this.name;
    }
}
