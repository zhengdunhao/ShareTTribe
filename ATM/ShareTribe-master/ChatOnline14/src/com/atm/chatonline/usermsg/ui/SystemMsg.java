package com.atm.chatonline.usermsg.ui;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter;
import com.atm.chatonline.usermsg.bean.Notification;
import com.atm.chatonline.usermsg.bean.SystemMessageData;
import com.atm.chatonline.usermsg.util.CacheData;
import com.atm.chatonline.usermsg.util.CacheManager;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * �ҵ���Ϣ����ϵͳ֪ͨ����
 */

public class SystemMsg extends BaseActivity implements OnClickListener{

	//private View mView;
	private PullToRefreshListView plv;
	private List<Notification> list;
	private String userId;
	private SystemMsgAdapter adapter;
	private ProgressBar pro;
	private Handler handler;
	private CacheManager cacheManager;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_systemmsg_view);
		initView();
		userId=BaseActivity.getSelf().getUserID();
		
		initCache();
		//initAdapter();
		//��ȡϵͳ��Ϣ
		new Thread(myMsgRunnable).start();
		
	}
	
	private void initView(){
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.systemmsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.systemmsg_hint);
		btn.setOnClickListener(this);
		plv=(PullToRefreshListView)findViewById(R.id.systemmsg_list_view);
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
//			for(Notification applyMeesage:list){
//				applyMeesage.getContent().setHeadImage(new BitmapDrawable(FileUtil.ByteToBitmap(((byte[])cacheManager.getCache(applyMeesage.getContent().getUserId()).getData()))));
//			}
			hasCache=true;
			initAdapter();
		}
	
		
	}
	private void initAdapter() {
		
		adapter=new SystemMsgAdapter(getApplicationContext(), R.layout.usermsg_systemmsg_listview_item, list);
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
				plv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						ExtendsIntent intent = new ExtendsIntent(ApplyMsg.this,
//								BBSPostDetailView.class, list.get(position - 1)
//										.getContent().getEssayId(), null, null, 1);
//						startActivity(intent);
						Intent intent=new Intent(SystemMsg.this,SystemMessageDetail.class);
						Bundle bundle=new Bundle();
						bundle.putSerializable("SystemMessage", list.get(position).getContent());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				
				pro.setVisibility(View.GONE);
	}

	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Notification> getCacheData(){
		CacheData  data =cacheManager.getCache(CacheUtils.SYSTEM_MSG);
		if(data==null){
			return null;
		}
		return (List<Notification>) data.getData();
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(List<Notification> data){
		CacheData cacheData=new CacheData(CacheUtils.SYSTEM_MSG, data);
		cacheManager.addCache(cacheData);
	}
	
	@Override
	public void processMessage(Message msg) {
		
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		
		LogUtil.i(json);
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			plv.onRefreshComplete();//������û������Ϣ����Ҫ����ˢ��
			if(jsonArr.length()>0){//������Ϣ
				SystemMessageData data=new Gson().fromJson(json, SystemMessageData.class);
				list=(List<Notification>)data.getMessage();
				System.out.println(list.size());
				//new GetPhotoTask().execute();
				//initAdapter();
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
				
				applymsg_hint.setVisibility(View.GONE);
			}else{
				applymsg_hint.setVisibility(View.VISIBLE);
				pro.setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	Runnable myMsgRunnable = new Runnable(){

		@Override
		public void run() {
			
			LogUtil.i("��ȡϵͳ��Ϣ");
			ApplyMsg.con.reqMyMsg(userId,2);	
		}
		
	};
	
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

}
