package com.nisoft.imdemo;


import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.nisoft.imdemo.module.Module;

/**
 * 替代默认Application，初始化全局实例
 * Created by NewIdeaSoft on 2017/9/14.
 */

public class IMApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //设置
        EMOptions options = new EMOptions();
        //设置不自动接受好友邀请
        options.setAcceptInvitationAlways(false);
        //设置不自动接受群邀请
        options.setAutoAcceptGroupInvitation(false);
        //初始化EaseUi
        EaseUI.getInstance().init(this, options);
        //初始化Module
        Module.getInstance().init(this);
        mContext = this;
    }

    /**
     *
     * @return 全局Context
     */
    public static Context getApplication() {
        return mContext;
    }
}
