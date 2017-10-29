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

public abstract class GroupMembersGridAdapter extends BaseAdapter {
    private Context mContext;
    private boolean isCanModify;
    private boolean isDeleteState;
    private List<String> mGroupMembers = new ArrayList<>();

    public GroupMembersGridAdapter(Context context, boolean canModify) {
        mContext = context;
        isCanModify = canModify;
    }

    @Override
    public int getCount() {
        return mGroupMembers.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        return position >= mGroupMembers.size() ? null : mGroupMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_members_grid, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_item_members_grid);
            holder.mDeleteIcon = (ImageView) convertView.findViewById(R.id.iv_item_members_grid_delete);
            holder.mTextView = (TextView) convertView.findViewById(R.id.tv_item_members_grid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //处理界面显示的三种状态  可编辑非删除；可编辑删除；不可编辑
        if(isCanModify) {
            if(isDeleteState) {//可编辑删除
                if(position<getCount()-2) {

                        holder.mDeleteIcon.setVisibility(View.VISIBLE);

                }else{
                    holder.mDeleteIcon.setVisibility(View.GONE);
                    holder.mImageView.setVisibility(View.GONE);
                    holder.mTextView.setVisibility(View.GONE);

                }
            }else {//可编辑非删除
                if (position == getCount() - 2) {
                    holder.mImageView.setImageResource(R.drawable.em_smiley_add_btn_nor);
                    holder.mTextView.setVisibility(View.GONE);
                } else if (position == getCount() - 1) {
                    holder.mImageView.setImageResource(R.drawable.em_smiley_minus_btn_nor);
                    holder.mTextView.setVisibility(View.GONE);
                }else{
                    holder.mTextView.setText((String) getItem(position));
                }
            }
        }else{//不可编辑
            if(position<getCount()-2) {
                holder.mDeleteIcon.setVisibility(View.GONE);
                holder.mTextView.setText(mGroupMembers.get(position));
            }else{
                holder.mDeleteIcon.setVisibility(View.GONE);
                holder.mImageView.setVisibility(View.GONE);
                holder.mTextView.setVisibility(View.GONE);
            }
        }
        //点击事件监听
        if(isCanModify) {
            if(isDeleteState()) {
                holder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteIconClick(mGroupMembers.get(position));
                    }
                });
            }else {
                if(position==getCount()-1) {//减号
                    holder.mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onDeleteClick();
                        }
                    });

                }else if(position==getCount()-2) {//加号
                    onAddClick();
                }
            }
        }
        return convertView;
    }

    protected abstract void onDeleteIconClick(String hxid);

    protected void onDeleteClick() {
        setDeleteState(true);
        notifyDataSetChanged();
    }

    protected abstract void onAddClick();

    public boolean isDeleteState() {
        return isDeleteState;
    }

    public void setDeleteState(boolean deleteState) {
        isDeleteState = deleteState;
    }

    public void refresh(List<String> groupMembers) {
        mGroupMembers.clear();
        mGroupMembers.addAll(groupMembers);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView mImageView;
        ImageView mDeleteIcon;
        TextView mTextView;
    }
    public interface OnClickListener{

    }
}
