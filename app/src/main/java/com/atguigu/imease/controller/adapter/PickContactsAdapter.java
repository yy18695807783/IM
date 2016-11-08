package com.atguigu.imease.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atguigu.imease.R;
import com.atguigu.imease.model.bean.PickContactsInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 颜银 on 2016/11/6.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class PickContactsAdapter extends BaseAdapter {

    //联系人集合
    private List<PickContactsInfo> mPickContactsInfos = new ArrayList<>();
    // 保存群中已经存在的成员集合
    private List<String> mExistMembers = new ArrayList<>();
    private Context mContext;


    public PickContactsAdapter(Context context, List<PickContactsInfo> pickContactsInfos, List<String> members) {
        mContext = context;

        if (pickContactsInfos != null && pickContactsInfos.size() >= 0) {
            mPickContactsInfos.clear();

            mPickContactsInfos.addAll(pickContactsInfos);
        }

        if (members != null && members.size() >= 0) {
            mExistMembers.clear();

            mExistMembers.addAll(members);
        }
    }

    @Override
    public int getCount() {
        return mPickContactsInfos == null ? 0 : mPickContactsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPickContactsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建ViewHolder
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_pickcontant, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //获取数据对象
        PickContactsInfo pickContactsInfo = mPickContactsInfos.get(position);
        //装配数据
        viewHolder.name.setText(pickContactsInfo.getUser().getName());
        viewHolder.choose.setChecked(pickContactsInfo.getIsChecked());

        if (mExistMembers.contains(pickContactsInfo.getUser().getHxid())) {
            viewHolder.choose.setChecked(true);//页面
            pickContactsInfo.setIsChecked(true);//内存
        }

        //返回数据
        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.cb_pickcontact_choose)
        CheckBox choose;

        @Bind(R.id.tv_pickcontact_name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //获取当前已经选择的联系人
    public List<String> getAddMembers() {
        List<String> picks = new ArrayList<>();

        for (PickContactsInfo pick : mPickContactsInfos) {
            //判断是否被选中
            if (pick.getIsChecked()) {
                picks.add(pick.getUser().getHxid());
            }
        }
        //返回数据
        return picks;
    }

}
