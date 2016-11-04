package com.atguigu.imease.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.imease.IMApplication;
import com.atguigu.imease.R;
import com.atguigu.imease.controller.activity.AddContactActivity;
import com.atguigu.imease.controller.activity.InviteActivity;
import com.atguigu.imease.utils.Contancts;
import com.atguigu.imease.utils.SpUtils;
import com.hyphenate.easeui.ui.EaseContactListFragment;

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
     * 注册广播监听的回调
     */
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
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

        //获取红点对像
        redPoint = (ImageView) headView.findViewById(R.id.iv_invite_red);
        redGroupPoint = (ImageView) headView.findViewById(R.id.iv_group_red);

        //添加头到ListView
        listView.addHeaderView(headView);

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

        //监听广播
        mLBM = LocalBroadcastManager.getInstance(IMApplication.getContext());
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Contancts.CONTACT_INVITE_CHANGED));

    }

}
