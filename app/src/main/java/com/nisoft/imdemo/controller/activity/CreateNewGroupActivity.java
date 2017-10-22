package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.Module;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            String[] members = data.getStringArrayExtra("members");
            createGroup(members);
        }
    }

    private void createGroup(final String[] members) {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMGroupOptions options = new EMGroupOptions();
                String groupName = et_newgroup_name.getText().toString();
                String groupDesc = et_newgroup_desc.getText().toString();
                boolean isPublic = cb_newgroup_publish.isChecked();
                boolean isPublicInvite = cb_newgroup_publish_invite.isChecked();
                options.maxUsers = 200;
                EMGroupManager.EMGroupStyle groupStyle = null;
                if(isPublic) {
                    if(isPublicInvite) {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else{
                    if(isPublicInvite) {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                options.style = groupStyle;
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName,groupDesc,members,"",options);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateNewGroupActivity.this, "创建群成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateNewGroupActivity.this, "创建群失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
