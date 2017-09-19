package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.adapter.InvitationAdapter;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.Invitation;

import java.util.List;

public class InvitationListActivity extends Activity {
    private ListView lv_invitation;
    private List<Invitation> mInvitationList;
    private InvitationAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list);
        initView();
        initData();
    }

    private void initData() {
        mInvitationList = Module.getInstance().getDbManager().getInvitationDAO().findAll();
        mAdapter = new InvitationAdapter(this, new InvitationAdapter.ItemButtonClickListener() {
            @Override
            public void onAcceptButtonClick(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(invitation.getUserInfo().getHxid());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        Module.getInstance().getDbManager().getInvitationDAO().updateInvitation(invitation.getUserInfo().getHxid(), Invitation.InvokeState.INVITE_ACCEPT);
                    }
                });

            }

            @Override
            public void onRejectButtonClick(Invitation invitation) {

            }
        });
        lv_invitation.setAdapter(mAdapter);
        mAdapter.refreshData(mInvitationList);
    }

    private void initView() {
        lv_invitation = (ListView)findViewById(R.id.lv_invitation);
    }
}
