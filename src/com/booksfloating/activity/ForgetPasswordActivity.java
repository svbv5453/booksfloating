package com.booksfloating.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;

public class ForgetPasswordActivity extends Activity {
	private EditText et_email = null;
	private Button btn_send_email = null;
	private Button btn_back = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		et_email = (EditText) findViewById(R.id.et_email);
		btn_send_email = (Button) findViewById(R.id.btn_send_email);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_send_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String email = et_email.getText().toString().trim();
				if(TextUtils.isEmpty(email)){
					Toast.makeText(ForgetPasswordActivity.this, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
					
					
				}else if(! email.contains("@")){
					Toast.makeText(ForgetPasswordActivity.this, "邮箱地址不正确", Toast.LENGTH_SHORT).show();
				}else{
					sendEmail(ForgetPasswordActivity.this, email);
					Toast.makeText(ForgetPasswordActivity.this, "邮件已发送，请查收邮件", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
				
			}
		});
		
	}

	protected void sendEmail(Context context, String email) {
		// TODO Auto-generated method stub
		PostParameter[] postParameter = new PostParameter[1];
		postParameter[0] = new PostParameter("email", email);
		HttpUtil.httpRequest(HttpUtil.FORGET_PASSWORD, postParameter, HttpUtil.POST);
		
	}

	

}
