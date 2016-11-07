package com.atguigu.imease.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.imease.R;
import com.atguigu.imease.controller.adapter.PickContactsAdapter;
import com.atguigu.imease.model.Model;
import com.atguigu.imease.model.bean.PickContactsInfo;
import com.atguigu.imease.model.bean.UserInfor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PickContactsActivity extends Activity {

    @Bind(R.id.tv_addcontact_save)
    TextView tvAddcontactSave;
    @Bind(R.id.lv_addcontact_togroup)
    ListView lvAddcontactTogroup;
    private PickContactsAdapter pickContactsAdapter;
    //是否选中加入群组的联系人
    private List<PickContactsInfo> pickContactsInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contacts);
        ButterKnife.bind(this);

        //获取已经存在的群成员

        initData();
        initListener();

    }

    private void initListener() {
        //listView的点击事件
        lvAddcontactTogroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  获取当前item的checkbox对象
                CheckBox cb_pickcontact_choose = (CheckBox) view.findViewById(R.id.cb_pickcontact_choose);

                //设置点击取反状态
                cb_pickcontact_choose.setChecked(!cb_pickcontact_choose.isChecked());

                // 更新数据
                PickContactsInfo pickContactsInfo = pickContactsInfos.get(position);
                pickContactsInfo.setIsChecked(cb_pickcontact_choose.isChecked());

                // 刷新列表数据
                pickContactsAdapter.notifyDataSetChanged();

            }
        });

        //保存按钮的点击事件
        tvAddcontactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取被选择的联系人
                List<String> addMembers = pickContactsAdapter.getAddMembers();
                // 设置数据准备返回创建群页面
                Intent intent = new Intent();
                //集合转化为数据
                intent.putExtra("members", addMembers.toArray(new String[0]));
                setResult(RESULT_OK, intent);
                // 结束当前页面
                finish();
            }
        });
    }

    private void initData() {

        //获取联系人数据
        List<UserInfor> contacts = Model.getInstance().getDbHelperManager().getContactTableDao().getContacts();

        if (contacts != null && contacts.size() >= 0) {
            pickContactsInfos = new ArrayList<>();

            for (UserInfor contact : contacts) {
                //默认都不是选中的联系人
                PickContactsInfo pickContactsInfo = new PickContactsInfo(contact, false);

                pickContactsInfos.add(pickContactsInfo);
            }
        }

        //设置适配器
        pickContactsAdapter = new PickContactsAdapter(this,pickContactsInfos);

        lvAddcontactTogroup.setAdapter(pickContactsAdapter);


    }

}
