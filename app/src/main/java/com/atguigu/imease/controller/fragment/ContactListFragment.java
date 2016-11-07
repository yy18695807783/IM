package com.atguigu.imease.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atguigu.imease.IMApplication;
import com.atguigu.imease.R;
import com.atguigu.imease.controller.activity.AddContactActivity;
import com.atguigu.imease.controller.activity.ChatActivity;
import com.atguigu.imease.controller.activity.GroupListActivity;
import com.atguigu.imease.controller.activity.InviteActivity;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.utils.Contancts;
import com.atguigu.imease.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：联系人Fragment
 */
public class ContactListFragment extends EaseContactListFragment {

    //好友邀请红点对像
    private ImageView redPoint;
//    @Bind(R.id.iv_invite_red)
//     public ImageView redPoint;

    //群组邀请红点对像
    private ImageView redGroupPoint;

    //好友邀请
    private LinearLayout ll_contact_invite;
    /**
     * 注册广播监听--邀请
     */
    private BroadcastReceiver inviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //显示红点
            redPoint.setVisibility(View.VISIBLE);
            //保存状态
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };
    //监听广播对象
    private LocalBroadcastManager mLBM;
    /**
     * 注册广播监听--联系人
     */
    private BroadcastReceiver contactsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Contancts.CONTACT_CHANGED) {
                //刷新页面
                contactsRefresh();
            }
        }
    };
    /**
     * 群组邀请信息变化的广播监听
     * 注册广播监听--群组信息
     */
//    private BroadcastReceiver groupsChangedReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            //联系人邀请信息变化  || 群邀请信息变化
////            if (intent.getAction() == Contancts.CONTACT_INVITE_CHANGED || intent.getAction() == Contancts.GROUP_INVITE_CHANGED) {
////                mInviteAdapter.refresh(Model.getInstance().getDbHelperManager().getInvitationTableDao().getInvitations());
////            }
//            //显示红点
//            redPoint.setVisibility(View.VISIBLE);
//            //保存状态
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
//        }
//    };


    //环信帐号的id
    private EaseUser mEaseUser;
    //群组点击对象
    private LinearLayout ll_contact_group;

    /**
     * 初始化视图
     */
    @Override
    protected void initView() {
        super.initView();
        Log.e("TAG", "111111111111111111");
        //添加加号到标题的右边
        titleBar.setRightImageResource(R.drawable.em_add);
        //创建头布局
        View headView = View.inflate(getActivity(), R.layout.fragment_contact_headview, null);
//        ButterKnife.bind(headView);

        //要有邀请的对象
        ll_contact_invite = (LinearLayout) headView.findViewById(R.id.ll_contact_invite);
        //群组点击对象
        ll_contact_group = (LinearLayout) headView.findViewById(R.id.ll_contact_group);

        //获取红点对像
        redPoint = (ImageView) headView.findViewById(R.id.iv_invite_red);
        redGroupPoint = (ImageView) headView.findViewById(R.id.iv_group_red);

        //添加头到ListView
        listView.addHeaderView(headView);

        // 1. listView绑定注册
        registerForContextMenu(listView);

        // 联系人条目的点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                //跳转页面
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                //是否专题参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());

                getActivity().startActivity(intent);
            }
        });
    }

    // 2.加载布局
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //获取当前item数据
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        mEaseUser = (EaseUser) listView.getItemAtPosition(position);
        //获取环信id
        String hxid = mEaseUser.getUsername();
        // 加载布局
        getActivity().getMenuInflater().inflate(R.menu.contact_delete, menu);

    }


    // 3.删除联系人
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //选择相应的item
        if (item.getItemId() == R.id.contact_delete) {
            // 删除选中的联系人
            deleteContact();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteContact() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //网上删除联系人信息
                    EMClient.getInstance().contactManager().deleteContact(mEaseUser.getNickname());

                    //本地数据库存储
                    Model.getInstance().getDbHelperManager().getContactTableDao().deleteContactByHxId(mEaseUser.getNickname());

                    //清楚邀请信息
                    Model.getInstance().getDbHelperManager().getInvitationTableDao().removeInvitation(mEaseUser.getNickname());

                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提示信息
                            Toast.makeText(getContext(), "删除联系人：" + mEaseUser.getNickname(), Toast.LENGTH_SHORT).show();

                            //刷新界面
                            contactsRefresh();
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();

                    if (getActivity() == null) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提示信息
                            Toast.makeText(getContext(), "删除联系人失败：" + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    /**
     * 设置视图
     */
    @Override
    protected void setUpView() {
        super.setUpView();

        initListener();
    }

    /**
     * 给视图设置监听事件
     */
    private void initListener() {

        //标题右边加号的点击监听
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //红点是否显示
        Boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        redPoint.setVisibility(isNewInvite ? View.VISIBLE : View.GONE);


        //好友邀请点击监听
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "好友邀请", Toast.LENGTH_SHORT).show();
                //点击先隐藏红点
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);
                redPoint.setVisibility(View.GONE);

                //跳转页面
                Intent intent = new Intent(getActivity(), InviteActivity.class);

                getActivity().startActivity(intent);
            }
        });

        //群组点击监听
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到群列表页面
                Intent intent = new Intent(getActivity(), GroupListActivity.class);

                getActivity().startActivity(intent);
            }
        });
        //注册监听广播---管理者
        mLBM = LocalBroadcastManager.getInstance(IMApplication.getContext());
        //联系人邀请信息变化的监听
        mLBM.registerReceiver(inviteChangedReceiver, new IntentFilter(Contancts.CONTACT_INVITE_CHANGED));
        //联系人变化的监听
        mLBM.registerReceiver(contactsChangedReceiver, new IntentFilter(Contancts.CONTACT_CHANGED));
        //群组邀请信息变化的监听
//        mLBM.registerReceiver(groupsChangedReceiver, new IntentFilter(Contancts.GROUP_INVITE_CHANGED));
        mLBM.registerReceiver(inviteChangedReceiver, new IntentFilter(Contancts.GROUP_INVITE_CHANGED));


        //网络获取联系人信息
        getContactFromHxServer();

    }

    private void getContactFromHxServer() {
        //网络获取联系人信息
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //网络获取联系人数据
                    List<String> allContacts = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    if (allContacts != null && allContacts.size() >= 0) {
                        List<UserInfor> users = new ArrayList<UserInfor>();
                        //便利
                        for (String hxid : allContacts) {
                            UserInfor userInfor = new UserInfor(hxid);
                            users.add(userInfor);
                        }

                        //保存到本地
                        Model.getInstance().getDbHelperManager().getContactTableDao().saveContacts(users, true);

                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                contactsRefresh();
                            }
                        });
                    }

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void contactsRefresh() {
        //从本地数据库获取联系人信息
        List<UserInfor> contacts = Model.getInstance().getDbHelperManager().getContactTableDao().getContacts();
        if (contacts != null && contacts.size() >= 0) {
            //设置适配器
            Map<String, EaseUser> contactsMap = new HashMap<>();

            EaseUser easeUser = null;
            for (UserInfor contact : contacts) {
                easeUser = new EaseUser(contact.getHxid());

                contactsMap.put(contact.getHxid(), easeUser);
            }

            // 设置数据
            setContactsMap(contactsMap);

            // 通知适配器数据变化，刷新列表
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //接注册广播
        if (mLBM != null) {
            mLBM.unregisterReceiver(inviteChangedReceiver);
            mLBM.unregisterReceiver(contactsChangedReceiver);
//            mLBM.unregisterReceiver(groupsChangedReceiver);
        }
    }
}
