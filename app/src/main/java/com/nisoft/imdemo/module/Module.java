package com.nisoft.imdemo.module;

import android.content.Context;

import com.nisoft.imdemo.module.dao.UserDao;

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
    private Module(){

    }

    public static Module getInstance() {
        return sModule;
    }

    public void init(Context context){
        mContext = context;
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
}
