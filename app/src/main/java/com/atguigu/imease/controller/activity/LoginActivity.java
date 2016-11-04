package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.UserInfor;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆界面
 */
public class LoginActivity extends Activity {

    @Bind(R.id.ed_login_name)
    EditText edLoginName;
    @Bind(R.id.ed_login_pwd)
    EditText edLoginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    /**
     * 注册点击事件
     *
     * @param view
     */
    @OnClick(R.id.btn_login_regist)
    void btn_login_regist_click(View view) {
//        Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();
        // 获取用户名和密码
        final String registName = edLoginName.getText().toString();
        final String registPwd = edLoginPwd.getText().toString();
        // 校验
        if (TextUtils.isEmpty(registName) || TextUtils.isEmpty(registPwd)) {
            Toast.makeText(LoginActivity.this, "帐号密码不能为空", Toast.LENGTH_SHORT).show();
            //返回
            return;
        }
        // 去服务器注册
        // 进度条
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在注册中...");
        dialog.show();
        // 访问网络
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //在环信服务器上创建帐号
                    EMClient.getInstance().createAccount(registName, registPwd);

                    //提示注册成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //同时隐藏提示的dialog
                            dialog.dismiss();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    //提示注册失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            //同时隐藏提示的dialog
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    /**
     * 登陆点击事件
     *
     * @param view
     */
    @OnClick(R.id.btn_login_login)
    void btn_login_login_click(View view) {
//        Toast.makeText(LoginActivity.this, "登陆", Toast.LENGTH_SHORT).show();
        // 获取用户名和密码
        final String loginName = edLoginName.getText().toString();
        final String loginPwd = edLoginPwd.getText().toString();
        // 校验
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPwd)) {
            Toast.makeText(LoginActivity.this, "帐号密码不能为空", Toast.LENGTH_SHORT).show();
            //返回
            return;
        }
        // 去服务器注册
        // 进度条
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在登陆中...");
        dialog.show();
        // 访问网络
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //记录成功了-创建数据库
                                Model.getInstance().loginSucess(new UserInfor(loginName));
                                //提示登陆成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                //保存数据库-登陆成功
                                Model.getInstance().getmUserAccountDao().addAccount(new UserInfor(loginName));
                                //跳转界面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                //同时隐藏提示的dialog
                                dialog.dismiss();
                                //销毁当前页面
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onError(int i, final String s) {

                        //失败处理
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败" + s.toString(), Toast.LENGTH_SHORT).show();
                                //同时隐藏提示的dialog
                                dialog.dismiss();
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
