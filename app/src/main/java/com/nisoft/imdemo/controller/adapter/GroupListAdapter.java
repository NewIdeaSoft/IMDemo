package com.nisoft.imdemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.nisoft.imdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mEMGroups = new ArrayList<>();
    public GroupListAdapter(Context context){
        mContext = context;
    }
    public void refresh(List<EMGroup> groups){
        mEMGroups.clear();
        mEMGroups.addAll(groups);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mEMGroups==null?0:mEMGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mEMGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_list,null);
            holder.mGroupName = (TextView) convertView.findViewById(R.id.tv_grouplist_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mGroupName.setText(mEMGroups.get(position).getGroupName());
        return convertView;
    }

    class ViewHolder{
        TextView mGroupName;
    }
}
