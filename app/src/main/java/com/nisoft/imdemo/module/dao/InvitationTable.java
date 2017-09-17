package com.nisoft.imdemo.module.dao;

/**
 * Created by Administrator on 2017/9/17.
 */

public class InvitationTable {
    public static final String TABLE_NAME = "invitation";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_HXID = "hxid";
    public static final String COL_PHOTO = "photo";
    public static final String COL_NICK = "nick";
    public static final String COL_STATE = "state";
    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_GROUP_HXID = "group_hxid";
    public static final String COL_REASON = "reason";
    public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COL_ID + " integer primary key not null,"
            + COL_HXID + " text not null unique,"
            + COL_NAME + " text,"
            + COL_PHOTO + " text,"
            + COL_NICK + " text,"
            + COL_STATE + " integer,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_HXID + " text,"
            + COL_REASON + " text)";
}
