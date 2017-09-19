package com.nisoft.imdemo.module.bean;

/**
 * Created by NewIdeaSoft on 2017/9/14.
 */

public class UserInfo {
    private String mName;
    private String mHxid;
    private String mNick;
    private String mPhoto;
    private String mPassword;

    public UserInfo() {
    }

    public UserInfo(String name) {
        mName = name;
        mHxid = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getHxid() {
        return mHxid;
    }

    public void setHxid(String hxid) {
        mHxid = hxid;
    }

    public String getNick() {
        return mNick;
    }

    public void setNick(String nick) {
        mNick = nick;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
