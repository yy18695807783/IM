package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.UserInfor;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends Activity {

    @Bind(R.id.et_add_name)
    EditText etAddName;
    @Bind(R.id.tv_add_name)
    TextView tvAddName;
    @Bind(R.id.rl_add)
    RelativeLayout rlAdd;
    private UserInfor userInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

    }

    /**
     * 查询按钮点击监听
     */
    @OnClick(R.id.tv_add_search)
    void tv_add_search_click(View v) {
        //1.获取信息
        String searchName = etAddName.getText().toString();
        //2.校验
        if (TextUtils.isEmpty(searchName)) {
            Toast.makeText(AddContactActivity.this, "查询信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //3.网上获取超找是否存在联系人
        userInfor = new UserInfor(searchName);

        //4.显示超找到的联系人
        rlAdd.setVisibility(View.VISIBLE);
        tvAddName.setText(userInfor.getName());
    }

    /**
     * 添加按钮
     *
     * @param v
     */
    @OnClick(R.id.btn_add_add)
    void btn_add_add_click(View v) {
        //去服务器发信息添加好友
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //发送消息
                try {
                    EMClient.getInstance().contactManager().addContact(userInfor.getName(), "添加好友邀请");

                    //提示发送添加好友信息完成
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "添加好友信息发送成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "添加好友信息发送失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
