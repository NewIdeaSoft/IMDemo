package com.nisoft.imdemo.module.bean;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Group {
    private String mGroupId;
    private String mGroupName;
    private String mInviterName;

    public Group() {
    }

    public Group(String groupId, String groupName, String inviterName) {
        mGroupId = groupId;
        mGroupName = groupName;
        mInviterName = inviterName;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public String getInviterName() {
        return mInviterName;
    }

    public void setInviterName(String inviterName) {
        mInviterName = inviterName;
    }
}
