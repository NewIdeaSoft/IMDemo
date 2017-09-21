package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.module.bean.UserInfo;

public class NewFriendActivity extends Activity {

    private TextView tv_new_friend_search;
    private EditText et_new_friend_name;
    private LinearLayout ll_new_friend_search_result;
    private ImageView iv_new_friend_search_result_icon;
    private TextView tv_new_friend_search_result_name;
    private Button btn_new_friend_add;
    private UserInfo mNewFriendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
        initListener();
    }

    private void initListener() {
        tv_new_friend_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFriend();
            }
        });
        btn_new_friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewFriend();
            }
        });
    }

    private void findFriend() {
        final String friendName = et_new_friend_name.getText().toString();

        UserInfo userInfo = new UserInfo();
        userInfo.setName(friendName);
        mNewFriendInfo = userInfo;
        ll_new_friend_search_result.setVisibility(View.VISIBLE);
        tv_new_friend_search_result_name.setText(mNewFriendInfo.getName());
//        if(isOldFriend(friendName)) {
//            btn_new_friend_add.setVisibility(View.GONE);
//        }
//        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                final String friendName = et_new_friend_name.getText().toString();
//
//                UserInfo userInfo = new UserInfo();
//                userInfo.setName(friendName);
//
//            }
//        });
    }

    private boolean isOldFriend(String friendName) {
        UserInfo contact = Module.getInstance().getDbManager().getContactDAO().findContact(friendName);
        if(contact != null) {
            return true;
        }
        return false;
    }

    private void addNewFriend() {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            String toastText;

            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(mNewFriendInfo.getName(), "添加好友");
                    toastText = "添加成功！";
                    Invitation invitation = new Invitation();
                    String friendName = tv_new_friend_search_result_name.getText().toString();
                    UserInfo userInfo = new UserInfo(friendName);
                    invitation.setUserInfo(userInfo);
                    invitation.setReason("向对方发送了一个好友请求");
                    invitation.setState(Invitation.InvokeState.NEW_SELF_INVITE);
                    Module.getInstance().getDbManager().getInvitationDAO().addInvitation(invitation);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    toastText = "添加失败！错误：" + e.toString();
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewFriendActivity.this, toastText, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    private void initView() {
        tv_new_friend_search = (TextView) findViewById(R.id.tv_new_friend_search);
        et_new_friend_name = (EditText) findViewById(R.id.et_new_friend_name);
        ll_new_friend_search_result = (LinearLayout) findViewById(R.id.ll_new_friend_search_result);
        iv_new_friend_search_result_icon = (ImageView) findViewById(R.id.iv_new_friend_search_result_icon);
        tv_new_friend_search_result_name = (TextView) findViewById(R.id.tv_new_friend_search_result_name);
        btn_new_friend_add = (Button) findViewById(R.id.btn_new_friend_add);
    }
}
