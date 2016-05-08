package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.ETC1Util;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.booklsfloating.activity.searchbooks.SearchBooksFragment;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

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
	private Button btn_back = null;
	private CheckBox cb_remember_psd;
	
	//等待的对话框
	private Dialog dialog = null;
	Intent intent = null;
	private String jsonStr;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_main);
		sp = this.getSharedPreferences(Constants.SAVE_USER, Context.MODE_PRIVATE);
		initView();
		cb_remember_psd.setChecked(true);
		if(sp.getBoolean("ISCHECKED", false)){
			cb_remember_psd.setChecked(true);
		}else{
			et_username.setText(sp.getString("username", null));
			et_password.setText(sp.getString("password", null));
		}
	}
	
	private void initView()
	{
		et_username = (EditText)findViewById(R.id.et_username);
		et_password = (EditText)findViewById(R.id.et_password);
		btn_forget_psd = (Button)findViewById(R.id.btn_forget_psd);
		btn_login_now = (Button)findViewById(R.id.btn_login_now);
		cb_remember_psd = (CheckBox)findViewById(R.id.cb_remember_psd);
		btn_goto_register = (Button)findViewById(R.id.btn_goto_register);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
				
		btn_forget_psd.setOnClickListener(this);
		//设置了登录按钮（具体的主题）设置监听这个操作（抽象主题中提供的操作）
		btn_login_now.setOnClickListener(this);
		btn_goto_register.setOnClickListener(this);
		cb_remember_psd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(cb_remember_psd.isChecked()){
					sp.edit().putBoolean("ISCHECKED", true).commit();
				}else{
					sp.edit().putBoolean("ISCHECKED", false).commit();
				}
				
			}
		});
		
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
		
		case R.id.btn_goto_register:
			intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			//this.finish();
		case R.id.back:
			finish();
		default:
			break;
		}
	}
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			dismissLoadingDialog();
			switch (msg.what) {
			case Constants.OK:
				//if(cb_remember_psd.isChecked()){
					//Editor editor = sp.edit();
					//editor.putString("username", et_username.getText().toString());
					//editor.putString("password", et_password.getText().toString());
					//editor.commit();
				//}
				parseReturnJson(jsonStr);
				break;
			case Constants.SERVER_ERROR:
				DialogFactory.AlertDialog(LoginActivity.this, "错误提示", "服务器错误，登录失败！");								
				break;
			case Constants.NULL_ERROR:
				DialogFactory.AlertDialog(LoginActivity.this, "登录提示", "账号或密码不能为空！");
				break;
			case Constants.NETWORK_ERROR:
				Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
			default:
				break;
			}			
		}
	};

	private void loginSubmit(){
		final String account = et_username.getText().toString().trim();
		final String password = et_password.getText().toString().trim();
		final PostParameter[] parameters = new PostParameter[3];
		showLoadingDialog();
		new Thread(new Runnable() {		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(account.length()== 0 || password.length() == 0)
				{					
					handler.sendEmptyMessage(Constants.NULL_ERROR);
				}
				else{
					parameters[0] = new PostParameter("account", account);
					parameters[1] = new PostParameter("password", password);
					parameters[2] = new PostParameter("login_method", "1");				
					jsonStr = HttpUtil.httpRequest(HttpUtil.USER_LOGIN, parameters, HttpUtil.POST);
					if(jsonStr != null)
					{												
						handler.sendEmptyMessage(Constants.OK);
					}
					else {
						handler.sendEmptyMessage(Constants.SERVER_ERROR);
					}
					
				}								
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
				SharePreferenceUtil sp = new SharePreferenceUtil(LoginActivity.this, Constants.SAVE_USER);
				sp.setToken(token);
				
				Constants.isLogin = true;
				
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				System.out.println("用户登录成功");
			}
			else if (status.equals("-1")) {
				DialogFactory.AlertDialog(LoginActivity.this, "提示", "该账户不存在！");
				System.out.println("该账户不存在");
			}
			else if (status.equals("-2")) {
				DialogFactory.AlertDialog(LoginActivity.this, "提示", "密码错误！");
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
