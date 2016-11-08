package com.atguigu.imease.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;

import com.atguigu.imease.IMApplication;
import com.atguigu.imease.R;
import com.atguigu.imease.utils.Contancts;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends FragmentActivity {

    @Bind(R.id.fl_chat)
    FrameLayout flChat;
    //环信聊天fragment
    private EaseChatFragment easeChatFragment;
    //聊天类型
    private int chatType;
    //聊天对象的环信id
    private String hxid;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver mExistBroadcastreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        initData();

        initListener();
    }

    private void initData() {
        //创建聊天页面  用环信自带的
        easeChatFragment = new EaseChatFragment();

        //获取聊天对象信息
        hxid = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        // 获取聊天类型
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        // 设置参数
        easeChatFragment.setArguments(getIntent().getExtras());

        //替换fragment的会话页面
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_chat, easeChatFragment).commit();
    }

    private void initListener() {
        easeChatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }

            @Override
            public void onEnterToChatDetails() {

                //跳转到群组详情页面
                Intent intent = new Intent(ChatActivity.this, GroupDetailActivity.class);

                //携带参数
                intent.putExtra(Contancts.GROUP_ID, hxid);
                startActivity(intent);
            }

            @Override
            public void onAvatarClick(String username) {

            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });

        // 如果当前是群聊我再注册监听
        if (chatType == EaseConstant.CHATTYPE_GROUP) {

            mLBM = LocalBroadcastManager.getInstance(IMApplication.getContext());

            mExistBroadcastreceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (hxid.equals(intent.getStringExtra(Contancts.GROUP_ID))) {
                        //销毁当前聊天界面
                        finish();
                    }
                }
            };

            mLBM.registerReceiver(mExistBroadcastreceiver, new IntentFilter(Contancts.EXIT_GROUP));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLBM != null) {
            mLBM.unregisterReceiver(mExistBroadcastreceiver);
        }

    }
}
