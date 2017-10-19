package com.nisoft.imdemo.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.nisoft.imdemo.R;

public class ChattingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        String hxid = getIntent().getStringExtra("hxid");
        String chatType = getIntent().getStringExtra(EaseConstant.EXTRA_CHAT_TYPE);
        Fragment fragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID,hxid);
        if(chatType!=null) {
            args.putString(EaseConstant.EXTRA_CHAT_TYPE,chatType);
        }
        fragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.chatting_fragment_content,fragment).commit();
    }


}
