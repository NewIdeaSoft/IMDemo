package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private GridView gv_group_detail_members;
    private TextView tv_group_detail_quit;
    private String mGroupId;
    private GroupMembersGridAdapter mAdapter;
    private List<String> mGroupMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_info);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        mGroupId = getIntent().getStringExtra(Constant.EXTRA_GROUP_ID);
        mAdapter = new GroupMembersGridAdapter(this);
        gv_group_detail_members.setNumColumns(4);
        gv_group_detail_members.setHorizontalSpacing(2);
        gv_group_detail_members.setAdapter(mAdapter);
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                    List<String> members = group.getMembers();
                    mGroupMembers.clear();
                    mGroupMembers.addAll(members);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
        gv_group_detail_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int count = mGroupMembers.size();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = mGroupMembers.size();
                if(position == count-2) {
                    //添加群成员
                }else if(position == count -1){
                    //删除群成员
                }else{
                    //进入联系人详情
                }
            }
        });
        gv_group_detail_members.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int count = mGroupMembers.size();
                if(position < count-2) {
                    //标记群成员
                }
                return true;
            }
        });
        tv_group_detail_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出该群
            }
        });
    }

    private void initView() {
        gv_group_detail_members = (GridView)findViewById(R.id.gv_group_detail_members);
        tv_group_detail_quit = (TextView)findViewById(R.id.tv_group_detail_quit);
    }
}
