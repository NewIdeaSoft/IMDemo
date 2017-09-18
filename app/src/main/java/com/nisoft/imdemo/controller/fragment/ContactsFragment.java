package com.nisoft.imdemo.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.nisoft.imdemo.R;
import com.nisoft.imdemo.controller.activity.InvitationListActivity;
import com.nisoft.imdemo.controller.activity.NewFriendActivity;
import com.nisoft.imdemo.utils.Constant;
import com.nisoft.imdemo.utils.SpUtil;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ContactsFragment extends EaseContactListFragment {
    private LinearLayout ll_fragment_main_contacts_new_friend;
    private LinearLayout ll_fragment_main_contacts_group;
    private ImageView iv_fragment_contact_new_invitation;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_fragment_contact_new_invitation.setVisibility(View.VISIBLE);
            SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,true);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        titleBar.setTitle("联系人");
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });
        View view = View.inflate(getActivity(), R.layout.header_fragment_main_contacts, null);
        ll_fragment_main_contacts_new_friend =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_new_friend);
        ll_fragment_main_contacts_group =
                (LinearLayout) view.findViewById(R.id.ll_fragment_main_contacts_group);
        iv_fragment_contact_new_invitation =
                (ImageView) view.findViewById(R.id.iv_fragment_contact_new_invitation);
        updateRedPoint();
        ll_fragment_main_contacts_new_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().put(SpUtil.IS_CONTACT_CHANGED,false);
                Intent intent = new Intent(getActivity(),InvitationListActivity.class);
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

    private void updateRedPoint() {
        boolean isContactChanged = SpUtil.getInstance().getBoolean(SpUtil.IS_CONTACT_CHANGED,false);
        iv_fragment_contact_new_invitation.setVisibility(isContactChanged?View.VISIBLE:View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constant.INVITATION_CHANGED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRedPoint();
    }
}
