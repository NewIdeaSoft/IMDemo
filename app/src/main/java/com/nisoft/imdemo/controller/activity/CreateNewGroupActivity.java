package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.nisoft.imdemo.R;

public class CreateNewGroupActivity extends Activity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_desc;
    private CheckBox cb_newgroup_publish;
    private CheckBox cb_newgroup_publish_invite;
    private Button btn_newgroup_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        initView();
        initListener();
    }

    private void initListener() {
        btn_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewGroupActivity.this,ChooseGroupMemberActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void initView() {
        et_newgroup_name = (EditText)findViewById(R.id.et_newgroup_name);
        et_newgroup_desc = (EditText)findViewById(R.id.et_newgroup_desc);
        cb_newgroup_publish = (CheckBox)findViewById(R.id.cb_newgroup_publish);
        cb_newgroup_publish_invite = (CheckBox)findViewById(R.id.cb_newgroup_publish_invite);
        btn_newgroup_create = (Button)findViewById(R.id.btn_newgroup_create);

    }
}
