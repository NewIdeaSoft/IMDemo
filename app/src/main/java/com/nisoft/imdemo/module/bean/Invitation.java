package com.nisoft.imdemo.module.bean;

/**
 * Created by Administrator on 2017/9/17.
 */

public class Invitation {
    private UserInfo mUserInfo;
    private String mReason;
    private String mGroupName;
    private String mGroupId;
    private InvokeState mState;

    public enum InvokeState{
        NEW_INVITE,
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER,// 邀请被接受
        //收到邀请去加入群
        NEW_GROUP_INVITE,
        //收到申请群加入
        NEW_GROUP_APPLICATION,
        //群邀请已经被对方接受
        GROUP_INVITE_ACCEPTED,
        //群申请已经被批准
        GROUP_APPLICATION_ACCEPTED,
        //接受了群邀请
        GROUP_ACCEPT_INVITE,
        //批准的群加入申请
        GROUPO_ACCEPT_APPLICATION,
        //拒绝了群邀请
        GROUP_REJECT_INVITE,
        //拒绝了群申请加入
        GROUP_REJECT_APPLICATION,
        //群邀请被对方拒绝
        GROUP_INVITE_DECLINED,
        //群申请被拒绝
        GROUP_APPLICATION_DECLINED
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        mReason = reason;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    public InvokeState getState() {
        return mState;
    }

    public void setState(InvokeState state) {
        mState = state;
    }
}
