package com.atguigu.imease.model.dao;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class ContactTable {

    public static final String TAB_NAME = "tab_contact";//表名称
    public static final String COL_HXID = "hxid";//环信ID
    public static final String COL_NAME = "name";//用户名称
    public static final String COL_NICK = "nick";//用户昵称
    public static final String COL_PHOTO = "photo";//用户头像
    public static final String COL_IS_CONTACT = "is_contact";//是否是好友

    //创建表
    public static final String CREATE_TABLE = "create table "
            + ContactTable.TAB_NAME + " ("
            + ContactTable.COL_HXID + " text primary key, "
            + ContactTable.COL_NAME + " text, "
            + ContactTable.COL_NICK + " text, "
            + ContactTable.COL_PHOTO + " text, "
            + ContactTable.COL_IS_CONTACT + " text); ";

}
