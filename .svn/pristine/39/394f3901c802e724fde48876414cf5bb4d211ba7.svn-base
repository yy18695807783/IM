package com.atguigu.imease.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.model.db.UserAccountDB;

/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用：用户信息操作类
 */
public class UserAccountDao {
    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        //创建数据库
        mHelper = new UserAccountDB(context);
    }


    /**
     * 添加数据
     */
    public void addAccount(UserInfor userInfor) {
        //1.获得数据库；链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //2.操作数据添加数据
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_NAME, userInfor.getName());
        values.put(UserAccountTable.COL_HXID, userInfor.getHxid());
        values.put(UserAccountTable.COL_NICK, userInfor.getNick());
        values.put(UserAccountTable.COL_PHOTO, userInfor.getPhoto());

        db.replace(UserAccountTable.TAB_NAME, null, values);
    }

    /**
     * 查询数据
     */
    public UserInfor getAccountByHxid(String hxid) {
        //1.获得数据库；链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //2.执行查询语句
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + UserAccountTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxid});
        UserInfor userInfor = null;

        if (cursor.moveToNext()) {
            //封装数据
            userInfor = new UserInfor();
            userInfor.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfor.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfor.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfor.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }
        //3.关闭资源
        cursor.close();
        //4.返回信息
        return userInfor;

    }
}
