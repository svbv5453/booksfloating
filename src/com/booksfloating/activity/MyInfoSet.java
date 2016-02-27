package com.booksfloating.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xd.booksfloating.R;

public class MyInfoSet extends Activity implements OnClickListener{

	
	private Button btn_reset_password = null;
	private Button btn_feedback = null;
	private Button btn_version_check = null;
	private Button btn_about = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_set_layout);
		//getActionBar().setTitle("设置");
		
		
		btn_reset_password = (Button) findViewById(R.id.btn_my_info_set_reset_password);
		btn_feedback = (Button) findViewById(R.id.btn_my_info_set_feedback);
		btn_version_check = (Button) findViewById(R.id.btn_my_info_set_version_check);
		btn_about = (Button) findViewById(R.id.btn_my_info_set_about);
		
		btn_reset_password.setOnClickListener(this);
		btn_feedback.setOnClickListener(this);
		btn_version_check.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_my_info_set_reset_password :
			Intent resetIntent = new Intent(MyInfoSet.this, MyInfoSetResetPassword.class);
			startActivity(resetIntent);
			break;
		case R.id.btn_my_info_set_feedback :
			Intent feedbackIntent = new Intent(MyInfoSet.this, MyInfoSetFeedback.class);
			startActivity(feedbackIntent);
			break;
		case R.id.btn_my_info_set_version_check :
			Intent versionCheckIntent = new Intent(MyInfoSet.this, MyInfoSetVersionCheck.class);
			startActivity(versionCheckIntent);
			break;
		case R.id.btn_my_info_set_about :
			Intent aboutIntent = new Intent(MyInfoSet.this, MyInfoSetAbout.class);
			startActivity(aboutIntent);
			break;
		default :
				break;
			
		}
	}
}
