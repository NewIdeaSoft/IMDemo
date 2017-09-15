package com.nisoft.imdemo.module.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nisoft.imdemo.module.bean.UserInfo;

/**
 * 存储和读取用户信息的工具类
 * Created by Administrator on 2017/9/15.
 */

public class UserDao {
    private SharedPreferences mPreferences;
    private Context mContext;
    public UserDao(Context context){
        mContext = context;
        mPreferences = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
    }

    /**
     * json格式保存用户信息
     * @param userInfo 将被保存的用户信息
     */
    public void putUserInfo(UserInfo userInfo){
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String userInfoJson = gson.toJson(userInfo);
        editor.putString("userInfoJson",userInfoJson);
        editor.apply();
    }

    /**
     * 读取用户信息
     * @return 用户信息
     */
    public UserInfo getExitUerInfo(){
        String userInfoJson = mPreferences.getString("userInfoJson", "");
        if (userInfoJson.equals("")){
            return null;
        }
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(userInfoJson,UserInfo.class);
        return userInfo;
    }
}
