package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "44444444444444444444444444");

        setContentView(R.layout.activity_add_invite);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Log.e("TAG", "5555555555555555555");
        inviteAdapter = new InviteAdapter(this, mOnInviteChangedListener);

        listviewAddinvite.setAdapter(inviteAdapter);

        refresh();
        //刷新方法
    }

    //刷新页面
    private void refresh() {
        Log.e("TAG", "6666666666666666666666666666");
        //获取数据库中的邀请信息
        List<InvitationInfo> invitationInfos = Model.getInstance().getDbHelperManager().getInvitationTableDao().getInvitations();

        Log.e("TAG", "取值7777777777777777777777---" + invitationInfos.toString());//空  没取出来
        inviteAdapter.refresh(invitationInfos);
    }
}
