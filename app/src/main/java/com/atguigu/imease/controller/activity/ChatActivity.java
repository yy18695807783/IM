package com.atguigu.imease.controller.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.atguigu.imease.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

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
        manager.beginTransaction().replace(R.id.fl_chat,easeChatFragment).commit();

    }
}
