package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.adapter.GroupListAdapter;
import com.nisoft.imdemo.module.Module;

public class GroupListActivity extends Activity {
    private ListView lv_group_list;
    private GroupListAdapter mGroupAdapter;
    private LinearLayout ll_grouplist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv_group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    return;
                }
                Intent intent = new Intent(GroupListActivity.this,ChattingActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                intent.putExtra("hxid",((EMGroup)mGroupAdapter.getItem(position-1)).getGroupId());
                startActivity(intent);
            }
        });
        ll_grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this,CreateNewGroupActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        mGroupAdapter = new GroupListAdapter(this);
        lv_group_list.setAdapter(mGroupAdapter);
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "获取群组信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refresh() {
        mGroupAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView() {
        lv_group_list = (ListView)findViewById(R.id.lv_group_list);
        View headView = View.inflate(this, R.layout.item_create_group, null);
        lv_group_list.addHeaderView(headView);
        ll_grouplist = (LinearLayout)headView.findViewById(R.id.ll_grouplist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
