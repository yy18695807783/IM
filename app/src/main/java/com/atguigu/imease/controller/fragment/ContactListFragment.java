package com.atguigu.imease.controller.fragment;

import android.content.Intent;
import android.view.View;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.activity.AddContactActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：联系人Fragment
 */
public class ContactListFragment extends EaseContactListFragment {

    /**
     * 初始化视图
     */
    @Override
    protected void initView() {
        super.initView();

        //添加加号到标题的右边
        titleBar.setRightImageResource(R.drawable.em_add);
        //创建头布局
        View headView = View.inflate(getActivity(), R.layout.fragment_contact_headview, null);
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
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

}
