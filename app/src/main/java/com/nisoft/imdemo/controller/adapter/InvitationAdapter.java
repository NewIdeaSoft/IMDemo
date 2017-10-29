package com.nisoft.imdemo.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        if(invitationList==null||invitationList.size()==0) {
            return;
        }
        mInvitationList.clear();
        mInvitationList.addAll(invitationList);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_invitation_list, null);
            holder.mTextViewName = (TextView) convertView.findViewById(R.id.tv_invitation_name);
            holder.mTextViewReason = (TextView) convertView.findViewById(R.id.tv_invitation_reason);
            holder.mAcceptButton = (Button) convertView.findViewById(R.id.btn_invitation_accept);
            holder.mRejectButton = (Button) convertView.findViewById(R.id.btn_invitation_reject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mAcceptButton.setVisibility(View.GONE);
        holder.mRejectButton.setVisibility(View.GONE);
        UserInfo userInfo = mCurrentInvitation.getUserInfo();
        if (userInfo == null) {
            holder.mTextViewName.setText(mCurrentInvitation.getGroup().getGroupName());
            switch (mCurrentInvitation.getState()) {
                case NEW_GROUP_INVITE:
                    holder.mTextViewReason.setText("收到新的群邀请");
                    holder.mAcceptButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onRejectGroupInvitation(mCurrentInvitation);
                        }
                    });
                    holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onAcceptGroupInvitation(mCurrentInvitation);
                        }
                    });
                    break;
                case NEW_GROUP_APPLICATION:
                    holder.mTextViewReason.setText("申请加入群");
                    holder.mAcceptButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onRejectGroupApplication(mCurrentInvitation);
                        }
                    });
                    holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onAcceptGroupApplication(mCurrentInvitation);
                        }
                    });
                    break;
                case GROUP_ACCEPT_INVITE:
                    holder.mTextViewReason.setText("接受了对方的群邀请");
                    break;
                case GROUP_REJECT_INVITE:
                    holder.mTextViewReason.setText("拒绝了对方的群邀请");
                    break;
                case GROUP_INVITE_ACCEPTED:
                    holder.mTextViewReason.setText("接受了您的群邀请");
                    break;
                case GROUP_INVITE_DECLINED:
                    holder.mTextViewReason.setText("拒绝了您的群邀请");
                    break;
                case GROUP_APPLICATION_ACCEPTED:
                    holder.mTextViewReason.setText("接受了您的群申请");
                    break;
                case GROUP_APPLICATION_DECLINED:
                    holder.mTextViewReason.setText("拒绝了您的群申请");
                    break;
                case GROUPO_ACCEPT_APPLICATION:
                    holder.mTextViewReason.setText("接受对方加入群");
                    break;
                case GROUP_REJECT_APPLICATION:
                    holder.mTextViewReason.setText("拒绝对方加入群");
                    break;
            }

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

                    holder.mTextViewReason.setText("对方请求添加您为好友");
                    holder.mAcceptButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setVisibility(View.VISIBLE);
                    holder.mRejectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onRejectContactInvitation(mCurrentInvitation);
                        }
                    });
                    holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemButtonClickListener.onAcceptContactInvitation(mCurrentInvitation);
                        }
                    });
                    break;
                default:
                    break;
            }

        }


        return convertView;
    }

    public interface ItemButtonClickListener {
        void onAcceptContactInvitation(Invitation invitation);

        void onRejectContactInvitation(Invitation invitation);
        void onAcceptGroupInvitation(Invitation invitation);
        void onRejectGroupInvitation(Invitation invitation);
        void onAcceptGroupApplication(Invitation invitation);
        void onRejectGroupApplication(Invitation invitation);
    }

    class ViewHolder {
        TextView mTextViewName;
        TextView mTextViewReason;
        Button mAcceptButton;
        Button mRejectButton;
    }
}
