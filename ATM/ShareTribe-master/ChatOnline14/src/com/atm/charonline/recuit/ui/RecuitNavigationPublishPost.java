/**
 * 
 */
package com.atm.charonline.recuit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.bbs.util.LogUtil;
import com.atm.charonline.bbs.util.SendDataToServer;
import com.atm.chatonline.bbs.activity.bbs.BBSPublishPostView;
import com.atm.chatonline.bbs.commom.ToastUtil;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---NavigationPublishPost
 * @���� BBS�������ķ�������
 * @���� ��С��
 * @ʱ�� 2015-8-18
 * 
 */
public class RecuitNavigationPublishPost extends BaseActivity implements
OnClickListener{
	private String work,type,worktype,location,phone,salary,info,response,getResponse,cookie;
	private Spinner spWork,spType;
	private EditText edWorkType,edLocation,edPhone,edInfo,edSalary;
	private TextView publish;
	private Button btnBack;
	private SendDataToServer send = new SendDataToServer();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_publish_post);
		
		//��ʼ���ؼ�
		spWork = (Spinner) findViewById(R.id.sp_work);
		spType = (Spinner) findViewById(R.id.sp_type);
		edWorkType = (EditText) findViewById(R.id.txt_worktype);
		edLocation = (EditText) findViewById(R.id.txt_location);
		edPhone = (EditText) findViewById(R.id.txt_phone);
		edSalary = (EditText) findViewById(R.id.txt_salary);
		edInfo = (EditText) findViewById(R.id.txt_info);
		publish = (TextView) findViewById(R.id.btn_publsh);
		btnBack = (Button) findViewById(R.id.btn_back);
		
		//���ü���
		btnBack.setOnClickListener(this);
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(edWorkType.getText().toString().equals("")) {
					LogUtil.d("��������Ϊ��");
					new ToastUtil().show(getApplication(), "�������Ͳ���Ϊ��");	
				}else {
					worktype = edWorkType.getText().toString();
					LogUtil.d("��������Ϊ" + worktype);
					if(edSalary.getText().toString().equals("")) {
						LogUtil.d("н��Ϊ��");
						new ToastUtil().show(getApplication(), "н�ʲ���Ϊ��");	
					}else {
						salary = edSalary.getText().toString();
						LogUtil.p("н��",salary);
					if(edPhone.getText().toString().equals("")) {
						LogUtil.d("��ϵ��ʽΪ��");
						new ToastUtil().show(getApplication(), "��ϵ��ʽ����Ϊ��");	
					}else {
						phone = edPhone.getText().toString();
						LogUtil.p("��ϵ�绰",phone);
						if(edLocation.getText().toString().equals("")) {
							LogUtil.d("��ַΪ��");
							new ToastUtil().show(getApplication(), "��ַ����Ϊ��");	
						}else {
					location = edLocation.getText().toString();
					LogUtil.p("��ַ",location);
						if(edInfo.getText().toString().equals("")) {
							LogUtil.d("���Ϊ��");
							new ToastUtil().show(getApplication(), "��鲻��Ϊ��");	
						}else {
							info = edInfo.getText().toString();
							LogUtil.p("���",info);
							work = spWork.getSelectedItem().toString();
							LogUtil.p("��Ƹ����ְ",work);
							type = spType.getSelectedItem().toString();
							LogUtil.p("����",type);
							sendDataToServer();// �����ݴ���������
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}// �����߳�˯��1�룬�ȴ�����response

							LogUtil.d( "response:"+response);
							try{
								if (response.equals("success")) {
									Toast.makeText(RecuitNavigationPublishPost.this, "�����ɹ�",
											Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(RecuitNavigationPublishPost.this, response,
											Toast.LENGTH_SHORT).show();
								}
							}catch(NullPointerException e){
								return ;
							}
						}}}
					}
				}
			}
			
		});
	}
	private void sendDataToServer() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			String getResponse;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ������������
				Map<Object, Object> params = new HashMap<Object, Object>();
				params.put("work", work);
				params.put("reTypeName", type);
				params.put("woTypeName", worktype);
				params.put("salary", salary);
				params.put("telephone", phone);
				params.put("workAddress", location);
				params.put("reContent", info);		
				// ��ȡcookie
				SharedPreferences pref = getSharedPreferences("data",
						Context.MODE_PRIVATE);
				cookie = pref.getString("cookie", "");
				params.put("cookie",cookie);		

				getResponse = send.post(UriAPI.SUB_URL + "recuit_publish.action",
							params, null, cookie);
			
				try {
					JSONObject object = new JSONObject(getResponse);
					response = object.getString("tip");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder back = new AlertDialog.Builder(this);
		back.setTitle("��ʾ��")
				.setMessage("�˳���ǰ�༭��")
				.setPositiveButton("�˳�",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								RecuitNavigationPublishPost.this.finish();
							}

						})
				.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0,
									int arg1) {
								// TODO Auto-generated method stub
							}
						});

		back.create().show();
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("��ʾ��")
					.setMessage("�˳���ǰ�༭��")
					.setPositiveButton("�˳�",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									RecuitNavigationPublishPost.this.finish();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
		}
	}
	/* (non-Javadoc)
	 * @see com.atm.chatonline.chat.ui.BaseActivity#processMessage(android.os.Message)
	 */
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}
}
