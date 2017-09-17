package com.nisoft.imdemo.module.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.imdemo.module.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/9/17.
 */

public class ContactDAO {
    private SQLiteOpenHelper mHelper;
    public ContactDAO(SQLiteOpenHelper helper) {
        mHelper = helper;
    }

    /**
     * 获取全部联系人
     * @return
     */
    public List<UserInfo> getAllContacts(){
        List<UserInfo> userInfoList = null;
        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "select * from "+ContactTable.TABLE_NAME +";";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount()>0){
            userInfoList = new ArrayList<>();
            while(cursor.moveToNext()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
                userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            }
        }
        cursor.close();
        return userInfoList;
    }

    /**
     * 根据hxid查找联系人
     * @param hxid
     * @return
     */
    public UserInfo findContact(String hxid){
        if(hxid == null) {
            return null;
        }
        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "select * from "+ContactTable.TABLE_NAME +" where "+ ContactTable.COL_HXID +"=?";
        Cursor cursor = database.rawQuery(sql, new String[]{hxid});
        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
        }
        cursor.close();
        return userInfo;
    }

    /**
     * 批量查找联系人
     * @param hxids
     * @return
     */
    public List<UserInfo> findContacts(List<String> hxids){
        if(hxids == null||hxids.size()<=0) {
            return null;
        }
        List<UserInfo> userInfoList = new ArrayList<>();
        for (String hxid : hxids){
            userInfoList.add(findContact(hxid));
        }
        return userInfoList;
    }

    public static ContentValues getContentValues(UserInfo userInfo){
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID,userInfo.getHxid());
        values.put(ContactTable.COL_NAME,userInfo.getName());
        values.put(ContactTable.COL_NICK,userInfo.getNick());
        values.put(ContactTable.COL_PHOTO,userInfo.getPhoto());
        return values;
    }

    /**
     * 添加联系人
     * @param contact
     */
    public void addContact(UserInfo contact){
        ContentValues values = getContentValues(contact);
        SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
        readableDatabase.replace(ContactTable.TABLE_NAME,null,values);
    }

    /**
     * 删除联系人
     * @param hxid
     */
    public void deleteContact(String hxid){
        if(hxid == null) {
            return;
        }
        SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
        readableDatabase.delete(ContactTable.TABLE_NAME,ContactTable.COL_HXID,new String[]{hxid});
    }
}
