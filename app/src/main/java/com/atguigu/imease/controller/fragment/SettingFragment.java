package com.atguigu.imease.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.activity.LoginActivity;
import com.atguigu.imease.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by 颜银 on 2016/11/2.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class SettingFragment extends Fragment {
    private Button btn_setting_loginout;

    private boolean isOut = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            isOut = true;
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);
        btn_setting_loginout = (Button) view.findViewById(R.id.btn_setting_loginout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }


    private void initData() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String currentUser = EMClient.getInstance().getCurrentUser();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn_setting_loginout.setText("退出登录（" + currentUser + "）");
                    }
                });
            }
        });
    }

    private void initListener() {
        //退出按钮监听
        btn_setting_loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOut) {
                    isOut = false;
                    Toast.makeText(getActivity(), "在按一次退出", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(1, 2000);
                } else {
                    //退出的方法  isOut = false时才进入

                    //退出前移除所有消息
                    handler.removeCallbacksAndMessages(null);

                    logOut();
                }
            }
        });
    }

    private void logOut() {
        //联网退出环信帐号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        //主线程提示
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "退出登陆成功", Toast.LENGTH_SHORT).show();
                                //跳转登陆页面
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                getActivity().startActivity(intent);
                                //销毁当前页面
                                getActivity().finish();
                            }
                        });

                    }

                    @Override
                    public void onError(int i, final String s) {
                        //主线程提示
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "退出登陆失败" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
}
