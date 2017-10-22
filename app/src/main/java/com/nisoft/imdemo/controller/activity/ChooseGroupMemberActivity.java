package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.adapter.PickMemberAdapter;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.PickMemberItem;
import com.nisoft.imdemo.module.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ChooseGroupMemberActivity extends Activity {
    private TextView tv_choose_member_complete;
    private ListView lv_choose_member;
    private List<PickMemberItem> mMemberItems = new ArrayList<>();
    private PickMemberAdapter mMemberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_member);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv_choose_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_item_pick_member);
                checkBox.setChecked(!checkBox.isChecked());
                mMemberItems.get(position).setChecked(checkBox.isChecked());
                mMemberAdapter.setPickMemberItems(mMemberItems);
                mMemberAdapter.notifyDataSetChanged();
            }
        });
        tv_choose_member_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> pickedMembers = mMemberAdapter.getPickedMembers();
                Intent intent = new Intent();
                intent.putExtra("members",pickedMembers.toArray(new String[0]));
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    private void initData() {
        List<UserInfo> allContacts = Module.getInstance().getDbManager().getContactDAO().getAllContacts();
        for (UserInfo user:allContacts){
            PickMemberItem item = new PickMemberItem(user,false);
            mMemberItems.add(item);
        }
        mMemberAdapter = new PickMemberAdapter(this,mMemberItems);
        lv_choose_member.setAdapter(mMemberAdapter);
    }

    private void initView() {
        tv_choose_member_complete = (TextView)findViewById(R.id.tv_choose_member_complete);
        lv_choose_member = (ListView)findViewById(R.id.lv_choose_member);
    }
}
