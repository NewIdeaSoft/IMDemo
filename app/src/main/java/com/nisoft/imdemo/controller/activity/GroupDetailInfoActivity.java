package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.adapter.GroupMembersGridAdapter;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailInfoActivity extends Activity {
    private static final String TAG = "GroupDetailInfoActivity";
    private GridView gv_group_detail_members;
    private TextView tv_group_detail_quit;
    private String mGroupId;
    private GroupMembersGridAdapter mAdapter;
    private List<String> mGroupMembers = new ArrayList<>();
    private EMGroup mGroup;
    private boolean isGroupOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_info);
        initView();
        initData();
        initButtonDisplay();
        initListener();
    }

    private void initButtonDisplay() {
        if (isGroupOwner) {
            tv_group_detail_quit.setText("解散群！");
        }
    }

    private void initData() {
        mGroupId = getIntent().getStringExtra(Constant.EXTRA_GROUP_ID);
        mGroup = EMClient.getInstance().groupManager().getGroup(mGroupId);
        isGroupOwner = EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner());
        boolean isCanModify = isGroupOwner || mGroup.isPublic();
        mAdapter = new GroupMembersGridAdapter(this, isCanModify) {
            @Override
            protected void onDeleteIconClick(final String hxid) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().removeUserFromGroup(mGroupId, hxid);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GroupDetailInfoActivity.this, "成员已删除", Toast.LENGTH_SHORT).show();
                                    getMemberFromHXServer();
                                }
                            });

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GroupDetailInfoActivity.this, "删除成员失败!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            protected void onAddClick() {
                //添加群成员
                Intent intent = new Intent(GroupDetailInfoActivity.this, ChooseGroupMemberActivity.class);
                intent.putExtra(Constant.EXTRA_GROUP_ID, mGroupId);
                startActivityForResult(intent, 3);
            }
        };
        gv_group_detail_members.setNumColumns(4);
        gv_group_detail_members.setHorizontalSpacing(2);
        gv_group_detail_members.setVerticalSpacing(2);
        gv_group_detail_members.setAdapter(mAdapter);
        getMemberFromHXServer();
    }

    private void getMemberFromHXServer() {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mGroup = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                    List<String> members = mGroup.getMembers();
                    mGroupMembers.clear();
                    mGroupMembers.addAll(members);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, e.toString());
                            Toast.makeText(GroupDetailInfoActivity.this, "从服务器获取群成员列表失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refresh() {
        mAdapter.refresh(mGroupMembers);
    }

    private void initListener() {
        if (isGroupOwner) {
            tv_group_detail_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(mGroupId);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailInfoActivity.this, "解散群成功！", Toast.LENGTH_SHORT).show();
                                        sendLeaveGroupBroadcast();
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailInfoActivity.this, "解散群失败!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {
            tv_group_detail_quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //退出该群
                    Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            String userId = Module.getInstance().getUserDao().getExitUerInfo().getHxid();
                            try {
                                EMClient.getInstance().groupManager().removeUserFromGroup(mGroupId, userId);
                                sendLeaveGroupBroadcast();
                                Toast.makeText(GroupDetailInfoActivity.this, "退群操作成功！", Toast.LENGTH_SHORT).show();
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailInfoActivity.this, "退群失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    private void sendLeaveGroupBroadcast() {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(new Intent(Constant.ACTION_LEAVE_GROUP));
    }

    private void initView() {
        gv_group_detail_members = (GridView) findViewById(R.id.gv_group_detail_members);
        tv_group_detail_quit = (TextView) findViewById(R.id.tv_group_detail_quit);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && mAdapter.isDeleteState()) {
            mAdapter.setDeleteState(false);
            mAdapter.notifyDataSetChanged();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 3) {
            final String[] members = data.getStringArrayExtra("members");
            if (members == null || members.length == 0) {
                return;
            }

            Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroupId, members);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (String member : members) {
                                    mGroupMembers.add(member);
                                }
                                refresh();
                                Toast.makeText(GroupDetailInfoActivity.this, "已成功发送群邀请！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailInfoActivity.this, "添加群成员失败！错误：" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
