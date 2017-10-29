package com.nisoft.imdemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.utils.Constant;

public class ChattingActivity extends FragmentActivity {

    private String mHxid;
    private int mChatType;
    private EaseChatFragment mChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        initData();
        initListener();
    }

    private void initListener() {
        mChatFragment.setChatFragmentHelper(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }

            @Override
            public void onEnterToChatDetails() {
                Intent intent = new Intent(ChattingActivity.this,GroupDetailInfoActivity.class);
                intent.putExtra(Constant.EXTRA_GROUP_ID,mHxid);
                startActivity(intent);
            }

            @Override
            public void onAvatarClick(String username) {

            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });
    }

    private void initData() {
        mHxid = getIntent().getStringExtra("hxid");
        mChatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE,-1);

        mChatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID, mHxid);
        if(mChatType !=-1) {
            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, mChatType);
        }
        mChatFragment.setArguments(args);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.chatting_fragment_content, mChatFragment).commit();
    }


}
