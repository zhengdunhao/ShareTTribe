package com.atm.chatonline.usercenter.activity.usercenter;



import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.atm.chatonline.usermsg.ui.IndicatorFragmentActivity;
import com.example.studentsystem01.R;

public class MyBbs extends IndicatorFragmentActivity implements OnClickListener{
//	private View v;
//	private String tag="MyBbs";
//	private int bbsNums = 0;
//	private String response;
	protected static String COOKIE;
//	private PullToRefreshListView plv;
//	private List<BBS> bbsList= new ArrayList<BBS>();
//	private MyBbsAdapter bbsAdapter;
//	private BBSConnectNet bBSConnectNet;
//	private Handler handler;

//	private int count =0;
	
	public static final int PUBLISH_NOTE=0; 
	public static final int COLLECT_NOTE=1; 
	public static final int RELATED_NOTE=2; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.my_bbs_bg_normal);
		//final ProgressBar pro=(ProgressBar)findViewById(R.id.myprogressbar);
		initView();
		setCookie();
//		Log.i(tag, "�ҵ����ӡ�������");
//		initParams();
//		new GetDataTask().execute();
//		handler=new Handler(){
//
//			@Override
//			public void handleMessage(Message msg) {
//				// TODO �Զ����ɵķ������
//				
//				
//				pro.setVisibility(View.GONE);
//				switch(msg.what){
//				case 1:
//					initView();
//					break;
//				case 2:
//					Toast.makeText(getApplicationContext(), "û�������ˣ�����ˢ��Ŷ��", Toast.LENGTH_SHORT).show();
//					break;
//				case 3:
//					Toast.makeText(getApplicationContext(), "�㻹û�з������ӣ��Ͻ�ȥ�����Ӱɣ���", Toast.LENGTH_SHORT).show();
//					break;
//				}
//			
//			}
//			
//		};
	}

	
	
	/**
	 * ��ʼ��������
	 * ��ȡ��Bundle����Ĳ�����
	 * �ò��������ڴ��ݸ�������������Ĳ�����
	 * �ĸ�������id;tip;relativePath
	 */
	public  String setCookie() {
		
		//Log.i(tag,"initParams");
		
		

		//SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		COOKIE = getIntent().getExtras().getString("cookie");
		return COOKIE;
	}
	private void initView(){
		findViewById(R.id.single_chat_back).setOnClickListener(this);
		TextView titleText=(TextView)findViewById(R.id.indicator_title_text);
		titleText.setText("��  ��");
		
		
//		LayoutInflater lf=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		v=lf.inflate(R.layout.my_bbs_main_view, null);
//		Button btnBackOnMainView=(Button) v.findViewById(R.id.btn_back);
//		btnBackOnMainView.setOnClickListener(this);
//		plv=(PullToRefreshListView)v.findViewById(R.id.lv_mybbs);
//		bbsAdapter=new MyBbsAdapter(this,R.layout.my_bbs_view_item,bbsList);
//		plv.setAdapter(bbsAdapter);
//		
//		
//		
//		//Ϊ���б�����������������
//		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//			public void onPullDownToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				bbsNums = 0;
//				new GetDataTask().execute();
//			}
//			public void onPullUpToRefresh(
//					PullToRefreshBase<ListView> refreshView) {
//				new GetDataTask().execute();
//			}
//		});
//		
//		//����PullRefreshListView�������ʱ�ļ�����ʾ
//        plv.getLoadingLayoutProxy(false, true).setPullLabel("���ڼ���...");
//        plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("���ڼ���ing");
//        plv.getLoadingLayoutProxy(false, true).setReleaseLabel("�ɿ������Ҿͼ��أ�(*^__^*) ��������");
//        // ����PullRefreshListView��������ʱ�ļ�����ʾ
//        plv.getLoadingLayoutProxy(true, false).setPullLabel("������������������O(��_��)O����~");
//        plv.getLoadingLayoutProxy(true, false).setRefreshingLabel("�׼�����Ŭ��ˢ��ing���I(^��^)�J");
//        plv.getLoadingLayoutProxy(true, false).setReleaseLabel("�ɿ������Ҿ�ˢ�£�(*^__^*) ��������");
//	
//        plv.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				//����essayid�����ӡ�
//				Log.i(tag, "���ӱ����");
//				ExtendsIntent intent = new ExtendsIntent( MyBbs.this , BBSPostDetailView.class ,
//						bbsList.get(position - 1).getEssayId(), null , null , 1);
//				Log.i(tag, "���ӱ����111");
//				Log.i(tag, bbsList.get(position - 1).getEssayId());
//				startActivity(intent);
//				//Log.i(tag, "queue.size:"+WoliaoBaseActivity.queue.size());
//				Log.i(tag, "���ӱ����2");
//			}
//        	
//		});
//        Log.i(tag,"����initView");
//        //����ǵ�һ�μ������滻view��������ǣ������view
//       /*if(count==0){
//    	   count++;
//    	   setContentView(v);
//       }*/
//        setContentView(v);
	}
	
	
	
