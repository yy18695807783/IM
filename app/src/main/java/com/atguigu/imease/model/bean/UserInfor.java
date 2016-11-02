package com.atguigu.imease.model.bean;

/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用：用户信息类
 */
public class UserInfor {

    private String name;
    private String hxid;
    private String nick;
    private String photo;

    public UserInfor() {
    }

    public UserInfor(String name) {
        this.name = name;
        this.hxid = name;
        this.nick = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserInfor{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
