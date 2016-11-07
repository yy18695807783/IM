package com.atguigu.imease.model.bean;

/**
 * Created by 颜银 on 2016/11/6.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class PickContactsInfo {

    private UserInfor user;//联系人
    private boolean isChecked;//是否被选中

    public PickContactsInfo() {
    }

    public PickContactsInfo(UserInfor user, Boolean isChecked) {
        this.user = user;
        this.isChecked = isChecked;
    }

    public UserInfor getUser() {
        return user;
    }

    public void setUser(UserInfor user) {
        this.user = user;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
}
