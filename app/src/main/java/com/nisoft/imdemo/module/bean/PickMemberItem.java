package com.nisoft.imdemo.module.bean;

/**
 * Created by Administrator on 2017/10/22.
 */

public class PickMemberItem {
    private UserInfo mUserInfo;
    private boolean isChecked;

    public PickMemberItem(UserInfo userInfo, boolean isChecked) {
        mUserInfo = userInfo;
        this.isChecked = isChecked;
    }

    public PickMemberItem() {
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
