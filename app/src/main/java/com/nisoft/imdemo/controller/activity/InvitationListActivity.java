package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.adapter.InvitationAdapter;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class InvitationListActivity extends Activity {
    private ListView lv_invitation;
    private List<Invitation> mInvitationList = new ArrayList<>();
    private InvitationAdapter mAdapter;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver invitationChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.INVITATION_CHANGED)) {
                refreshInvitationList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(invitationChangedReceiver, new IntentFilter(Constant.INVITATION_CHANGED));
        mLBM.registerReceiver(invitationChangedReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGED));
        mAdapter = new InvitationAdapter(this, new InvitationAdapter.ItemButtonClickListener() {
            @Override
            public void onAcceptContactInvitation(final Invitation invitation) {
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
                                    Toast.makeText(InvitationListActivity.this, "操作失败！错误：" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onRejectContactInvitation(final Invitation invitation) {
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

            @Override
            public void onAcceptGroupInvitation(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager()
                                    .acceptInvitation(invitation.getGroup().getGroupId()
                                            , invitation.getGroup().getInviterName());
                            invitation.setState(Invitation.InvokeState.GROUP_ACCEPT_INVITE);
                            Module.getInstance().getDbManager()
                                    .getInvitationDAO()
                                    .addInvitation(invitation);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInvitationList();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onRejectGroupInvitation(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager()
                                    .declineInvitation(invitation.getGroup().getGroupId()
                                            , invitation.getGroup().getInviterName()
                                            , "拒绝了群邀请");
                            invitation.setState(Invitation.InvokeState.GROUP_REJECT_INVITE);
                            Module.getInstance().getDbManager()
                                    .getInvitationDAO()
                                    .addInvitation(invitation);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInvitationList();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onAcceptGroupApplication(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager()
                                    .acceptInvitation(invitation.getGroup().getGroupId()
                                            , invitation.getGroup().getInviterName());
                            invitation.setState(Invitation.InvokeState.GROUPO_ACCEPT_APPLICATION);
                            Module.getInstance().getDbManager()
                                    .getInvitationDAO()
                                    .addInvitation(invitation);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInvitationList();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onRejectGroupApplication(final Invitation invitation) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager()
                                    .declineApplication(invitation.getGroup().getGroupId()
                                            , invitation.getGroup().getInviterName()
                                            , "拒绝了群申请");
                            invitation.setState(Invitation.InvokeState.GROUP_REJECT_APPLICATION);
                            Module.getInstance().getDbManager()
                                    .getInvitationDAO()
                                    .addInvitation(invitation);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshInvitationList();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InvitationListActivity.this, "操作失败，请重试！", Toast.LENGTH_SHORT).show();
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
        lv_invitation = (ListView) findViewById(R.id.lv_invitation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(invitationChangedReceiver);
    }
}
