package com.atguigu.imease.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.imease.model.dao.ContactTable;
import com.atguigu.imease.model.dao.InvitationTable;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：创建联系人和邀请信息数据库
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人数据库
        db.execSQL(ContactTable.CREATE_TABLE);

        //创建邀请信息数据库
        db.execSQL(InvitationTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
