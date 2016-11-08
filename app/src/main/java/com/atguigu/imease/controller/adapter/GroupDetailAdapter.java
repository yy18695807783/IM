package com.atguigu.imease.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.imease.R;
import com.atguigu.imease.model.bean.UserInfor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 颜银 on 2016/11/7.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;       // 表示是否可以添加和删除好友
    private boolean mIsDeleteModel = false;// 删除模式  true：表示可以删除； false:表示不可以删除

    private OnGroupDetailListener mOnGroupDetailListener;
    private List<UserInfor> mUsers = new ArrayList<>();

    public GroupDetailAdapter(Context context, boolean isCanModify, OnGroupDetailListener onGroupDetailListener) {
        mContext = context;
        mIsCanModify = isCanModify;
        mOnGroupDetailListener = onGroupDetailListener;
    }

    private void initUsers() {
        UserInfor add = new UserInfor("add");
        UserInfor delete = new UserInfor("delete");

        mUsers.add(delete);
        mUsers.add(0, add);
    }

    // 刷新的方法
    public void refresh(List<UserInfor> users) {

        // 校验
        if (users != null && users.size() >= 0) {
            mUsers.clear();

            initUsers();

            mUsers.addAll(0, users);
        }

        // 刷新页面
        notifyDataSetChanged();
    }


    // 获取当前的模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }

    // 设置当前的模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }


    @Override
    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_group_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        final UserInfor userInfor = mUsers.get(position);

        //判断显示数据
        if (mIsCanModify) { //群主

            //布局处理
            if (position == getCount() - 1) { // 减号处理
                //删除模式判断
                if (mIsDeleteModel) {  //是删除模式，进入有删除红色减号 但是减号隐藏
                    convertView.setVisibility(View.INVISIBLE);//隐藏/相当于GOSN

                } else {
                    convertView.setVisibility(View.VISIBLE); // 减号显示

                    //图片  显示减号
                    viewHolder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);

                    //delete符号   隐藏
                    viewHolder.delete.setVisibility(View.GONE);

                    //名字   隐藏
                    viewHolder.name.setVisibility(View.INVISIBLE);
                }
            } else if (position == getCount() - 2) { // 加号处理
                //删除模式判断
                if (mIsDeleteModel) {  //是删除模式，进入有删除红色减号 但是减号隐藏
                    convertView.setVisibility(View.INVISIBLE);//隐藏/相当于GOSN

                } else {
                    convertView.setVisibility(View.VISIBLE); // 减号显示

                    //图片  显示减号
                    viewHolder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);

                    //delete符号   隐藏
                    viewHolder.delete.setVisibility(View.GONE);

                    //名字   隐藏
                    viewHolder.name.setVisibility(View.INVISIBLE);
                }
            } else { // 群成员显示
                convertView.setVisibility(View.VISIBLE);

                viewHolder.photo.setVisibility(View.VISIBLE);
                viewHolder.photo.setImageResource(R.drawable.em_default_avatar);

                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.name.setText(userInfor.getName());

                if (mIsDeleteModel) {
                    viewHolder.delete.setVisibility(View.VISIBLE);//删除模式显示
                } else {
                    viewHolder.delete.setVisibility(View.GONE);//非删除模式隐藏
                }
            }

            //  加减号  点击事件处理
            if (position == getCount() - 1) { //减号
                viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //处理是否是删除模式
                        if (!mIsDeleteModel) {
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            } else if (position == getCount() - 2) { // 加号
                viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //接口
                        if (mOnGroupDetailListener != null) {
                            mOnGroupDetailListener.onAddMembers();
                        }
                    }
                });

            } else {  // 删除模式的小红色减号
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //接口
                        if (mOnGroupDetailListener != null) {
                            mOnGroupDetailListener.onDeleteMembers(userInfor);
                        }
                    }
                });
            }

        } else { //群成员
            if (position == getCount() - 1 || position == getCount() - 2) { // 隐藏加减号
                convertView.setVisibility(View.GONE);

            } else { // 显示群成员信息
                convertView.setVisibility(View.VISIBLE);

                //名称
                viewHolder.name.setText(userInfor.getName());

                //头像
                viewHolder.photo.setImageResource(R.drawable.em_default_avatar);

                //删除  隐藏
                viewHolder.delete.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.tv_member_name)
        TextView name;

        @Bind(R.id.iv_member_photo)
        ImageView photo;

        @Bind(R.id.iv_member_delete)
        ImageView delete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnGroupDetailListener {
        //增加
        void onAddMembers();

        //删除
        void onDeleteMembers(UserInfor user);
    }
}
