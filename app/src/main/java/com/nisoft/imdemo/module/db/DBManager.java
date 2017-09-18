package com.nisoft.imdemo.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.imdemo.module.dao.ContactDAO;
import com.nisoft.imdemo.module.dao.InvitationDAO;

/**
 * Created by Administrator on 2017/9/18.
 */

public class DBManager {
    private final ContactDB mHelper;
    private ContactDAO mContactDAO;
    private InvitationDAO mInvitationDAO;
    public DBManager(Context context, String name) {
        mHelper = new ContactDB(context,name);
        mContactDAO = new ContactDAO(mHelper);
        mInvitationDAO = new InvitationDAO(mHelper);
    }

    public ContactDAO getContactDAO() {
        return mContactDAO;
    }

    public InvitationDAO getInvitationDAO() {
        return mInvitationDAO;
    }

    public void close() {
        mHelper.close();
    }
}
