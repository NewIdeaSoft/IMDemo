package com.nisoft.imdemo.controller.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.activity.NewFriendActivity;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ContactsFragment extends EaseContactListFragment {
    private LinearLayout ll_fragment_main_contacts_new_friend;
    private LinearLayout ll_fragment_main_contacts_group;
    @Override
    protected void initView() {
        super.initView();
        titleBar.setTitle("联系人");
        View view = View.inflate(getActivity(), R.layout.header_fragment_main_contacts, null);
        ll_fragment_main_contacts_new_friend =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_new_friend);
        ll_fragment_main_contacts_group =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_group);
        ll_fragment_main_contacts_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewFriendActivity.class);
                startActivity(intent);
            }
        });

        ll_fragment_main_contacts_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.addHeaderView(view);
    }
}
