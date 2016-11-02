package com.atguigu.imease.model.dao;

/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用：数据库AQ语句的常亮
 */
public class UserAccountTable {
    //create table tab_account(hxid text primary key,name text ,nick text,photo text);

    public static final String TAB_NAME = "tab_account";
    public static final String COL_NAME = "name";
    public static final String COL_HXID = "hxid";
    public static final String COL_NICK = "nick";
    public static final String COL_PHOTO = "photo";

    public static final String CREATE_TAB = "create table "
            + UserAccountTable.TAB_NAME + "( "
            + UserAccountTable.COL_HXID + " text primary key, "
            + UserAccountTable.COL_NAME + " text, "
            + UserAccountTable.COL_NICK + " text, "
            + UserAccountTable.COL_PHOTO + " text);";
}
