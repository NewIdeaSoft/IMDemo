package com.nisoft.imdemo.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.activity.ChattingActivity;
import com.nisoft.imdemo.controller.activity.GroupListActivity;
import com.nisoft.imdemo.controller.activity.InvitationListActivity;
import com.nisoft.imdemo.controller.activity.NewFriendActivity;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.UserInfo;
import com.nisoft.imdemo.utils.Constant;
import com.nisoft.imdemo.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ContactsFragment extends EaseContactListFragment {
    private static final String TAG = "ContactsFragment";
    private LinearLayout ll_fragment_main_contacts_new_friend;
    private LinearLayout ll_fragment_main_contacts_group;
    private ImageView iv_fragment_contact_new_invitation;
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_fragment_contact_new_invitation.setVisibility(View.VISIBLE);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED, true);
        }
    };
    private BroadcastReceiver mContactChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContacts();
        }
    };
    private String mHxid;
    private BroadcastReceiver mGroupChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_fragment_contact_new_invitation.setVisibility(View.VISIBLE);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED, true);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        titleBar.setRightImageResource(R.drawable.em_add);
        View view = View.inflate(getActivity(), R.layout.header_fragment_main_contacts, null);
        ll_fragment_main_contacts_new_friend =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_new_friend);
        ll_fragment_main_contacts_group =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_group);
        iv_fragment_contact_new_invitation =
                (ImageView) view.findViewById(R.id.iv_fragment_contact_new_invitation);

        listView.addHeaderView(view);


        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                if(user==null){
                    return;
                }
                Intent intent = new Intent(getActivity(), ChattingActivity.class);
                intent.putExtra("hxid",user.getUsername());
                startActivity(intent);
            }
        });

    }

    private void getContactsFromServer() {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (allContacts == null || allContacts.size() == 0) return;
                    Log.e(TAG, allContacts.size() + "个联系人");
                    List<UserInfo> contactList = new ArrayList<>();
                    for (String hxid : allContacts) {
                        Log.e(TAG, "hxid:"+hxid);
                        UserInfo userInfo = new UserInfo(hxid);
                        contactList.add(userInfo);
                    }
                    Module.getInstance().getDbManager().getContactDAO().addContacts(contactList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshContacts();
                        }
                    });
                } catch (HyphenateException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void refreshContacts() {
        List<UserInfo> contacts = Module.getInstance().getDbManager().getContactDAO().getAllContacts();
        Map<String, EaseUser> contactsMap = new HashMap<>();

        if (contacts == null || contacts.size() == 0) {
            Log.e(TAG, "refreshContacts："+0);
            return;
        }
        Log.e(TAG, "refreshContacts："+contacts.size());
        for (UserInfo userInfo : contacts) {
            contactsMap.put(userInfo.getHxid(), new EaseUser(userInfo.getHxid()));
        }
        setContactsMap(contactsMap);
        refresh();
    }

    private void updateRedPoint() {
        boolean isContactChanged = SpUtil.getInstance().getBoolean(SpUtil.IS_CONTACT_CHANGED, false);
        iv_fragment_contact_new_invitation.setVisibility(isContactChanged ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(mReceiver);
        mBroadcastManager.unregisterReceiver(mContactChangedReceiver);
        mBroadcastManager.unregisterReceiver(mGroupChangedReceiver);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        mHxid = ((EaseUser) listView.getItemAtPosition(position)).getUsername();
        getActivity().getMenuInflater().inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteContact();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteContact() {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(mHxid);
                    Module.getInstance().getDbManager().getContactDAO().deleteContact(mHxid);
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshContacts();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
        updateRedPoint();
        ll_fragment_main_contacts_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED, false);
                Intent intent = new Intent(getActivity(), InvitationListActivity.class);
                startActivity(intent);
            }
        });

        ll_fragment_main_contacts_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GroupListActivity.class);
                startActivity(intent);
            }
        });
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mBroadcastManager.registerReceiver(mReceiver,new IntentFilter(Constant.INVITATION_CHANGED));
        mBroadcastManager.registerReceiver(mContactChangedReceiver,new IntentFilter(Constant.CONTACT_CHANGED));
        mBroadcastManager.registerReceiver(mGroupChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));
        getContactsFromServer();
        registerForContextMenu(listView);
    }
}
