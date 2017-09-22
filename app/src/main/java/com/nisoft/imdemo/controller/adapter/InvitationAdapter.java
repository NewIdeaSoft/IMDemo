package com.nisoft.imdemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nisoft.imdemo.R;
import com.nisoft.imdemo.module.bean.Invitation;
import com.nisoft.imdemo.module.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class InvitationAdapter extends BaseAdapter {
    private Context mContext;
    private ItemButtonClickListener mItemButtonClickListener;
    private List<Invitation> mInvitationList = new ArrayList<>();
    private Invitation mCurrentInvitation;

    public InvitationAdapter(Context context, ItemButtonClickListener itemButtonClickListener) {
        mContext = context;
        mItemButtonClickListener = itemButtonClickListener;
    }

    public void refreshData(List<Invitation> invitationList) {
        mInvitationList = invitationList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mInvitationList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mCurrentInvitation = (Invitation) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_invitation_list, null);
            holder.mTextViewName = (TextView) convertView.findViewById(R.id.tv_invitation_name);
            holder.mTextViewReason = (TextView) convertView.findViewById(R.id.tv_invitation_reason);
            holder.mAcceptButton = (Button) convertView.findViewById(R.id.btn_invitation_accept);
            holder.mRejectButton = (Button) convertView.findViewById(R.id.btn_invitation_reject);
            holder.mLinearLayoutButtons = (LinearLayout) convertView.findViewById(R.id.ll_item_invitation_buttons);
            holder.mTextViewStateInfo = (TextView) convertView.findViewById(R.id.tv_state_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mLinearLayoutButtons.setVisibility(View.VISIBLE);
        holder.mAcceptButton.setVisibility(View.GONE);
        holder.mRejectButton.setVisibility(View.GONE);
        holder.mTextViewStateInfo.setVisibility(View.GONE);
//        if(mCurrentInvitation.getState() == Invitation.InvokeState.NEW_SELF_INVITE) {
//            holder.mTextViewStateInfo.setVisibility(View.VISIBLE);
//            holder.mTextViewStateInfo.setText("等待对方处理好友请求");
//        }else{
//            holder.mLinearLayoutButtons.setVisibility(View.VISIBLE);
//            holder.mAcceptButton.setVisibility(View.GONE);
//            holder.mRejectButton.setVisibility(View.GONE);
//        }
        UserInfo userInfo = mCurrentInvitation.getUserInfo();
        if (userInfo == null) {


        } else {
            holder.mTextViewName.setText(userInfo.getHxid());

            switch (mCurrentInvitation.getState()) {
                case INVITE_ACCEPT:
                    if (mCurrentInvitation.getReason() == null) {
                        holder.mTextViewReason.setText("已经接受了对方的好友邀请");
                    }

                    break;
                case INVITE_ACCEPT_BY_PEER:
                    if (mCurrentInvitation.getReason() == null) {
                        holder.mTextViewReason.setText("对方接受了您的好友邀请");
                    }
                    break;
                case NEW_INVITE:
                    if (mCurrentInvitation.getReason() == null) {
                        holder.mTextViewReason.setText("对方请求添加您为好友");
                    }
                    holder.mAcceptButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        holder.mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemButtonClickListener.onRejectButtonClick(mCurrentInvitation);
            }
        });
        holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemButtonClickListener.onAcceptButtonClick(mCurrentInvitation);
            }
        });
        return convertView;
    }

    public interface ItemButtonClickListener {
        void onAcceptButtonClick(Invitation invitation);

        void onRejectButtonClick(Invitation invitation);
    }

    class ViewHolder {
        TextView mTextViewName;
        TextView mTextViewReason;
        Button mAcceptButton;
        Button mRejectButton;
        TextView mTextViewStateInfo;
        LinearLayout mLinearLayoutButtons;
    }
}
