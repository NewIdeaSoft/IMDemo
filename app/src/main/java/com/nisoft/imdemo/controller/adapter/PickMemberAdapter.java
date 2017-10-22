package com.nisoft.imdemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.bean.PickMemberItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/22.
 */

public class PickMemberAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickMemberItem> mPickMemberItems;

    public PickMemberAdapter(Context context, List<PickMemberItem> pickMemberItems) {
        mContext = context;
        mPickMemberItems = pickMemberItems;
    }

    public void setPickMemberItems(List<PickMemberItem> pickMemberItems) {
        mPickMemberItems = pickMemberItems;
    }

    @Override
    public int getCount() {
        return mPickMemberItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mPickMemberItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView ==null) {
            convertView = View.inflate(mContext, R.layout.item_pick_member_list,null);
            holder = new ViewHolder();
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_item_pick_member);
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_item_pick_member);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        PickMemberItem item = mPickMemberItems.get(position);
        holder.mCheckBox.setChecked(item.isChecked());
        holder.mTextView.setText(item.getUserInfo().getHxid());
        return convertView;
    }
    public List<String> getPickedMembers(){
        List<String> pickedMembers = new ArrayList<>();
        for (PickMemberItem item : mPickMemberItems) {
            if(item.isChecked()) {
                pickedMembers.add(item.getUserInfo().getHxid());
            }
        }
        return pickedMembers;
    }
    class ViewHolder{
        private CheckBox mCheckBox;
        private TextView mTextView;
    }
}
