package com.booksfloating.activity;

import com.xd.booksfloating.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author liuwenyuan
 *
 */
public class LoginActivity extends Activity implements OnClickListener{
	private EditText et_username, et_password;
	private Button btn_forget_psd,btn_remember_psd, btn_login_now, btn_goto_register;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
	}
	
	private void initView()
	{
		et_username = (EditText)findViewById(R.id.et_username);
		et_password = (EditText)findViewById(R.id.et_password);
		btn_forget_psd = (Button)findViewById(R.id.btn_forget_psd);
		btn_login_now = (Button)findViewById(R.id.btn_login_now);
		btn_remember_psd = (Button)findViewById(R.id.btn_remember_psd);
		btn_goto_register = (Button)findViewById(R.id.btn_goto_register);
		
		btn_forget_psd.setOnClickListener(this);
		btn_login_now.setOnClickListener(this);
		btn_remember_psd.setOnClickListener(this);
		btn_goto_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_forget_psd:
			intent = new Intent();
			intent.setClass(LoginActivity.this, ForgetPasswordActivity.class);
			break;
		case R.id.btn_login_now:
			break;
		case R.id.btn_remember_psd:
			break;
		case R.id.btn_goto_register:
			intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
		default:
			break;
		}
	}
}
