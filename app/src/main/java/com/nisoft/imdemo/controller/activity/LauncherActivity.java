package com.nisoft.imdemo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.Module;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        //显示2s后进入下一界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toLoginOrToMain();
            }
        },2000);
    }

    /**
     * 已登陆过则直接进入主界面，否则进入登陆界面
     */
    private void toLoginOrToMain() {
        Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(EMClient.getInstance().isLoggedInBefore()) {
                    //获取用户数据

                    intent = new Intent(LauncherActivity.this,MainActivity.class);
                }else{
                    intent = new Intent(LauncherActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });

    }
}
