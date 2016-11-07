package com.atguigu.imease.controller.fragment;

import android.content.Intent;

import com.atguigu.imease.controller.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：回话fragment
 */
public class ChatFragment extends EaseConversationListFragment {

    @Override
    protected void initView() {
        super.initView();

        //回话条目点击事件的监听
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                //跳转
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());

                //会话条目类型，默认为单人 ，要是群组走下面代码
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                }

                getActivity().startActivity(intent);
            }
        });


        //注册会话列表的监听
        EMClient.getInstance().chatManager().addMessageListener(mAddMessageListener);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    private EMMessageListener mAddMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //设置数据
            EaseUI.getInstance().getNotifier().onNewMesg(list);

            //刷新列表
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

}
