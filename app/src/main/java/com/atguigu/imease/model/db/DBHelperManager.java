package com.atguigu.imease.model.db;

import android.content.Context;

import com.atguigu.imease.model.dao.ContactTableDao;
import com.atguigu.imease.model.dao.InvitationTableDao;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class DBHelperManager {

    private final DBHelper mHelper;
    private final ContactTableDao contactTableDao;
    private final InvitationTableDao invitationTableDao;

    public DBHelperManager(Context context, String name) {
        //登陆成功时创建数据库，数据库的名字为登陆的名字
        mHelper = new DBHelper(context, name);

        //初始化
        contactTableDao = new ContactTableDao(mHelper);
        invitationTableDao = new InvitationTableDao(mHelper);
    }

    public ContactTableDao getContactTableDao() {
        return contactTableDao;
    }

    public InvitationTableDao getInvitationTableDao() {
        return invitationTableDao;
    }

    public void close() {
        mHelper.close();
    }
}
