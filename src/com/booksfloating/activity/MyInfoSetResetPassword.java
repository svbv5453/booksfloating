package com.booksfloating.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;

public class MyInfoSetResetPassword extends Activity{
	
	private Button btn_confim = null;
	private EditText oldPassowrd = null;
	private EditText newPassword = null;
	private EditText confimPassword = null;
	private Button btn_back = null;
	public static final int OK = 0,SERVER_ERROR = -1, NETWORK_ERROR = -2, NULL_ERROR = -3,OLDPASSWORD_ERROR = -4,PASSWORD_ERROR = -5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_set_reset_layout);
		
		//getActionBar().setTitle("重置密码");
		
		btn_confim = (Button) findViewById(R.id.btn_myinfo_set_reset_confirm);
		oldPassowrd = (EditText)findViewById(R.id.et_myinfo_set_reset_oldPassword);
		newPassword = (EditText)findViewById(R.id.et_myinfo_set_reset_newPassword);
		confimPassword = (EditText)findViewById(R.id.et_myinfo_set_reset_confirmPassword);
		btn_back = (Button)findViewById(R.id.back);
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intentBack = new Intent(MyInfoPublish.this, MyInfoFragment.class);
				startActivity(intentBack);*/
				finish();
				
			}
		});
		btn_confim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changePassword();
				
				
			}

			
		});
	}
	public Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch(msg.what){
			case OK:
				Toast.makeText(MyInfoSetResetPassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MyInfoSetResetPassword.this, MyInfoSet.class);
				startActivity(intent);
				break;
			case OLDPASSWORD_ERROR:
				Toast.makeText(MyInfoSetResetPassword.this, "原密码错误，请重写输入！", Toast.LENGTH_SHORT).show();
				break;
			case NULL_ERROR:
				Toast.makeText(MyInfoSetResetPassword.this, "请输入密码！", Toast.LENGTH_SHORT).show();
			case SERVER_ERROR:
				Toast.makeText(MyInfoSetResetPassword.this, "服务器出错，请稍后访问！", Toast.LENGTH_SHORT).show();
				break;
			case PASSWORD_ERROR:
				Toast.makeText(MyInfoSetResetPassword.this, "密码输入不一致，请核对后输入！", Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_ERROR:
				Toast.makeText(MyInfoSetResetPassword.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
				break;
				
			}
		};
	};
	public void changePassword(){
		final String oldPasswrodString = oldPassowrd.getText().toString().trim();
		final String newPasswordString = newPassword.getText().toString().trim();
		final String confimPasswordString = confimPassword.getText().toString().trim();
		final PostParameter[] parameters = new PostParameter[3];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(oldPasswrodString.length()== 0 || newPasswordString.length() == 0)
				{					
					handler.sendEmptyMessage(NULL_ERROR);
				}else if(!newPasswordString.equals(confimPasswordString)){
					handler.sendEmptyMessage(PASSWORD_ERROR);
				}else{
					parameters[0] = new PostParameter("token", "2605");
					parameters[1] = new PostParameter("old_password", oldPasswrodString);
					parameters[2] = new PostParameter("new_password", confimPasswordString);
					String responseJson = HttpUtil.httpRequest(HttpUtil.CHANGE_PASSWORD, parameters, HttpUtil.POST);
					if(responseJson != null){
						if(parseResponseJson(responseJson)){
							handler.sendEmptyMessage(OK);
						}else{
							handler.sendEmptyMessage(OLDPASSWORD_ERROR);
						}
						
						
					}else{
						handler.sendEmptyMessage(SERVER_ERROR);
					}
				}
				
				
			}
		}).start();
	}
	public boolean parseResponseJson(String responseJson){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(responseJson);
			if(jsonObject.getString("status").equals("1")){
				
				return true;
			}else{
				
				return false;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
		
	}
	

}
