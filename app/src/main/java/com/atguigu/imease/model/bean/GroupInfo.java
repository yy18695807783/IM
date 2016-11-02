package com.atguigu.imease.model.bean;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：群组bean类
 */
public class GroupInfo {
    private String groupName; //群名称
    private String groupId;//群ID
    private String invitePerson;//群邀请人

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String invitePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invitePerson = invitePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInvitePerson() {
        return invitePerson;
    }

    public void setInvitePerson(String invitePerson) {
        this.invitePerson = invitePerson;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", invitePerson='" + invitePerson + '\'' +
                '}';
    }
}
