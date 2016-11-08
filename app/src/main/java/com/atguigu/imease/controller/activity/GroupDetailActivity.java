package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.atguigu.imease.IMApplication;
import com.atguigu.imease.R;
import com.atguigu.imease.controller.adapter.GroupDetailAdapter;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.utils.Contancts;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupDetailActivity extends Activity {

    @Bind(R.id.gv_group_detail)
    GridView gvGroupDetail;
    @Bind(R.id.bt_group_detail)
    Button btGroupDetail;
    private EMGroup mGroup;
    private LocalBroadcastManager mLBM;

    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        //添加群成员
        @Override
        public void onAddMembers() {
            //跳转到选择添加群成员页面
            Intent intent = new Intent(GroupDetailActivity.this, PickContactsActivity.class);

            //携带参数---群组id
            intent.putExtra(Contancts.GROUP_ID, mGroup.getGroupId());

            startActivityForResult(intent, 2);
        }

        //删除群成员
        @Override
        public void onDeleteMembers(final UserInfor user) {
            //联网
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //网络服务器删除群成员
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(), user.getHxid());

                        //刷新
                        getMembersFromHxServer();

                        //内存和页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除群成员成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除群成员失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };
    private GroupDetailAdapter groupDetailAdapter;
    private List<UserInfor> mUsers;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "1111111111111111111111111");
        if (resultCode == RESULT_OK) {

            //获取但会的联系人---用data接受
            final String[] memberses = data.getStringArrayExtra("members");
            Log.e("TAG", "222222222--memberses ==" + memberses.toString());

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //网络  环信服务器添加群组好友
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroup.getGroupId(), memberses);

                        //刷新
                        getMembersFromHxServer();

                        //页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送邀请群成员成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送邀请群成员失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);

        mLBM = LocalBroadcastManager.getInstance(IMApplication.getContext());

        getData();

        //初始化按钮
        initButtonDisplay();

        //初始化GridView
        initGridView();

        //从安魂新服务器获取群成员数据集合
        getMembersFromHxServer();

        initListener();
    }

    private void initListener() {
        gvGroupDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                            //按下判断是否为删除模式---是删除模式，触摸变成非触摸模式 刷新
                        if(groupDetailAdapter.ismIsDeleteModel()){
                            groupDetailAdapter.setmIsDeleteModel(false);
                            //刷新
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void getMembersFromHxServer() {
        //联网
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //网络  获取群组成员集合
                    EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(mGroup.getGroupId());

                    //转化
                    List<String> members = groupFromServer.getMembers();

                    if (members != null && members.size() >= 0) {

                        mUsers = new ArrayList<UserInfor>();

                        for (String member : members) {
                            UserInfor userInfor = new UserInfor(member);
                            mUsers.add(userInfor);
                        }
                    }

                    //内存页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupDetailAdapter.refresh(mUsers);
                            Toast.makeText(GroupDetailActivity.this, "加载群成员数据成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this, "加载群成员数据失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //初始化GridView
    private void initGridView() {
        // 你是群主 或者 你这个群是公开  你就可以添加和删除群成员
        boolean mIsCanModify = EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner()) || mGroup.isPublic();

        groupDetailAdapter = new GroupDetailAdapter(this, mIsCanModify, mOnGroupDetailListener);

        gvGroupDetail.setAdapter(groupDetailAdapter);
    }

    private void initButtonDisplay() {
        //显示
        //先判断是否谁群主
        if (EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())) {
            //是群主---更新button为解散群
            btGroupDetail.setText("解散群");

            //执行button的点击事件
            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //联网
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //网络---服务器解散群
                                EMClient.getInstance().groupManager().destroyGroup(mGroup.getGroupId());

                                // 发送解散群广播
                                exitGroupBroadCast();

                                //提示界面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群成功", Toast.LENGTH_SHORT).show();

                                        //销毁当前页面
                                        finish();
                                    }
                                });

                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {
            //是群成员
            //更新显示
            btGroupDetail.setText("退出群");

            //点击事件
            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //联网
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //联网---服务器退出群
                                EMClient.getInstance().groupManager().leaveGroup(mGroup.getGroupId());

                                // 发送退群广播
                                exitGroupBroadCast();

                                //提示界面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退出群成功", Toast.LENGTH_SHORT).show();

                                        //销毁当前页面
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退出群失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    private void exitGroupBroadCast() {
        // 发送广播
        Intent intent = new Intent(Contancts.EXIT_GROUP);
        // 传递群id参数
        intent.putExtra(Contancts.GROUP_ID, mGroup.getGroupId());
        // 发送

        mLBM.sendBroadcast(intent);
    }

    public void getData() {

        final String groupId = getIntent().getStringExtra(Contancts.GROUP_ID);
        if (groupId == null) {
            finish();
            return;
        } else {
            //获取群详情信息
            mGroup = EMClient.getInstance().groupManager().getGroup(groupId);
        }

    }
}
