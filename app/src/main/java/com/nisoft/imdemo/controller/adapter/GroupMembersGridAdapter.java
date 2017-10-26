package com.nisoft.imdemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nisoft.imdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class GroupMembersGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mGroupMembers = new ArrayList<>();

    public GroupMembersGridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mGroupMembers.size()+2;
    }

    @Override
    public Object getItem(int position) {
        return position>=mGroupMembers.size()?null:mGroupMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_members_grid,null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_item_members_grid);
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_item_members_grid);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==getCount()-2) {
            holder.mImageView.setImageResource(R.drawable.em_smiley_add_btn_nor);
        }else if(position ==getCount()-1){
            holder.mImageView.setImageResource(R.drawable.em_smiley_minus_btn_nor);
        }else{
            holder.mTextView.setText((String)getItem(position));
        }
        return convertView;
    }

    public void refresh(List<String> groupMembers) {
        mGroupMembers.clear();
        mGroupMembers.addAll(groupMembers);
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
    }
}
