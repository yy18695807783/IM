package com.atguigu.imease.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.atguigu.imease.IMApplication;

/**
 * Created by 颜银 on 2016/11/4.
 * QQ:443098360
 * 微信：y443098360
 * 作用：内部存储工具类
 */
public class SpUtils {

    private static SpUtils instance = new SpUtils();

    private static SharedPreferences sp = null;
    public static String IS_NEW_INVITE = "is_new_invite";

    public static SpUtils getInstance() {

        if (sp == null) {
            sp = IMApplication.getContext().getSharedPreferences("imease", Context.MODE_PRIVATE);//私有化内部简单存储模式
        }
        return instance;
    }

    //保存
    public void save(String key, Object values) {
        if (values instanceof String) {
            sp.edit().putString(key, (String) values).commit();
        } else if (values instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) values).commit();
        } else if (values instanceof Integer) {
            sp.edit().putInt(key, (Integer) values).commit();
        }

    }

    //读取字符串
    public String getString(String key, String defValues) {
        return sp.getString(key, defValues);
    }

    //读取Boolean
    public Boolean getBoolean(String key, Boolean defValues) {
        return sp.getBoolean(key, defValues);
    }

    //读取Integer
    public int getInt(String key, int defValues) {
        return sp.getInt(key, defValues);
    }


}
