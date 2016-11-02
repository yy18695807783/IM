package com.atguigu.imease.model.dao;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：创建邀请信息的建表类
 */
public class InvitationTable {
    public static final String TABLE_NAME = "tab_invite";//表名
    public static final String COL_USER_NAME = "user_name";//用户名称
    public static final String COL_USER_HXID = "user_hxid";//用户ID
    public static final String COL_GROUP_NAME = "group_name";//群组名称
    public static final String COL_GROUP_HXID = "group_hxid";//群组ID
    public static final String COL_REASON = "reaason";//邀请原因
    public static final String COL_STATUS = "status";//邀请状态

    //创建表
    public static final String CREATE_TABLE = "create table "
            + InvitationTable.TABLE_NAME + " ("
            + InvitationTable.COL_USER_HXID + " text primary key, "
            + InvitationTable.COL_USER_NAME + " text, "
            + InvitationTable.COL_GROUP_NAME + " text, "
            + InvitationTable.COL_GROUP_HXID + " text, "
            + InvitationTable.COL_REASON + " text, "
            + InvitationTable.COL_STATUS + " integer); ";
}
