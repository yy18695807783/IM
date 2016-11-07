package com.atguigu.imease.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.imease.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 颜银 on 2016/11/5.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class GroupListAdapter extends BaseAdapter {

    private List<EMGroup> mEmGroups = new ArrayList<>();

    private Context mContext;

    public GroupListAdapter(Context context) {
        mContext = context;
    }

    //刷新方法
    public void refresh(List<EMGroup> emGroups) {
        if (emGroups != null && emGroups.size() >= 0) {
            mEmGroups.clear();

            mEmGroups.addAll(emGroups);

            //刷新适配器
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mEmGroups == null ? 0 : mEmGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mEmGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //创建 ViewHolder
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_grouplist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //回去信息对象
        EMGroup emGroup = mEmGroups.get(position);

        //装配数据
        viewHolder.groupName.setText(emGroup.getGroupName());

        //返回View
        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.tv_group_name)
        TextView groupName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
