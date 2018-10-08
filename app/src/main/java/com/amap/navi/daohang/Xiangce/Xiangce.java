package com.amap.navi.daohang.Xiangce;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by THINK on 2018/6/18.
 */

public class Xiangce extends BmobObject {

    private String userid;
    private BmobFile tupian;
    private String lujing;
    private Number lat;
    private Number lng;
    private String address;
    private String intruduce;

    public String getLujing() {
        return lujing;
    }

    public void setLujing(String lujing) {
        this.lujing = lujing;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BmobFile getTupian() {
        return tupian;
    }

    public void setTupian(BmobFile tupian) {
        this.tupian = tupian;
    }

    public Number getLat() {
        return lat;
    }

    public void setLat(Number lat) {
        this.lat = lat;
    }

    public Number getLng() {
        return lng;
    }

    public void setLng(Number lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntruduce() {
        return intruduce;
    }

    public void setIntruduce(String intruduce) {
        this.intruduce = intruduce;
    }
}
