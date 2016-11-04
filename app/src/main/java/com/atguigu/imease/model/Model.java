package com.atguigu.imease.model;

import android.content.Context;

import com.atguigu.imease.model.bean.UserInfor;
import com.atguigu.imease.model.dao.UserAccountDao;
import com.atguigu.imease.model.db.DBHelperManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 颜银 on 2016/11/1.
 * QQ:443098360
 * 微信：y443098360
 * 作用：
 */
public class  Model {
    /**
     * 全局线程池
     */
    private ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 单例
     */
    private static Model model = new Model();

    /**
     * 得到账户信息数据库操作类
     */
    private UserAccountDao mUserAccountDao;
    /**
     * 数据库管理类
     */
    private DBHelperManager dbHelperManager;
    private Context mContext;
    /**
     * 创建广播
     */
    private EventListener mEventListener;

    public Model() {

    }

    /**
     *初始化
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //创建数据库
        mUserAccountDao = new UserAccountDao(mContext);

        //创建广播监听
        if(mEventListener==null){
            mEventListener = new EventListener(mContext);
        }
    }

    public static Model getInstance() {
        return model;
    }

    public ExecutorService getGlobalThreadPool() {
        return executorService;
    }

    //得到账户信息数据库操作类
    public UserAccountDao getmUserAccountDao() {
        return mUserAccountDao;
    }

    //登陆成功的回调
    public void loginSucess(UserInfor userInfor){
        //校验
        if(userInfor == null){
            return;
        }
        //判断dbHelperManager是否为空
        if(dbHelperManager != null){
            dbHelperManager.close();
        }

        dbHelperManager = new DBHelperManager(mContext,userInfor.getName());
    }

    public DBHelperManager getDbHelperManager() {
        return dbHelperManager;
    }
}
