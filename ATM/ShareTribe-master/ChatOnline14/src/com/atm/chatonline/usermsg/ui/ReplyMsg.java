package com.atm.chatonline.usermsg.ui;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.charonline.bbs.util.ExtendsIntent;
import com.atm.charonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.usermsg.adapter.ReplyAdapter;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.MessageData;
import com.atm.chatonline.usermsg.bean.ReplyMessage;
import com.atm.chatonline.usermsg.bean.ReplyMessageData;
import com.atm.chatonline.usermsg.util.CacheData;
import com.atm.chatonline.usermsg.util.CacheManager;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.atm.chatonline.usermsg.util.MyMsgReceivePhoto;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 *  �ҵ���Ϣ�����@�ҵĽ���
 */

public class ReplyMsg extends BaseActivity implements OnClickListener{

	//private View mView;
	private ProgressBar pro;
	private String userId;
	private CacheManager cacheManager;
	private boolean hasCache=false;
	private String tag="Replymsg";
	private PullToRefreshListView plv; 
	private ReplyAdapter adapter;
	private TextView replymsg_hint;
	
	private List<ReplyMessage> list;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_replymsg_view);
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.replymsg_probar);
		replymsg_hint=(TextView) findViewById(R.id.replymsg_hint);
		btn.setOnClickListener(this);
		userId=BaseActivity.getSelf().getUserID();
		initCache();
//		initData();
//		initAdapter();
		
		//��ȡ������Ϣ
		new Thread(myMsgRunnable).start();
		
	}
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		cacheManager=CacheManager.getInstance();
		cacheManager.init(getApplicationContext());
		list=getCacheData();
		LogUtil.i("list:"+(list==null));
		if(list!=null&&list.size()>0){
			//���list�Ĵ�С����0������ʾ����
			LogUtil.i("list size="+list.size());
			for(ReplyMessage replyMeesage:list){
				replyMeesage.getContent().setHeadImage(new BitmapDrawable(FileUtil.ByteToBitmap(((byte[])cacheManager.getCache(replyMeesage.getContent().getUserId()).getData()))));
			}
			hasCache=true;
			initAdapter();
		}
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ReplyMessage> getCacheData() {
		CacheData  data =cacheManager.getCache(CacheUtils.REPLY_MSG);
		if(data==null){
			return null;
		}
		return (List<ReplyMessage>) data.getData();
	}
	
	private void initAdapter() {
				plv=(PullToRefreshListView)findViewById(R.id.reply_msg_list_view);
				adapter=new ReplyAdapter(getApplicationContext(), R.layout.reply_msg_listview_item, list); 
				plv.setAdapter(adapter);
		
				//ˢ�£������Ƿ�������Ϣ������ Config.ishaveNewMessage
				plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
						
						//��ȡ������Ϣ
						new Thread(myMsgRunnable).start();
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
						//��ȡ������Ϣ
						new Thread(myMsgRunnable).start();
						
					}
				});
				
				// ����PullRefreshListView�������ʱ�ļ�����ʾ
				plv.getLoadingLayoutProxy(false, true).setPullLabel(
								"��������...");
				plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
								"���ڼ���...");
				plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
								"�ͷż���...");
						// ����PullRefreshListView��������ʱ�ļ�����ʾ
				plv.getLoadingLayoutProxy(true, false).setPullLabel(
								"��������...");
				plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
						        "���ڼ���...");
				plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
								"�ͷż���...");
				
				//Ϊitem��Ӽ���
				plv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ExtendsIntent intent = new ExtendsIntent(ReplyMsg.this,
								BBSPostDetailView.class, String.valueOf(list.get(position - 1)
										.getContent().getEssayId()), null, null, 1);
						startActivity(intent);
					}
				});
				
				pro.setVisibility(View.GONE);
	}
//	private void initData() {
//		list=new ArrayList<ReplyMessage>();
//		list.add(new ReplyMessage("İ���","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
//		list.add(new ReplyMessage("������","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
//	
//	}
	
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		LogUtil.i(json);
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			if(jsonArr.length()>0){//������Ϣ
				ReplyMessageData data=new Gson().fromJson(json, ReplyMessageData.class);
				list=(List<ReplyMessage>)data.getApplyMessage();
				new GetPhotoTask().execute();
				replymsg_hint.setVisibility(View.GONE);
			}else{
				replymsg_hint.setVisibility(View.VISIBLE);
				pro.setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * �첽����ͷ��
	 */
	@SuppressWarnings("unused")
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {

		public GetPhotoTask() {
		}

		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}

		

		protected void onPostExecute(String result) {
			//���滺��
			addCacheData(list);
			LogUtil.i("111111111111");
			//����л�����������ݣ����򴴽�����������
			if(hasCache){
				adapter.notifyDataSetChanged();
			}else{
				initAdapter();
				hasCache=true;
			}
			plv.onRefreshComplete();
		}
	}

	//����ͼƬ
	public void loadPhoto(){
		MyMsgReceivePhoto recPho=new MyMsgReceivePhoto();
		for(ReplyMessage replyMeesage:list){
			replyMeesage.getContent().setHeadImage(recPho.getPhoto(cacheManager, replyMeesage.getContent().getUserId(), replyMeesage.getContent().getAvatar()));
		}
		//����Ϊnull,���ⳤʱ��ռ���ڴ棬����oom�Ĳ�������
		recPho=null;
		LogUtil.i("111111111111^^^^^^^^^^^^^");
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(List<ReplyMessage> data){
		CacheData cacheData=new CacheData(CacheUtils.REPLY_MSG, data);
		cacheManager.addCache(cacheData);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	//���ͻ�ȡ�ҵ�������Ϣ���������
		Runnable myMsgRunnable=new Runnable() {
			
			@Override
			public void run() {
				Log.i(tag, "��ȡ������Ϣ");
				ReplyMsg.con.reqMyMsg(userId,0);	
			}
		};

	
}
