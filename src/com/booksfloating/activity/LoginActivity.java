package com.booksfloating.activity;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录Activity
 * @author liuwenyuan
 *
 */
@SuppressLint("ShowToast")
//OnClickListener是一个接口，是抽象观察者，LoginActivity实现了这个接口，就变成了一个具体的观察者，
//观察的对象就是UI对象（即主题）
public class LoginActivity extends Activity implements OnClickListener{
	private EditText et_username, et_password;
	private Button btn_forget_psd,btn_login_now, btn_goto_register;
	private CheckBox cb_remember_psd;
	//等待的对话框
	private Dialog dialog = null;
	Intent intent = null;
	private String jsonStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		initView();
	}
	
	private void initView()
	{
		et_username = (EditText)findViewById(R.id.et_username);
		et_password = (EditText)findViewById(R.id.et_password);
		btn_forget_psd = (Button)findViewById(R.id.btn_forget_psd);
		btn_login_now = (Button)findViewById(R.id.btn_login_now);
		cb_remember_psd = (CheckBox)findViewById(R.id.cb_remember_psd);
		btn_goto_register = (Button)findViewById(R.id.btn_goto_register);
				
		btn_forget_psd.setOnClickListener(this);
		//设置了登录按钮（具体的主题）设置监听这个操作（抽象主题中提供的操作）
		btn_login_now.setOnClickListener(this);
		cb_remember_psd.setOnClickListener(this);
		btn_goto_register.setOnClickListener(this);
	}

	//观察者中的操作
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_forget_psd:
			intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.btn_login_now:
			loginSubmit();
			break;
		case R.id.cb_remember_psd:
			break;
		case R.id.btn_goto_register:
			intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			//this.finish();
		default:
			break;
		}
	}
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if(jsonStr != null)
				{
					dismissLoadingDialog();
					parseReturnJson(jsonStr);
				}
				else {
					dismissLoadingDialog();
					DialogFactory.AlertDialog(LoginActivity.this, "错误提示", "服务器错误，登录失败！");				
				}
				break;

			default:
				break;
			}			
		}
	};

	private void loginSubmit(){
		final String account = et_username.getText().toString().trim();
		final String password = et_password.getText().toString().trim();
		System.out.println("account: "+account);
		if(account.length()== 0 || password.length() == 0)
		{
			DialogFactory.AlertDialog(LoginActivity.this, "登录提示", "账号或密码不能为空！");
		}
		else{
			}
			showLoadingDialog();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					PostParameter[] parameters = new PostParameter[3];
					parameters[0] = new PostParameter("account", account);
					parameters[1] = new PostParameter("password", password);
					parameters[2] = new PostParameter("login_method", "1");				
					jsonStr = HttpUtil.httpRequest(HttpUtil.USER_LOGIN, parameters, HttpUtil.POST);
					
					handler.sendEmptyMessage(0);
				}
			}).start();			
			
		}
	
	private void parseReturnJson(String jsonString)
	{
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			String status = jsonObj.optString("status");
			String token = jsonObj.optString("token");
			if(status.equals("1"))
			{
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				System.out.println("用户登录成功");
			}
			else if (status.equals("-1")) {
				System.out.println("该账户不存在");
			}
			else if (status.equals("-2")) {
				System.out.println("密码错误");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在验证账号...");
			dialog.show();
		}
		else {
			dialog.dismiss();
		}
	}
	
	private void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
