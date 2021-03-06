package com.atm.chatonline.usercenter.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atm.charonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.commom.CircleImageView;
import com.example.studentsystem01.R;





public class NoteAdapter extends ArrayAdapter<BBS> {
	private String tag ="BBSAdapter";
	private int resID;
	Button btnDel;
	public NoteAdapter(Context context, int resID, List<BBS> objects,Button btnDel){
		super(context, resID , objects);
		this.resID = resID;
		this.btnDel=btnDel;
	}
	
	class ViewHolder{
		TextView clickGoodNum, publishTime, publishID,replyNum,title,someContent;
		CheckBox box;
		CircleImageView headImage;
		TextView [] lab = new TextView[3];
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		BBS bbs = getItem(position);
		View view ;
		ViewHolder viewHolder ;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resID, null);
			viewHolder = new ViewHolder();
			viewHolder.clickGoodNum = (TextView) view.findViewById(R.id.note_clickGoodNum);
			viewHolder.publishTime = (TextView) view.findViewById(R.id.note_publishTime);
			viewHolder.publishID = (TextView) view.findViewById(R.id.note_publishID);
			viewHolder.replyNum = (TextView) view.findViewById(R.id.note_replynum);
			viewHolder.title = (TextView) view.findViewById(R.id.note_title);
			viewHolder.someContent = (TextView) view.findViewById(R.id.note_someContent);
			viewHolder.headImage = (CircleImageView) view.findViewById(R.id.note_headImage);
			viewHolder.box=(CheckBox) view.findViewById(R.id.usercenter_note_check_box);
			int [] labs = {R.id.note_labName1,R.id.note_labName2,R.id.note_labName3};
			for(int i = 0 ; i < viewHolder.lab.length ; i++){
				viewHolder.lab[i] = (TextView) view.findViewById(labs[i]);
			}
			view.setTag(viewHolder);//将ViewHolder存储在view中
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
		}
		viewHolder.clickGoodNum.setText(bbs.getClickGoodNum());
		viewHolder.publishTime.setText(bbs.getPublishTime());
		viewHolder.publishID.setText(bbs.getNickname());
		viewHolder.replyNum.setText(bbs.getReplyNum());
		viewHolder.title.setText("【"+bbs.getEssayType()+"】"+bbs.getTitle());
		viewHolder.someContent.setText(bbs.getSomeContent());
		viewHolder.headImage.setImageDrawable(bbs.getHeadImage());
		if(btnDel.getVisibility()==View.VISIBLE){
			viewHolder.box.setVisibility(View.VISIBLE);
		}
		
		//判断是否加标签“最新/最热”
//		if(bbs.getFlag()!=null) {
//			if(bbs.getFlag().equals("hot")) {
//				viewHolder.type.setText("最热");
//				viewHolder.type.setVisibility(View.VISIBLE);
//				Log.d("flag",bbs.getFlag());
//			}else if(bbs.getFlag().equals("new")) {
//				viewHolder.type.setText("最新");
//				viewHolder.type.setVisibility(View.VISIBLE);
//			}
//			else {
//				viewHolder.type.setVisibility(View.GONE);
//			}
//		}
		//加载标签
		for(int i=0;i<bbs.getLabName0().length;i++){
			viewHolder.lab[i].setText(bbs.getLabName0()[i]);
//			Log.i(tag, "bbs.getLabColor0()[i]"+bbs.getLabColor0()[i]);
			viewHolder.lab[i].setBackgroundColor(bbs.getLabColor0()[i]);
			viewHolder.lab[i].setVisibility(View.VISIBLE);
		}
		for(int i = 2 ; i >= bbs.getLabName0().length ; i--){
			viewHolder.lab[i].setVisibility(View.INVISIBLE);
		}
		return view;
	}
}