//	/**
//	 * �첽��������
//	 */
//	private class GetDataTask extends AsyncTask<Void , Void , String>{
//		
//		
//		public GetDataTask(){
//			Log.i(tag, "+++++++first");
//		}
//		
//		protected String doInBackground(Void... params) {
//		try{
//				//����������ͣס���ȴ�2��
//			Log.i(tag, "+++++++second");
//			//2015.10.9����Ҹ���һ�£�doInBackground�Ǵ����ʱ���⣬���԰�loadData��������������ANR����---֣
//				Thread.sleep(2000);
//				try {
//					Log.i(tag, "+++++++third");
//					loadData();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}catch(InterruptedException e){
//			}
//			return null;
//		}
//	
//		protected void onPostExecute(String result){
//			Log.i(tag, "update bbslistview !");
//			bbsAdapter.notifyDataSetChanged();
//			plv.onRefreshComplete();
//		}
		
		
		

//		/**
//		 * �ӷ������˻�ȡ����
//		 * @return response��json������
//		 */
//		private String getResponseFromNet() {
//			Log.i(tag,"getResponseFromNet");
//			Thread thread = new Thread(new Runnable(){
//				public void run(){
//					bBSConnectNet = new BBSConnectNet("","",bbsNums,"essay_publishedEssay.action",cookie);
//					
//					Log.i(tag,"getResponseFromNet+BBSConnectNet����");
//					response = bBSConnectNet.getResponse();
//					Log.i(tag,"Gson:"+response);
//					Log.i(tag,"bBSConnectNet.getResponse()����");
//				}
//			});
//			thread.start();
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				Log.d(tag,"getResponseFromNet-�̱߳����");
//				e.printStackTrace();
//			}
//			Log.i(tag,"555");
//			return response;
//		}
//		
//		/**
//		 * �첽���µļ�������
//		 * @param page2
//		 * @throws JSONException 
//		 */
//		public void loadData() throws JSONException {
//			
//			Log.d(tag,"loadData");
//			
//			if(bbsNums == 0){
//				//�Ȱ��б����
//				Log.d(tag,"bbsList.clear()");
//				bbsList.clear();
//			}
//			response = getResponseFromNet();
//			Log.d(tag,"loadData+1");
//			
//			Data data = new Gson().fromJson(response,Data.class);
//			
//			Log.d(tag,"loadData+3");
//	
//				addData(data);
//				//��BBSFirst�е�BBS�б���ӵ�BBS bean����
//				for(BBS bbs :bbsList ){
//					Log.i(tag,"img path"+ bbs.getHeadImagePath().toLowerCase());
//				}
//				Log.d(tag,"loadData����");
//				if(count==0){
//				handler.sendEmptyMessage(1);
//				count++;
//				}
//		}
//	}
	
	
//	/**
//	 * ��������
//	 * ��BBSFirst�е�BBS�б���ӵ�BBS bean���У�����ȡͼƬ
//	 * @param dataFromJson : BBSFirst��ʵ����
//	 */
//	private void addData(Data data) {
//
//		Log.d(tag,"addData");
//		
//		//��BBSFirst�е�BBS�б���ӵ�BBS bean����
//		for(BBS bbs :data.getBbs()){
//			if(!bbsList.contains(bbs)){
//				bbsList.add(bbs);
//				bbsNums++;
//			}
//		}
//
//		Log.i(tag, "bbsList.size()"+bbsList.size());
//		int count=1;
//		//��ȡ��Ƭ��
//		for(BBS bbs: bbsList){
//			Log.i(tag, "��"+count+++"����¼");
//			//bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath()).getPhoto());
//			//��ȡ��ǩ���ͱ�ǩ��ɫ
//			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---111");
//			bbs.setLabName0(bbs.getLabName().split("\\*#"));
//			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---222");
//			String[] colors = bbs.getLabColor().split("\\*");
//			int[] color = new int[colors.length];
//			for(int i = 0 ; i < colors.length; i++){
//				color[i] = Color.parseColor(colors[i]);
//			}
//			bbs.setLabColor0(color);
//			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---333");
//		}
//	}



	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.single_chat_back:
			super.onBackPressed();
			break;
			default:
				break;
		}
		
	}

	


	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(PUBLISH_NOTE, getString(R.string.mybbs_publish_note),MyPublishedNote.class));
		tabs.add(new TabInfo(COLLECT_NOTE, getString(R.string.mybbs_collect_note),MyCollectionNote.class));
		tabs.add(new TabInfo(RELATED_NOTE, getString(R.string.mybbs_relate_note),MyRelatedNote.class));
		return PUBLISH_NOTE;
	}



	
	
}
