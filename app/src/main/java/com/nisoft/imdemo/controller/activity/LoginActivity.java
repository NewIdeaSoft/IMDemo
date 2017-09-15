package com.nisoft.imdemo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.Module;
import com.nisoft.imdemo.module.bean.UserInfo;

public class LoginActivity extends Activity {
    private EditText et_login_name;
    private EditText et_login_password;
    private Button btn_login_register;
    private Button btn_login_login;

    private String mInputName;
    private String mInputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    /**
     * 设置控件监听
     */
    private void initListener() {
        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getInputData()) {//如果输入的用户名或密码不为空

                    //登陆请求
                    Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            EMClient.getInstance().login(mInputName, mInputPassword, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //保存用户名密码用户数据
                                            UserInfo user = new UserInfo();
                                            user.setName(mInputName);
                                            user.setPassword(mInputPassword);
                                            Module.getInstance().getUserDao().putUserInfo(user);
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }

                                @Override
                                public void onError(int i, final String s) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登失败！"+s, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }
                    });
                }


            }
        });
        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getInputData()) {//如果输入的用户名或密码不为空
                    //注册请求
                    Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(mInputName,mInputPassword);
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "注册失败！"+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 实例化控件
     */
    private void initView(){
        et_login_name = (EditText)findViewById(R.id.et_login_name);
        et_login_password = (EditText)findViewById(R.id.et_login_password);
        btn_login_register = (Button)findViewById(R.id.btn_login_register);
        btn_login_login = (Button)findViewById(R.id.btn_login_login);
    }

    /**
     * 获取输入的用户名和密码 并进行判定
     * @return true 用户名密码符合要求 false用户名或密码为空
     */
    private boolean getInputData(){
        mInputName = et_login_name.getText().toString();
        mInputPassword = et_login_password.getText().toString();
        if(TextUtils.isEmpty(mInputName)||TextUtils.isEmpty(mInputPassword)) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
