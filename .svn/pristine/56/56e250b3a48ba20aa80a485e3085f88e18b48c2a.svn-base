package com.atguigu.imease.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.imease.model.dao.UserAccountTable;

/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用： 用户数据库操作
 */
public class UserAccountDB extends SQLiteOpenHelper{


    public UserAccountDB(Context context) {
        super(context, "user_account.da", null, 1);
    }

    /**
     * 创建数据库的时候用
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table tab_account(hxid text primary key,name text ,nick text,photo text);");
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    /**
     * 调用数据的时候调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
