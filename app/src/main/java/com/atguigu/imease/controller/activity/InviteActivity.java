package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.adapter.InviteAdapter;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.InvitationInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友邀请页面
 */
public class InviteActivity extends Activity {

    @Bind(R.id.listview_addinvite)
    ListView listviewAddinvite;
    private InviteAdapter inviteAdapter;
    private InviteAdapter.OnInviteChangedListener mOnInviteChangedListener = new InviteAdapter.OnInviteChangedListener() {
        //接受邀请的回调
        @Override
        public void onAccept(final InvitationInfo invitationInfo) {
            //网络
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //环信服务器接受
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUser().getHxid());

                        //更新数据库,添加好友的信息
//                        Model.getInstance().getDbHelperManager().getContactTableDao().
//                                saveContact(new UserInfor(invitationInfo.getUser().getName()), true);
//                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT);

                        //本地 -- 更新数据库信息
                        Model.getInstance().getDbHelperManager().getInvitationTableDao()
                                .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT, invitationInfo.getUser().getHxid());

                        //发送提示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //内存
                                refresh();
                                //页面
                                Toast.makeText(InviteActivity.this, "接受好友邀请", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        //发送提示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受好友邀请失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //拒绝邀请的回调
        @Override
        public void onRejest(final InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    //网络--去环信服务器上拒绝邀请
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUser().getHxid());

                        //本地  --删除此人所有信息(邀请信息和联系人信息)
//                        Model.getInstance().getDbHelperManager().getInvitationTableDao()
//                                .updateInvitationStatus(InvitationInfo.InvitationStatus.);
                        Model.getInstance().getDbHelperManager().getInvitationTableDao().removeInvitation(invitationInfo.getUser().getHxid());
                        Model.getInstance().getDbHelperManager().getContactTableDao().deleteContactByHxId(invitationInfo.getUser().getHxid());


                        //本地 -- 更新数据库信息
                        Model.getInstance().getDbHelperManager().getInvitationTableDao()
                                .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_REJEST, invitationInfo.getUser().getHxid());


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新
                                refresh();
                                //页面
                                Toast.makeText(InviteActivity.this, "拒绝邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝邀请失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //接受群邀请
        @Override
        public void onAcceptInvite(final InvitationInfo invitationInfo) {
            //联网
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //联网环信服务器接受群邀请
                        //参数一：接受群组id  参数二：群组邀请人
                        EMClient.getInstance().groupManager()
                                .acceptInvitation(invitationInfo.getGroup().getGroupId(), invitationInfo.getGroup().getInvitePerson());

                        //本地数据库
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                        Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

                        //内存和页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受群邀请成功", Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受群邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //拒绝群邀请
        @Override
        public void onRejestInvite(final InvitationInfo invitationInfo) {
            //联网
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //网络
                        //参数一：邀请群组id  参数二：邀请人信息  参数三：拒绝原因
                        EMClient.getInstance().groupManager()
                                .declineInvitation(invitationInfo.getGroup().getGroupId(), invitationInfo.getGroup()
                                        .getInvitePerson(), "拒绝了求你邀请");

                        //本地数据库
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

                        //内存和页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝了群邀请", Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //接受群申请
        @Override
        public void onAcceptApplication(final InvitationInfo invitationInfo) {
            //联网
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //网络
                        //参数一：接受群组id  参数二：群组申请人
                        EMClient.getInstance().groupManager().acceptApplication(invitationInfo
                                .getGroup().getGroupId(), invitationInfo.getGroup().getInvitePerson());

                        //本地
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

                        //内存和页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "绝收群申请", Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群申请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //拒绝群申请
        @Override
        public void onRejestApplication(final InvitationInfo invitationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //网络
                        //参数一：邀请群组id  参数二：申请人信息  参数三：拒绝原因
                        EMClient.getInstance().groupManager().declineApplication(invitationInfo
                                .getGroup().getGroupId(), invitationInfo.getGroup().getInvitePerson(), "拒绝了群申请");

                        //本地
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDbHelperManager().getInvitationTableDao().addInvitation(invitationInfo);

                        //内存和页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群申请", Toast.LENGTH_SHORT).show();

                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群申请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_invite);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        inviteAdapter = new InviteAdapter(this, mOnInviteChangedListener);

        listviewAddinvite.setAdapter(inviteAdapter);

        refresh();
        //刷新方法
    }

    //刷新页面
    private void refresh() {
        //获取数据库中的邀请信息
        List<InvitationInfo> invitationInfos = Model.getInstance().getDbHelperManager().getInvitationTableDao().getInvitations();

        inviteAdapter.refresh(invitationInfos);
    }

}
