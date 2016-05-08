package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
		
	}

	protected String sendEmail(Context context, String email) {
		// TODO Auto-generated method stub
		PostParameter[] postParameter = new PostParameter[2];
		SharePreferenceUtil sp = new SharePreferenceUtil(context, Constants.SAVE_USER);
		postParameter[0] = new PostParameter("email", email);
		postParameter[1] = new PostParameter("username", sp.getAccount());
		String jsonString = HttpUtil.httpRequest(HttpUtil.FORGET_PASSWORD, postParameter, HttpUtil.POST);
		return jsonString;
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
				String jsonString = sendEmail(ForgetPasswordActivity.this, email);
				if (jsonString != null) {
					try {
						JSONObject object = new JSONObject(jsonString);
						String status = object.optString("status");
						if (status.equals("1")) {
							DialogFactory.AlertDialog(ForgetPasswordActivity.this, "提示", "邮件已发送成功，请到您的邮箱中获取验证码！");
							ll_verify.setVisibility(View.VISIBLE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				else {
					Toast.makeText(ForgetPasswordActivity.this, "邮件发送失败！", Toast.LENGTH_SHORT).show();
				}
			}
			break;
			
		case R.id.btn_sure:
			String jsonString = sendVerifyCode();
			if (jsonString != null) {
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
			}
			else {
				Toast.makeText(ForgetPasswordActivity.this, "服务器错误，请重试！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	private String sendVerifyCode()
	{
		String json = null;
		if (et_verify.getText().toString().trim().length() > 0) {
			PostParameter[] postParameter = new PostParameter[2];
			SharePreferenceUtil sp = new SharePreferenceUtil(ForgetPasswordActivity.this, Constants.SAVE_USER);
			postParameter[0] = new PostParameter("cookie",et_verify.getText().toString().trim());
			postParameter[1] = new PostParameter("username", sp.getAccount());
			json = HttpUtil.httpRequest(HttpUtil.VERIFY_COOKIE, postParameter, HttpUtil.POST);
		}
		return json;
	}
	

}
