package com.amap.navi.daohang.User;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by LI on 2017/11/12.
 */

public class User extends BmobObject {
    private String username;
    private String password;
    private String phone;
    private String qq;
    private String sex;
    private String weixin;
    private BmobFile pic;
    private String nicheng;
    private Number jifen;
    private String qiandaoshijian;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQiandaoshijian() {
        return qiandaoshijian;
    }

    public void setQiandaoshijian(String qiandaoshijian) {
        this.qiandaoshijian = qiandaoshijian;
    }

    public Number getJifen() {
        return jifen;
    }

    public void setJifen(Number jifen) {
        this.jifen = jifen;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
