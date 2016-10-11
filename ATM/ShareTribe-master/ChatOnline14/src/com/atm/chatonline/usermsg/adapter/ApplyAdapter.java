package com.atm.chatonline.usermsg.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.usermsg.adapter.ReplyAdapter.ViewHolder;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.ReplyMessage;
import com.example.studentsystem01.R;

public class ApplyAdapter extends BaseAdapter {

	private Context mContext;
	private int resId;
	private List<ApplyMessage> list;

	public ApplyAdapter(Context mContext, int resId, List<ApplyMessage> list) {
		super();
		this.mContext = mContext;
		this.resId = resId;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO �Զ����ɵķ������
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApplyMessage msg=list.get(position);
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(resId, null);
			viewHolder=new ViewHolder();
			viewHolder.headImage=(CircleImageView) convertView.findViewById(R.id.applymsg_headimg);
			viewHolder.applyContent=(TextView) convertView.findViewById(R.id.applymsg_test_content);
			viewHolder.applyNickName=(TextView) convertView.findViewById(R.id.applymsg_nickname);
			viewHolder.applyTime=(TextView) convertView.findViewById(R.id.applyMsgTime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.headImage.setImageDrawable(msg.getContent().getHeadImage());
		viewHolder.applyContent.setText(msg.getContent().getEssayTitle());
		viewHolder.applyNickName.setText(msg.getContent().getNickname());
		viewHolder.applyTime.setText(msg.getContent().getReplyTime());
		return convertView;
	}

	
	class ViewHolder{
		CircleImageView headImage;
		TextView applyNickName,applyContent,applyTime;
	}
}
