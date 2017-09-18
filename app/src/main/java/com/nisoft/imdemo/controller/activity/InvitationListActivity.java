package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.Invitation;

import java.util.List;

public class InvitationListActivity extends Activity {
    private ListView lv_invitation;
    private List<Invitation> mInvitationList;
    private BaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv_invitation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void initData() {
        mInvitationList = Module.getInstance().getDbManager().getInvitationDAO().findAll();
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }
        };
        lv_invitation.setAdapter(mAdapter);
    }

    private void initView() {
        lv_invitation = (ListView)findViewById(R.id.lv_invitation);
    }
}
