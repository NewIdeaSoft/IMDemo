package com.nisoft.imdemo.module.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.module.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/17.
 */

public class InvitationDAO {
    private SQLiteOpenHelper mHelper;

    public InvitationDAO(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    public List<Invitation> findAll() {
        String sql = "select * from " + InvitationTable.TABLE_NAME + ";";
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        List<Invitation> invitationList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Invitation invitation = new Invitation();
            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            invitation.setUserInfo(userInfo);
            invitation.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_HXID)));
            invitation.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));
            invitation.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            invitation.setState(int2InvokeState(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATE))));
            invitationList.add(invitation);
        }
        cursor.close();
        return invitationList;
    }

    public Invitation findInvitation(String hxid) {
        if (hxid == null) {
            return null;
        }
        String sql = "select * from " + InvitationTable.TABLE_NAME + " where " + InvitationTable.COL_HXID + "=?;";
        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, new String[]{hxid});
        Invitation invitation = null;
        if (cursor.moveToNext()) {
            invitation = new Invitation();
            UserInfo userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            invitation.setUserInfo(userInfo);
            invitation.setGroupId(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_HXID)));
            invitation.setGroupName(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_GROUP_NAME)));
            invitation.setReason(cursor.getString(cursor.getColumnIndex(InvitationTable.COL_REASON)));
            invitation.setState(int2InvokeState(cursor.getInt(cursor.getColumnIndex(InvitationTable.COL_STATE))));
        }
        cursor.close();
        return invitation;
    }

    public void updateInvitation(String hxid, Invitation.InvokeState state) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "update " + InvitationTable.TABLE_NAME + " set " + InvitationTable.COL_STATE +
                "=" + state.ordinal() + " where " + InvitationTable.COL_HXID + "='" + hxid +"';";
        database.execSQL(sql);
    }

    public void deleteInvitation(String hxid) {
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.delete(InvitationTable.TABLE_NAME, InvitationTable.COL_HXID+"=?", new String[]{hxid});
    }

    public void addInvitation(Invitation invitation) {
        ContentValues values = ContactDAO.getContentValues(invitation.getUserInfo());
        values.put(InvitationTable.COL_GROUP_HXID, invitation.getGroupId());
        values.put(InvitationTable.COL_GROUP_NAME, invitation.getGroupName());
        values.put(InvitationTable.COL_STATE,invitation.getState().ordinal());
        SQLiteDatabase database = mHelper.getReadableDatabase();
        database.replace(InvitationTable.TABLE_NAME, null, values);
    }

    private Invitation.InvokeState int2InvokeState(int state) {
        if (state == Invitation.InvokeState.NEW_INVITE.ordinal()) {
            return Invitation.InvokeState.NEW_INVITE;
        }
        if(state == Invitation.InvokeState.NEW_SELF_INVITE.ordinal()) {
            return Invitation.InvokeState.NEW_SELF_INVITE;
        }
        if (state == Invitation.InvokeState.INVITE_ACCEPT.ordinal()) {
            return Invitation.InvokeState.INVITE_ACCEPT;
        }
        if (state == Invitation.InvokeState.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return Invitation.InvokeState.INVITE_ACCEPT_BY_PEER;
        }
        if (state == Invitation.InvokeState.NEW_GROUP_INVITE.ordinal()) {
            return Invitation.InvokeState.NEW_GROUP_INVITE;
        }
        if (state == Invitation.InvokeState.NEW_GROUP_APPLICATION.ordinal()) {
            return Invitation.InvokeState.NEW_GROUP_APPLICATION;
        }
        if (state == Invitation.InvokeState.GROUP_INVITE_ACCEPTED.ordinal()) {
            return Invitation.InvokeState.GROUP_INVITE_ACCEPTED;
        }
        if (state == Invitation.InvokeState.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return Invitation.InvokeState.GROUP_APPLICATION_ACCEPTED;
        }
        if (state == Invitation.InvokeState.GROUP_INVITE_DECLINED.ordinal()) {
            return Invitation.InvokeState.GROUP_INVITE_DECLINED;
        }
        if (state == Invitation.InvokeState.GROUP_APPLICATION_DECLINED.ordinal()) {
            return Invitation.InvokeState.GROUP_APPLICATION_DECLINED;
        }
        if (state == Invitation.InvokeState.GROUP_ACCEPT_INVITE.ordinal()) {
            return Invitation.InvokeState.GROUP_ACCEPT_INVITE;
        }
        if (state == Invitation.InvokeState.GROUPO_ACCEPT_APPLICATION.ordinal()) {
            return Invitation.InvokeState.GROUPO_ACCEPT_APPLICATION;
        }
        if (state == Invitation.InvokeState.GROUP_REJECT_APPLICATION.ordinal()) {
            return Invitation.InvokeState.GROUP_REJECT_APPLICATION;
        }
        if (state == Invitation.InvokeState.GROUP_REJECT_INVITE.ordinal()) {
            return Invitation.InvokeState.GROUP_REJECT_INVITE;
        }
        return null;
    }

}
