package com.atguigu.imease.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.atguigu.imease.model.bean.InvitationInfo;
import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.utils.Contancts;
import com.atguigu.imease.utils.SpUtils;
import com.hyphenate.EMContactListener;
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

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(mContext);

    }



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
