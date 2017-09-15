package com.nisoft.imdemo.controller.fragment;

import android.view.View;

import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.nisoft.imdemo.R;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ContactsFragment extends EaseContactListFragment {
    @Override
    protected void initView() {
        super.initView();
        View.inflate(getActivity(), R.layout.header_fragment_main_contacts,null);
    }
}
