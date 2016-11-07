package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.adapter.GroupListAdapter;
import com.atguigu.imease.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupListActivity extends Activity {

    @Bind(R.id.lv_grouplist)
    ListView lvGrouplist;
    //创建群组的对象
    private LinearLayout createGroup;
    //群组列表适配器
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_list);
        ButterKnife.bind(this);

        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lvGrouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //判断listview的头部点击事件在此不做处理  之后单独做点击处理  此地不判断会空指针
                if(position == 0){
                    return;
                }

                //头部视图为0  listView都要减少1
                position -= 1;

                //跳转到Chat界面
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);

                //携带数据
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);

                //跳转携带数据
                List<EMGroup> allGroups = EMClient.getInstance().groupManager().getAllGroups();
                intent.putExtra(EaseConstant.EXTRA_USER_ID, allGroups.get(position).getGroupId());

                startActivity(intent);
            }
        });

        //创建群组点击监听
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
                //是否携带信息---不带信息
                //开启跳转
                startActivity(intent);
            }
        });
    }

    private void initData() {
        //初始化listView 创建适配器
        groupListAdapter = new GroupListAdapter(this);

        lvGrouplist.setAdapter(groupListAdapter);

        //从环信服务器上获取群组信息
        getGroupFromHxServer();
    }

    private void getGroupFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //联网获取群组信息-----注意区分本地读取群信息方法
                    List<EMGroup> groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    Log.e("TAG1", "333333333----" + groupList.size());

                    //内存中没做
                    if (groupList != null && groupList.size() >= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示
                                Toast.makeText(GroupListActivity.this, "获取群组信息成功", Toast.LENGTH_SHORT).show();

                                //刷新显示
                                refresh();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提示失败信息
                            Toast.makeText(GroupListActivity.this, "加载群信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refresh() {
        Log.e("TAG1", "111111111---");
        List<EMGroup> allGroups = EMClient.getInstance().groupManager().getAllGroups();
        groupListAdapter.refresh(allGroups);
        Log.e("TAG1", "222222222---" + allGroups.size());
    }

    private void initView() {
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        createGroup = (LinearLayout) headerView.findViewById(R.id.ll_create_group);
        lvGrouplist.addHeaderView(headerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //刷新页面
        refresh();
    }
}
