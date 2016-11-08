package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewGroupActivity extends Activity {

    @Bind(R.id.et_group_name)
    EditText etGroupName;
    @Bind(R.id.et_group_detail)
    EditText etGroupDetail;
    @Bind(R.id.cb_group_ispublic)
    CheckBox cbGroupIspublic;
    @Bind(R.id.cb_group_isinvite)
    CheckBox cbGroupIsinvite;
    @Bind(R.id.btn_group_create)
    Button btnGroupCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_group);
        ButterKnife.bind(this);


        initListener();
    }

    private void initListener() {
        btnGroupCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到联系页面获取联系人
                Intent intent = new Intent(NewGroupActivity.this, PickContactsActivity.class);

                //带回调期启动
                startActivityForResult(intent, 110);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            //创建群
            createGroup(data.getStringArrayExtra("members"));
        }
        Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();
        //销毁界面
        finish();
    }

    private void createGroup(final String[] members) {

        //群组名称
        final String groupName = etGroupName.getText().toString();
        //群描述
        final String groupDetails = etGroupDetail.getText().toString();
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器创建群
                // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                options.maxUsers = 200;//最多容乃多少钱
                EMGroupManager.EMGroupStyle groupStyle = null;

                if (cbGroupIspublic.isChecked()) {//公开
                    if (cbGroupIsinvite.isChecked()) {//开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {//不开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {//不公开
                    if (cbGroupIsinvite.isChecked()) {//开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {//不开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                options.style = groupStyle;//创建群类型
                try {
                    //去环信服务器创建群组
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDetails, members, "申请加入群", options);

                    //提示创建成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群组成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群组失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
