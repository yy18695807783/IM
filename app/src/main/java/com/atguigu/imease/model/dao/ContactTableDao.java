package com.atguigu.imease.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用： 联系人信息表的操作类
 */
public class ContactTableDao {

    private DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }

    // 获取所有联系人
    public List<UserInfor> getContacts() {
        //1.获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //2.执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<UserInfor> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserInfor userInfor = new UserInfor();
            userInfor.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfor.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfor.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfor.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            users.add(userInfor);
        }

        //3.关闭资源
        cursor.close();

        //4.返回数据
        return users;
    }

    // 通过环信id获取联系人单个信息
    public UserInfor getContactByHx(String hxId) {

        //校验
        if (hxId == null) {
            return null;
        }
        //1.获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //2.执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfor userInfor = null;
        if (cursor.moveToNext()) {
            userInfor = new UserInfor();
            userInfor.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfor.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfor.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfor.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
        }

        //3.关闭资源
        cursor.close();

        //4.返回数据
        return userInfor;

    }

    // 通过环信id获取用户联系人信息
    public List<UserInfor> getContactsByHx(List<String> hxIds) {
        //校验
        if (hxIds == null || hxIds.size() == 0) {
            return null;
        }

        //执行-->通过环信id获取联系人单个信息
        List<UserInfor> users = new ArrayList<>();
        for (String hxId : hxIds) {
            //查询个人信息
            UserInfor userInfo = getContactByHx(hxId);
            users.add(userInfo);
        }

        //返回数据
        return users;
    }

    // 保存单个联系人
    public void saveContact(UserInfor user, boolean isMyContact) {
        //检验
        if (user == null) {
            return;
        }

        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID, user.getHxid());
        values.put(ContactTable.COL_NAME, user.getName());
        values.put(ContactTable.COL_NICK, user.getNick());
        values.put(ContactTable.COL_PHOTO, user.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);//是否是联系人？是1：否0

        db.replace(ContactTable.TAB_NAME, null, values);
    }


    // 保存联系人信息
    public void saveContacts(List<UserInfor> contacts, boolean isMyContact) {
        //校验
        if (contacts == null || contacts.size() == 0) {
            return;
        }

        //循环保存联系人
        for (UserInfor user : contacts) {
            // 向数据库总添加一个用户
            saveContact(user, isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId) {
        //检验
        if (hxId == null) {
            return;
        }

        //获取数据库的链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行删除错做语句
        db.delete(ContactTable.TAB_NAME, ContactTable.COL_HXID + "=?", new String[]{hxId});
    }

}
