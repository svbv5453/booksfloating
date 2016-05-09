package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

public class ForgetPasswordActivity extends Activity implements OnClickListener{
	private EditText et_email = null;
	private Button btn_send_email = null;
	private Button btn_back = null;
	private EditText et_verify;
	private Button btn_sure;
	private LinearLayout ll_verify;
	private static final int VERIFY = 1,SEND_EMAIL = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		et_email = (EditText) findViewById(R.id.et_email);		
		
		btn_back = (Button) findViewById(R.id.btn_back);				
		btn_back.setOnClickListener(this);
		
		btn_send_email = (Button) findViewById(R.id.btn_send_email);
		btn_send_email.setOnClickListener(this);
		
		btn_sure = (Button)findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		
		et_verify = (EditText)findViewById(R.id.et_verify);
		
	}	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.btn_send_email:
			String email = et_email.getText().toString().trim();
			if(TextUtils.isEmpty(email)){
				Toast.makeText(ForgetPasswordActivity.this, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();								
			}else if(! email.contains("@")){
				Toast.makeText(ForgetPasswordActivity.this, "邮箱地址不正确", Toast.LENGTH_SHORT).show();
			}else{
				sendEmail(email);									
			}
			break;
			
		case R.id.btn_sure:			
			sendVerifyCode();										
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case VERIFY:
				String jsonString = (String) msg.obj;
				try {
					JSONObject object = new JSONObject(jsonString);
					String status = object.optString("status");
					if (status.equals("1")) {
						String password = object.optString("password");
						DialogFactory.AlertDialog(ForgetPasswordActivity.this, "重要提示", "请牢记你的密码: "+password);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				break;
				
			case SEND_EMAIL:
				String json = (String) msg.obj;
				try {
					JSONObject object = new JSONObject(json);
					String status = object.optString("status");
					if (status.equals("1")) {
						DialogFactory.AlertDialog(ForgetPasswordActivity.this, "提示", "邮件已发送成功，请到您的邮箱中获取验证码！");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case Constants.SERVER_ERROR:
				Toast.makeText(ForgetPasswordActivity.this, "验证失败，请重试！", Toast.LENGTH_SHORT).show();
				break;
			case Constants.NULL_ERROR:
				Toast.makeText(ForgetPasswordActivity.this, "邮件发送失败，请重试！", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};
	
	private void sendEmail(final String email) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PostParameter[] postParameter = new PostParameter[2];
				SharePreferenceUtil sp = new SharePreferenceUtil(ForgetPasswordActivity.this, Constants.SAVE_USER);
				postParameter[0] = new PostParameter("email", email);
				postParameter[1] = new PostParameter("username", sp.getAccount());
				String jsonString = HttpUtil.httpRequest(HttpUtil.FORGET_PASSWORD, postParameter, HttpUtil.POST);
				if (jsonString == null) {
					handler.sendEmptyMessage(Constants.NULL_ERROR);
				}else {
					Message msg = new Message();
					msg.obj = jsonString;
					msg.what = SEND_EMAIL;
					handler.sendMessage(msg);
				}
			}
		}).start();

	}
	
	private void sendVerifyCode()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String json = null;
				if (et_verify.getText().toString().trim().length() > 0) {
					PostParameter[] postParameter = new PostParameter[2];
					SharePreferenceUtil sp = new SharePreferenceUtil(ForgetPasswordActivity.this, Constants.SAVE_USER);					
					postParameter[0] = new PostParameter("username", sp.getAccount());
					postParameter[1] = new PostParameter("code",et_verify.getText().toString().trim());
					json = HttpUtil.httpRequest(HttpUtil.GET_PASSWORD, postParameter, HttpUtil.POST);
					if (json == null) {
						handler.sendEmptyMessage(Constants.SERVER_ERROR);
					}else {
						Message msg = new Message();
						msg.obj = json;
						msg.what = VERIFY;
						handler.sendMessage(msg);
					}
				}
			}
		}).start();
		
	}	
}
