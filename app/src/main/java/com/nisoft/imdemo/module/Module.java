package com.nisoft.imdemo.module;

import android.content.Context;
import android.util.Log;

import com.nisoft.imdemo.module.bean.UserInfo;
import com.nisoft.imdemo.module.dao.UserDao;
import com.nisoft.imdemo.module.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by NewIdeaSoft on 2017/9/14.
 */

public class Module {
    private static Module sModule = new Module();
    private Context mContext;
    private ExecutorService mExecutors = Executors.newCachedThreadPool();//创建线程池
    private UserDao mUserDao;
    private DBManager mDbManager;

    private Module(){

    }

    public static Module getInstance() {
        return sModule;
    }

    public void init(Context context){
        mContext = context;
        mUserDao = new UserDao(mContext);
        EventListener eventListener = new EventListener(mContext);
    }

    public ExecutorService getGlobalThreadPool(){
        return mExecutors;
    }
    public UserDao getUserDao(){
        if(mUserDao ==null) {
            mUserDao = new UserDao(mContext);
        }
        return mUserDao;
    }
    public void onLoginSuccess(UserInfo account){
        if(account==null) {
            return;
        }
        if(mDbManager!=null) {
            mDbManager.close();
        }
        mDbManager = new DBManager(mContext,account.getName());
        Log.e("TAG", "account.getName()="+account.getName());
    }

    public DBManager getDbManager() {
        return mDbManager;
    }
}
