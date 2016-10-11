package com.atm.charonline.recuit.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.activity.bbs.BBSClickGoodListView;
import com.atm.chatonline.bbs.activity.bbs.BBSCommentView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.example.studentsystem01.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @���� ����������ʾ��������
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 * */

public class RecuitPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private LinearLayout ll_clickGood, ll_comment, ll_share, ll_report;
	private ImageView iv_clickGood, iv_comment, iv_share, iv_report, iv_return,
			iv_collect;
	private TextView tv_clickGood, tv_comment, tv_share, tv_report;
	private boolean flag = true;
	private String essayId = "", response;
	private String relativePath = "essay_content.action";
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "recuit/";
	public static final int IS_CLICK = 1, IS_NOT_CLICK = 2;
	private Handler handler;
	private int replyNum;
	private static String cookie;
	
	private SharePopupWindow pop;//���һ��Popwindow������һ����������,���Բ��ֱ��� author �� 
    private OnClickListener listener;
    private LinearLayout parentLayout;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_post_detail);
		
		//����BBSMainView�������һƪ����ʱ���ᴫ����һ��essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");
		
		//��ʼ���������󣬼����û��Ĳ���
		initLintener();
		initView();
		initEvent();
				
		//��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		jsonDemo();//�����߳����������ȡ����
		
		
		// �첽��Ϣ�������
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case IS_CLICK:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("ȡ����");
					flag = false;
					break;
				case IS_NOT_CLICK:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("����");
					flag = true;
					break;
				}
				tv_comment.setText("����(" + replyNum + ")");
			}
		};
		webViewLoadUrl();//WebView������ҳ
		

	}
	
	//���������ж��û����������������|����Ȧ�� 2016.4.13 ��
	private void initLintener() {
		listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap thumb=BitmapFactory.decodeResource(RecuitPostDetailView.this.getResources(), R.drawable.pic_software_small);
				WebPageShare webpage=new WebPageShare(RecuitPostDetailView.this, url + essayId + ".html", "��Ƹ��Ϣ", "������������ҵ�ʦ��ʦ���Ƽ�����Ƹ��Ϣ������Խ��������Ƿ�����ϲ���Ĺ�����λ��ʵϰ��λ", thumb);
				if(!webpage.isInstalled()){
					Toast.makeText(RecuitPostDetailView.this.getApplicationContext(), "����û�а�װ΢�ſͻ��ˣ�", Toast.LENGTH_SHORT).show();
					return;
				}
				switch(v.getId()){
				//������������ͼ��
				case R.id.ly_share_friend:
					webpage.shareToWx(0);
					break;
					//����˷�������Ȧͼ��
				case R.id.ly_share_timeline:
					webpage.shareToWx(1);
					break;
				}
				//ÿ�η�����Ϲرյ�������
				pop.dismiss();
			}
		};
	}

	//WebView������ҳ
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		
		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		//������ҳ�е����������ʱ�������ø�webView������
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// �������Ҫ�����Ե�������¼��Ĵ�����true�����򷵻�false
				return true;
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		webView.addJavascriptInterface(new TelRecuitJSInterface(), "recuitView");
		synCookies(this, url + essayId + ".html");//ͬ��cookie
		webView.loadUrl(url + essayId + ".html");
	}
	
	/** 
	 * ��Ϊ�ͻ��˺���ҳ��cookie�ǲ�һ���ģ���������Ҫͬ��һ��cookie 
	 */  
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//�Ƴ�  
	    cookieManager.setCookie(url, cookie);
	    CookieSyncManager.getInstance().sync();  
	}  

	//�����߳����������ȡ����
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				httpClientGet = new BBSConnectNet(essayId, relativePath, cookie);
				response = httpClientGet.getResponse();				
				parseJSONWithJSONObject(response);//�����ȡ��������
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();

	}

	//�����ȡ��������
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			boolean clickGood = json.getBoolean("clickGood");
			boolean collect = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (clickGood == true) {
				message.what = IS_CLICK;
			} else {
				message.what = IS_NOT_CLICK;
			}
			handler.sendMessage(message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��ʼ���ؼ�
	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webView);
		ll_clickGood = (LinearLayout) findViewById(R.id.ll_clickGood);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		ll_share = (LinearLayout) findViewById(R.id.ll_share);
		ll_report = (LinearLayout) findViewById(R.id.ll_report);
		iv_clickGood = (ImageView) findViewById(R.id.iv_clickGood);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_report = (ImageView) findViewById(R.id.iv_report);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		tv_clickGood = (TextView) findViewById(R.id.tv_clickGood);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		tv_share = (TextView) findViewById(R.id.tv_share);
		tv_report = (TextView) findViewById(R.id.tv_report);
		parentLayout=(LinearLayout) findViewById(R.id.parent_layout);
	}

	// ���ü�����
	private void initEvent() {
		// TODO Auto-generated method stub
		ll_clickGood.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_report.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_clickGood:
			if (flag == true) {
				iv_clickGood.setImageResource(R.drawable.clickgood_green);
				tv_clickGood.setTextColor(0xff33cc66);
				tv_clickGood.setText("ȡ����");
				flag = false;
			} else {
				iv_clickGood.setImageResource(R.drawable.clickgood);
				tv_clickGood.setTextColor(0xff666666);
				tv_clickGood.setText("����");
				flag = true;
			}
			break;
		case R.id.ll_comment:
			Intent intent_comment = new Intent(this, BBSCommentView.class);
			Bundle bundle = new Bundle();
			bundle.putString("essayId", essayId);
			intent_comment.putExtras(bundle);
			startActivity(intent_comment);
			break;
		case R.id.ll_share:
			//���һ���������ڣ�author �� 2016.4.12
			if(pop==null){
			pop=new SharePopupWindow(this, 0.5f, listener);
			}
			pop.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.ll_report:
			Intent intent_report = new Intent(this, RecuitReportView.class);
			startActivity(intent_report);
			break;
		case R.id.iv_return:
			RecuitPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			Toast.makeText(RecuitPostDetailView.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	//Javascript���ð�׿�������ڲ���
	class DemoJavaScriptInterface{
	// ��Ϊ��ȫ���⣬��Android4.2��(���Ӧ�õ�android:targetSdkVersion��ֵΪ17+)
				// JSֻ�ܷ��ʴ��� @JavascriptInterfaceע���Java������
				// ��������Ŀ����汾�Ƚϸߣ���Ҫ�ڱ����õĺ���ǰ����@JavascriptInterfaceע�⡣
				@JavascriptInterface
				public void goClickGoodView(String url) {

					Intent intent = new Intent(RecuitPostDetailView.this, BBSClickGoodListView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					RecuitPostDetailView.this.startActivity(intent);
				}
				@JavascriptInterface
				public void finishPostDetailView(){
					RecuitPostDetailView.this.finish();
				}
	}
	/**
	 * js����native �ľ����recuitView  ����ʱtel
	 * @author Jackbing
	 *
	 */
	class TelRecuitJSInterface{
		//type���������� һ����sms��һ����tel
		public void tel(String type,String number){
			
			if(type.equals("sms")){
				//������
				sendSmsOnlyNumber(number);
			}else{
				//��绰
				dialPhoneNumber(number);
			}
		}
		
	}
	
	/**
	 * ����ϵͳ���Ͷ��ŵĽ���,ָ��Ŀ�귢����
	 * @param content
	 */

	public void sendSmsOnlyNumber(String number){
		Uri smsUri=Uri.parse("smsto:"+number);
		Intent intent=new Intent(Intent.ACTION_SENDTO,smsUri);
		startActivity(intent);
	}
	
	/**
	 * ����ϵͳ��绰�Ľ��棬ָ��Ŀ��绰����
	 * @param number
	 */
	public void dialPhoneNumber(String number){
		Uri telUri=Uri.parse("tel:"+number);
		Intent intent=new Intent(Intent.ACTION_VIEW,telUri);
		startActivity(intent);
	}
	

	@Override
	public void processMessage(Message msg) {
		
		
	}

}
