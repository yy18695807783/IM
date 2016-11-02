package com.atguigu.imease;

import android.app.Application;

import com.atguigu.imease.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;


/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class IMApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAutoAcceptGroupInvitation(false);//不自动接受群邀请
        options.setAcceptInvitationAlways(false);//不总是一直接受所有邀请
        //初始化EaseUI
        EaseUI.getInstance().init(this,options);

        // 初始化模型层类
        Model.getInstance().init(this);

    }
}
