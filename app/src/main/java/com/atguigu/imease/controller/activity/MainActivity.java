package com.atguigu.imease.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.fragment.ChatFragment;
import com.atguigu.imease.controller.fragment.ContactListFragment;
import com.atguigu.imease.controller.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {


    @Bind(R.id.rg_main)
    RadioGroup rgMain;

    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        initView();
        initData();
        initListener();
    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId) {
                    case R.id.rb_main_chat://会话界面
                        fragment = chatFragment;

                        break;
                    case R.id.rb_main_contact://联系人界面
                        fragment = contactListFragment;

                        break;
                    case R.id.rb_main_setting://设置界面
                        fragment = settingFragment;

                        break;
                }
                //切换对应的fragment
                switchFragment(fragment);
            }
        });
        //默认在会话界面
        rgMain.check(R.id.rb_main_chat);
    }

    /**
     * 实现fragment的切换方法
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        //获得getSupportFragmentManager 此类要继承FragmentActivity
        FragmentManager manager = getSupportFragmentManager();
        //开启事务，别忘记提交
        manager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData() {
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }
}
