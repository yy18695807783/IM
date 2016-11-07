package com.atguigu.imease.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.atguigu.imease.R;
import com.atguigu.imease.model.bean.InvitationInfo;
import com.atguigu.imease.model.bean.UserInfor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 颜银 on 2016/11/4.
 * QQ:443098360
 * 微信：y443098360
 * 作用：好友邀请适配器
 */
public class InviteAdapter extends BaseAdapter {

    private final Context mContext;

    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();

    public InviteAdapter(Context context, OnInviteChangedListener mOnInviteChangedListener) {
        mContext = context;
        this.mOnInviteChangedListener = mOnInviteChangedListener;
    }

    public void refresh(List<InvitationInfo> invitationInfos) {
        if (invitationInfos != null && invitationInfos.size() >= 0) {
            //清空在添加
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invitationInfos);
            //刷新适配器
            notifyDataSetChanged();
            Log.e("TAG", "适配器6666666666666666666666---" + invitationInfos.size());
        }
    }

    @Override
    public int getCount() {
        Log.e("TAG", "适配器aaaaaaaaaaaaaaaaaaa" + mInvitationInfos.size());
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {

        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1.创建ViewHolder
        ViewHolder viewHolder = null;
        //2.初始化convertView及复用
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_invite, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //3.装配数据
        final InvitationInfo invitationInfo = mInvitationInfos.get(position);

        UserInfor user = invitationInfo.getUser();
        if (user != null) {//好友邀请
            viewHolder.name.setText(user.getName());

            //隐藏接受和拒绝按钮
            viewHolder.btn_invite_rejest.setVisibility(View.GONE);
            viewHolder.btn_invite_accept.setVisibility(View.GONE);

            if (InvitationInfo.InvitationStatus.NEW_INVITE == invitationInfo.getStatus()) { //新邀请信息
                //显示接受和拒绝按钮
                viewHolder.btn_invite_rejest.setVisibility(View.VISIBLE);
                viewHolder.btn_invite_accept.setVisibility(View.VISIBLE);

//                if (invitationInfo.getReason() != null) {
//                    viewHolder.reason.setText(invitationInfo.getReason());
//                } else {
//                    viewHolder.reason.setText("新邀请信息");
//                }
                    viewHolder.reason.setText("新邀请信息");


                //设置监听
                viewHolder.btn_invite_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteChangedListener.onAccept(invitationInfo);
                    }
                });
                viewHolder.btn_invite_rejest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteChangedListener.onRejest(invitationInfo);
                    }
                });

            } else if (InvitationInfo.InvitationStatus.INVITE_ACCEPT == invitationInfo.getStatus()) { //接受邀请信息
//                if (invitationInfo.getReason() != null) {
//                    viewHolder.reason.setText(invitationInfo.getReason());
//                } else {
//                    viewHolder.reason.setText("接受邀请信息");
//                }
                    viewHolder.reason.setText("接受邀请信息");

            } else if (InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER == invitationInfo.getStatus()) { //邀请信息被接受
//                if (invitationInfo.getReason() != null) {
//                    viewHolder.reason.setText(invitationInfo.getReason());
//                } else {
//                    viewHolder.reason.setText("邀被接受");
//                }
                    viewHolder.reason.setText("邀被接受");

            } else if (InvitationInfo.InvitationStatus.INVITE_REJEST == invitationInfo.getStatus()) { //邀请信息被拒绝
//                if (invitationInfo.getReason() != null) {
//                    viewHolder.reason.setText(invitationInfo.getReason());
//                } else {
//                    viewHolder.reason.setText("拒绝你");
//                }
                    viewHolder.reason.setText("拒绝你");

            }


        } else {//群邀请

            //群邀请的邀请人名称
            viewHolder.name.setText(invitationInfo.getGroup().getInvitePerson());

            //隐藏接受和拒绝按钮
            viewHolder.btn_invite_rejest.setVisibility(View.GONE);
            viewHolder.btn_invite_accept.setVisibility(View.GONE);

            //群组邀请原因
            switch (invitationInfo.getStatus()) {
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    Log.e("TAG2", "适配器1111111111");
                    viewHolder.reason.setText("您的群申请请已经被接受");
                    break;

                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    Log.e("TAG2", "适配器22222222222");
                    viewHolder.reason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    viewHolder.reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    viewHolder.reason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    Log.e("TAG2", "适配器33333333");
                    viewHolder.reason.setText("您收到了群邀请");

                    //显示接受和拒绝按钮
                    viewHolder.btn_invite_rejest.setVisibility(View.VISIBLE);
                    viewHolder.btn_invite_accept.setVisibility(View.VISIBLE);

                    //接受群邀请
                    viewHolder.btn_invite_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteChangedListener.onAcceptInvite(invitationInfo);
                        }
                    });

                    //拒绝群邀请
                    viewHolder.btn_invite_rejest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteChangedListener.onRejestInvite(invitationInfo);
                        }
                    });
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    viewHolder.reason.setText("您收到了群申请");

                    //显示接受和拒绝按钮
                    viewHolder.btn_invite_rejest.setVisibility(View.VISIBLE);
                    viewHolder.btn_invite_accept.setVisibility(View.VISIBLE);

                    //接受群申请
                    viewHolder.btn_invite_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteChangedListener.onAcceptApplication(invitationInfo);
                        }
                    });

                    //拒绝群申请
                    viewHolder.btn_invite_rejest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteChangedListener.onRejestApplication(invitationInfo);
                        }
                    });
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    Log.e("TAG2", "适配器444444444444");
                    viewHolder.reason.setText("你接受了群邀请");
                    break;

                // 您批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    viewHolder.reason.setText("您批准了群申请");
                    break;

                // 你拒绝了群邀请
                case GROUP_REJECT_INVITE:
                    viewHolder.reason.setText("你拒绝了群邀请");
                    break;

                // 您拒绝了群申请
                case GROUP_REJECT_APPLICATION:
                    viewHolder.reason.setText("您拒绝了群申请");
                    break;
            }


        }


//        //设置监听
//        viewHolder.btn_invite_accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnInviteChangedListener.onAccept(invitationInfo);
//            }
//        });
//        viewHolder.btn_invite_rejest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnInviteChangedListener.onRejest(invitationInfo);
//            }
//        });

        //4返回数据
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_invite_name)
        public TextView name;

        @Bind(R.id.tv_invite_reason)
        public TextView reason;

        @Bind(R.id.btn_invite_accept)
        public Button btn_invite_accept;

        @Bind(R.id.btn_invite_rejest)
        public Button btn_invite_rejest;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnInviteChangedListener {
        //接受的监听回调方法
        void onAccept(InvitationInfo invitationInfo);

        //拒绝的监听回调方法
        void onRejest(InvitationInfo invitationInfo);

        //接受群邀请
        void onAcceptInvite(InvitationInfo invitationInfo);

        //拒绝群邀请
        void onRejestInvite(InvitationInfo invitationInfo);

        //接受群申请
        void onAcceptApplication(InvitationInfo invitationInfo);

        //拒绝群申请
        void onRejestApplication(InvitationInfo invitationInfo);
    }

    private OnInviteChangedListener mOnInviteChangedListener;

//    /**
//     * 接受和拒绝按钮的监听回调设置方法
//     *
//     * @return
//     */
//    public void setmOnInviteChangedListener(OnInviteChangedListener mOnInviteChangedListener) {
//        this.mOnInviteChangedListener = mOnInviteChangedListener;
//    }
}
