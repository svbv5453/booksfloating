package com.booksfloating.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xd.booksfloating.R;

public class MyInfoSetResetPassword extends Activity{
	
	private Button btn_confim = null;
	private EditText oldPassowrd = null;
	private EditText newPassword = null;
	private EditText confimPassword = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_set_reset_layout);
		
		//getActionBar().setTitle("重置密码");
		
		btn_confim = (Button) findViewById(R.id.btn_myinfo_set_reset_confirm);
		oldPassowrd = (EditText)findViewById(R.id.et_myinfo_set_reset_oldPassword);
		newPassword = (EditText)findViewById(R.id.et_myinfo_set_reset_newPassword);
		confimPassword = (EditText)findViewById(R.id.et_myinfo_set_reset_confirmPassword);
		btn_confim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String oldPasswrodString = oldPassowrd.getText().toString();
				String newPasswordString = newPassword.getText().toString();
				String confimPasswordString = confimPassword.getText().toString();
				
				
				Toast.makeText(MyInfoSetResetPassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MyInfoSetResetPassword.this, MyInfoSet.class);
				startActivity(intent);
				
				
				
			}

			
		});
	}
	

}
