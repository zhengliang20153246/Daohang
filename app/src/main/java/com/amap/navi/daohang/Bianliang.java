package com.amap.navi.daohang;

import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.navi.daohang.User.User;

/**
 * Created by THINK on 2018/5/1.
 */

public class Bianliang {
    private static String city="";
    private static LatLonPoint start;
    private static LatLonPoint end;

    private static NaviLatLng start1;
    private static NaviLatLng end1;
    private static LatLonPoint latLonPointstart;
    private static LatLonPoint latLonPointend;

    private static String searchpoi;

    public static String getSearchpoi() {
        return searchpoi;
    }

    public static void setSearchpoi(String searchpoi) {
        Bianliang.searchpoi = searchpoi;
    }

    private static String  userID;

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        Bianliang.userID = userID;
    }

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Bianliang.user = user;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        Bianliang.city = city;
    }

    public static LatLonPoint getStart() {
        return start;
    }

    public static void setStart(LatLonPoint start) {
        Bianliang.start = start;
    }

    public static LatLonPoint getEnd() {
        return end;
    }

    public static void setEnd(LatLonPoint end) {
        Bianliang.end = end;
    }

    public static NaviLatLng getStart1() {
        return start1;
    }

    public static void setStart1(NaviLatLng start1) {
        Bianliang.start1 = start1;
    }

    public static NaviLatLng getEnd1() {
        return end1;
    }

    public static void setEnd1(NaviLatLng end1) {
        Bianliang.end1 = end1;
    }

    public static LatLonPoint getLatLonPointstart() {
        return latLonPointstart;
    }

    public static void setLatLonPointstart(LatLonPoint latLonPointstart) {
        Bianliang.latLonPointstart = latLonPointstart;
    }

    public static LatLonPoint getLatLonPointend() {
        return latLonPointend;
    }

    public static void setLatLonPointend(LatLonPoint latLonPointend) {
        Bianliang.latLonPointend = latLonPointend;
    }
}
