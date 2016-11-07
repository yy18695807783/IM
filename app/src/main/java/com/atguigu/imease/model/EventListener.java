package com.atguigu.imease.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.atguigu.imease.model.bean.GroupInfo;
import com.atguigu.imease.model.bean.InvitationInfo;
import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.utils.Contancts;
import com.atguigu.imease.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;

/**
 * Created by 颜银 on 2016/11/4.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class EventListener {

    private final LocalBroadcastManager mLBM;
    private Context mContext;

    public EventListener(Context context) {

        mContext = context;

        //联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emConntactListener);

        //群组信息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

        //获得广播管理者
        mLBM = LocalBroadcastManager.getInstance(mContext);

    }

    //群组信息变化的监听
    private final EMGroupChangeListener groupChangeListener = new EMGroupChangeListener() {

        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName,groupId,inviter));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_INVITE);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applicant, String reason) {
            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName,groupId,applicant));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被接受
        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setGroup(new GroupInfo(groupName,groupId,accepter));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被拒绝
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            //更新数据
            InvitationInfo invitiation = new InvitationInfo();
            invitiation.setReason(reason);
            invitiation.setGroup(new GroupInfo(groupName,groupId,decliner));
            //邀请状态
            invitiation.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitiation);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被同意
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {

            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName) {
//            //更新数据
//
//            //发送红点
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//
//            //发送广播
//            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群被解散
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
//            //更新数据
//
//            //发送红点
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//
//            //发送广播
//            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            //更新数据
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(inviteMessage);
            invitationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
            //邀请状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //发送红点
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contancts.GROUP_INVITE_CHANGED));
        }
    };

    //-------------------------------------------------------------------------------------------------------------


    //联系人变化的监听
    private final EMContactListener emConntactListener = new EMContactListener() {
        //增加联系人监听回调
        @Override
        public void onContactAdded(String hxid) {

            // 更新数据库
            Model.getInstance().getDbHelperManager().getContactTableDao().saveContact(new UserInfor(hxid),true);

            //发送一个联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contancts.CONTACT_CHANGED));

        }

        //联系人删除监听回调
        @Override
        public void onContactDeleted(final String hxid) {

            // 更新数据库
            Model.getInstance().getDbHelperManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDbHelperManager().getInvitationTableDao().removeInvitation(hxid);

            //发送一个联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contancts.CONTACT_CHANGED));

        }

        //联系人邀请
        @Override
        public void onContactInvited(String hxid, String reason) {

            //更新数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            //邀请原因
            invitationInfo.setReason(reason);
            //邀请联系人状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
            //邀请人信息
            invitationInfo.setUser(new UserInfor(hxid));

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);
            Log.e("TAG", "bbbbbbbbb----"+ invitationInfo.toString());//有值

            //更新本地存储-有新的邀请信息-红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送一个邀请联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contancts.CONTACT_INVITE_CHANGED));


        }

        //同意邀请
        @Override
        public void onContactAgreed(String hxid) {

            //更新数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            //邀请联系人状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            invitationInfo.setReason("同意邀请了");
            //邀请人信息
            invitationInfo.setUser(new UserInfor(hxid));

            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

            //更新本地存储-有新的邀请信息-红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送一个邀请联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contancts.CONTACT_INVITE_CHANGED));


        }

        //拒绝邀请
        @Override
        public void onContactRefused(String s) {

            //更新数据库
            InvitationInfo invitationInfo = new InvitationInfo();
            //拒绝邀请原因
            invitationInfo.setReason("你太丑了");
            //邀请联系人状态
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_REJEST);//拒绝邀请
            //拒绝邀请人信息
            invitationInfo.setUser(new UserInfor(s));
            Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);


            //更新本地存储-有新的邀请信息-红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送一个邀请联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contancts.CONTACT_INVITE_CHANGED));

        }
    };

}
