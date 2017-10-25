package com.nisoft.imdemo.module;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.nisoft.imdemo.module.bean.Group;
import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.module.bean.UserInfo;
import com.nisoft.imdemo.utils.Constant;
import com.nisoft.imdemo.utils.SpUtil;

import java.util.List;

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

    private final EMGroupChangeListener mEmGroupChangeListener = new EMGroupChangeListener() {
        @Override
        public void onInvitationReceived(String s, String s1, String s2, String s3) {
            Invitation invitation = new Invitation();
            invitation.setReason(s3);
            invitation.setGroup(new Group(s,s1,s2));
            invitation.setState(Invitation.InvokeState.NEW_GROUP_INVITE);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {
            Invitation invitation = new Invitation();
            invitation.setReason(s3);
            invitation.setGroup(new Group(s,s1,s2));
            invitation.setState(Invitation.InvokeState.NEW_GROUP_APPLICATION);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {
            Invitation invitation = new Invitation();
            invitation.setGroup(new Group(s,s1,s2));
            invitation.setState(Invitation.InvokeState.GROUP_APPLICATION_ACCEPTED);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {
            Invitation invitation = new Invitation();
            invitation.setGroup(new Group(s,s1,s2));
            invitation.setReason(s3);
            invitation.setState(Invitation.InvokeState.GROUP_APPLICATION_DECLINED);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onInvitationAccepted(String s, String s1, String s2) {
            Invitation invitation = new Invitation();
            invitation.setGroup(new Group(s,s,s1));
            invitation.setReason(s2);
            invitation.setState(Invitation.InvokeState.GROUP_INVITE_DECLINED);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onInvitationDeclined(String s, String s1, String s2) {
            Invitation invitation = new Invitation();
            invitation.setGroup(new Group(s,s,s1));
            invitation.setReason(s2);
            invitation.setState(Invitation.InvokeState.GROUP_INVITE_DECLINED);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onUserRemoved(String s, String s1) {
        }

        @Override
        public void onGroupDestroyed(String s, String s1) {
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {
            Invitation invitation = new Invitation();
            invitation.setGroup(new Group(s,s,s1));
            invitation.setReason(s2);
            invitation.setState(Invitation.InvokeState.GROUP_INVITE_ACCEPTED);
            Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
            mLocalBroadcast.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {
        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {
        }

        @Override
        public void onAdminAdded(String s, String s1) {
        }

        @Override
        public void onAdminRemoved(String s, String s1) {
        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {

        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    };
    public EventListener(Context context) {
        mContext = context;
        mLocalBroadcast = LocalBroadcastManager.getInstance(mContext);
        EMClient.getInstance().contactManager().setContactListener(mContactListener);
        EMClient.getInstance().groupManager().addGroupChangeListener(mEmGroupChangeListener);
    }

}
