package com.atguigu.imease.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imease.model.bean.GroupInfo;
import com.atguigu.imease.model.bean.InvitationInfo;
import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：邀请信息表的操作类
 */
public class InvitationTableDao {

    private DBHelper mHelper;

    public InvitationTableDao(DBHelper helper) {
        mHelper = helper;
    }

    // 添加邀请
    public void addInvitation(InvitationInfo invitationInfo) {
        //校验
        if (invitationInfo == null) {
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行添加语句
        ContentValues values = new ContentValues();
        values.put(InvitationTable.COL_REASON, invitationInfo.getReason());//原因
        values.put(InvitationTable.COL_STATUS, invitationInfo.getStatus().ordinal());//状态的下角标

        //区分是好友邀请还是群组邀请
        UserInfor userInfor = invitationInfo.getUser();
        if (userInfor == null) {//群组
            values.put(InvitationTable.COL_GROUP_HXID, invitationInfo.getGroup().getGroupId());//群组ID
            values.put(InvitationTable.COL_GROUP_NAME, invitationInfo.getGroup().getGroupName());//群组名称
            values.put(InvitationTable.COL_USER_HXID, invitationInfo.getGroup().getInvitePerson());//群组邀请人
        } else {//好友
            values.put(InvitationTable.COL_USER_HXID, userInfor.getHxid());//好友ID
            values.put(InvitationTable.COL_USER_NAME, userInfor.getName());//好友名称
        }
        db.replace(InvitationTable.TABLE_NAME, null, values);
    }

    // 获取所有邀请信息
    public List<InvitationInfo> getInvitations() {
        //获取数据库可连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行查询语句
        String sql = "select * from " + InvitationTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        List<InvitationInfo> invitationInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            InvitationInfo invitationInfo = new InvitationInfo();
            //邀请原因
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            //邀请状态
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATUS))));
            //判断是否是群组
            String groupId = cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_HXID));
            if (groupId == null) {//好友
                UserInfor userInfor = new UserInfor();

                userInfor.setHxid(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_HXID)));//用户ID
                userInfor.setName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));//用户名称
                userInfor.setNick(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_NAME)));//用户昵称

                invitationInfo.setUser(userInfor);
            } else {//群组
                GroupInfo groupInfo = new GroupInfo();

                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_HXID)));//群组ID
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));//群组名称
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_USER_HXID)));//群组邀请人

                invitationInfo.setGroup(groupInfo);

            }
            invitationInfos.add(invitationInfo);
        }
        //关闭资源
        cursor.close();

        //返回数据
        return invitationInfos;
    }

    // 将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;
    }

    // 删除邀请
    public void removeInvitation(String hxId) {
        //校验
        if (hxId == null) {
            return;
        }

        //获取数据路链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行删除语句
        db.delete(InvitationTable.TABLE_NAME, InvitationTable.COL_USER_HXID + "=?", new String[]{hxId});
    }

    // 更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String hxId) {
        //校验
        if (hxId == null) {
            return;
        }

        //获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行更新语句
        ContentValues values = new ContentValues();
        values.put(InvitationTable.COL_STATUS, invitationStatus.ordinal());
        db.update(InvitationTable.TABLE_NAME, values, InvitationTable.COL_USER_HXID + "=?", new String[]{hxId});
    }
}
