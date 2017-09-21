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

import java.util.ArrayList;
import java.util.List;

public class InvitationListActivity extends Activity {
    private ListView lv_invitation;
    private List<Invitation> mInvitationList = new ArrayList<>();
    private InvitationAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list);
        initView();
        initData();
    }

    private void initData() {
        mAdapter = new InvitationAdapter(this, new InvitationAdapter.ItemButtonClickListener() {
            @Override
            public void onAcceptButtonClick(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().acceptInvitation(invitation.getUserInfo().getHxid());
                            Module.getInstance().getDbManager().getInvitationDAO().updateInvitation(invitation.getUserInfo().getHxid(), Invitation.InvokeState.INVITE_ACCEPT);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "成功接受好友邀请！", Toast.LENGTH_SHORT).show();
                                    //刷新页面
                                    refreshInvitationList();
                                }
                            });
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败！错误："+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onRejectButtonClick(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().declineInvitation(invitation.getUserInfo().getHxid());
                            Module.getInstance().getDbManager().getInvitationDAO().deleteInvitation(invitation.getUserInfo().getHxid());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "拒绝了对方的好友请求！", Toast.LENGTH_SHORT).show();
                                    refreshInvitationList();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
        lv_invitation.setAdapter(mAdapter);
        refreshInvitationList();
    }

    private void refreshInvitationList() {
        mInvitationList.clear();
        mInvitationList.addAll(Module.getInstance().getDbManager().getInvitationDAO().findAll());
        mAdapter.refreshData(mInvitationList);
    }

    private void initView() {
        lv_invitation = (ListView)findViewById(R.id.lv_invitation);
    }
}
