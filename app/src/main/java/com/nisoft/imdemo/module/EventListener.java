package com.nisoft.imdemo.module;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.module.bean.UserInfo;
import com.nisoft.imdemo.utils.Constant;
import com.nisoft.imdemo.utils.SpUtil;

/**
 * Created by Administrator on 2017/9/18.
 */

public class EventListener {
    private Context mContext;
    private LocalBroadcastManager mLocalBroadcast;
    private EMContactListener mContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(String s) {
            Module.getInstance().getDbManager().getContactDAO().addContact(new UserInfo(s));
            mLocalBroadcast.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        @Override
        public void onContactDeleted(String s) {
            Module.getInstance().getDbManager().getContactDAO().deleteContact(s);
            Module.getInstance().getDbManager().getInvitationDAO().deleteInvitation(s);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        @Override
        public void onContactInvited(String s, String s1) {
            Invitation invitation = new Invitation();
            invitation.setUserInfo(new UserInfo(s));
            invitation.setReason(s1);
            invitation.setState(Invitation.InvokeState.NEW_INVITE);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.INVITATION_CHANGED));
        }

        @Override
        public void onFriendRequestAccepted(String s) {
            Module.getInstance().getDbManager().getInvitationDAO().updateInvitation(s, Invitation.InvokeState.INVITE_ACCEPT_BY_PEER);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.INVITATION_CHANGED));
        }

        @Override
        public void onFriendRequestDeclined(String s) {
            Module.getInstance().getDbManager().getInvitationDAO().deleteInvitation(s);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.INVITATION_CHANGED));
        }
    };


    public EventListener(Context context) {
        mContext = context;
        mLocalBroadcast = LocalBroadcastManager.getInstance(mContext);
        EMClient.getInstance().contactManager().setContactListener(mContactListener);
    }

}
