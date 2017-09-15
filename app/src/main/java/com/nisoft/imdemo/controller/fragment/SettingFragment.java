package com.nisoft.imdemo.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.activity.LoginActivity;
import com.nisoft.imdemo.module.Module;

/**
 * Created by Administrator on 2017/9/15.
 */

public class SettingFragment extends Fragment {
    private ScrollView sv_fragment_setting;
    private TextView tv_fragment_setting_logout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting,null);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        tv_fragment_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Module.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {

    }

    private void initView(View view){
        sv_fragment_setting = (ScrollView) view.findViewById(R.id.sv_fragment_setting);
        tv_fragment_setting_logout = (TextView) view.findViewById(R.id.tv_fragment_setting_logout);
        tv_fragment_setting_logout.append(EMClient.getInstance().getCurrentUser());
    }
}
