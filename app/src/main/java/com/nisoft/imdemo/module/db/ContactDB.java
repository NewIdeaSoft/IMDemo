package com.nisoft.imdemo.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.imdemo.module.dao.ContactTable;
import com.nisoft.imdemo.module.dao.InvitationTable;

/**
 * Created by Administrator on 2017/9/17.
 */

public class ContactDB extends SQLiteOpenHelper {
    public ContactDB(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.SQL_CREATE_TABLE);
        db.execSQL(InvitationTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
