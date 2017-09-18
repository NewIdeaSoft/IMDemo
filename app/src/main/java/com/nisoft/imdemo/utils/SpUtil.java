package com.nisoft.imdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nisoft.imdemo.IMApplication;

/**
 * Created by Administrator on 2017/9/18.
 */

public class SpUtil {
    public static final String IS_CONTACT_CHANGED = "IS_CONTACT_CHANGED";
    private static SpUtil sUtil = new SpUtil();
    private static SharedPreferences mPreferences;
    private SpUtil() {

    }
    public static SpUtil getInstance(){
        if(mPreferences == null) {
            mPreferences = IMApplication
                            .getApplication()
                            .getSharedPreferences("im", Context.MODE_PRIVATE);
        }
        return sUtil;
    }

    public void put(String key,Object data){
        if(data instanceof String) {
            mPreferences.edit().putString(key, (String) data).apply();
        }else if(data instanceof Integer) {
            mPreferences.edit().putInt(key, (Integer) data).apply();
        }else if(data instanceof Boolean) {
            mPreferences.edit().putBoolean(key, (Boolean) data).apply();
        }

    }

    public String getString(String key,String defaultValue){
        return mPreferences.getString(key,defaultValue);
    }
    public int getInt(String key,int defaultValue){
        return mPreferences.getInt(key,defaultValue);
    }
    public boolean getBoolean(String key,boolean defaultValue){
        return mPreferences.getBoolean(key,defaultValue);
    }

}
