package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;

public class MyInfoSetFeedback extends Activity{
	private EditText et_feedback = null;
	private Button btn_feedback = null;
	private static final int OK = 0,SERVER_ERROR = -1, NETWORK_ERROR = -2, NULL_ERROR = -3,OLDPASSWORD_ERROR = -4,PASSWORD_ERROR = -5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myinfo_set_feedback_layout);
		
		//getActionBar().setTitle("反馈问题");
		
		et_feedback = (EditText) findViewById(R.id.et_myinfo_set_feedback);
		btn_feedback = (Button) findViewById(R.id.btn_myinfo_set_feedback);
		btn_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitFeeback();
				
				
				
			}
		});
		
	}
public Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch(msg.what){
			case OK:
				Toast.makeText(MyInfoSetFeedback.this, "感谢您对我们的大力支持！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MyInfoSetFeedback.this, MyInfoSet.class);
				startActivity(intent);
				break;
			
			case NULL_ERROR:
				Toast.makeText(MyInfoSetFeedback.this, "请输入反馈信息！", Toast.LENGTH_SHORT).show();
			case SERVER_ERROR:
				Toast.makeText(MyInfoSetFeedback.this, "服务器出错，请稍后访问！", Toast.LENGTH_SHORT).show();
				break;
			
			case NETWORK_ERROR:
				Toast.makeText(MyInfoSetFeedback.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
				break;
				
			}
		};
	};
	public void submitFeeback(){
		final String feedback = et_feedback.getText().toString().trim();
		final PostParameter[] postParams = new PostParameter[1];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(feedback.length() == 0){
					handler.sendEmptyMessage(NULL_ERROR);
				}else{
					postParams[0] = new PostParameter("feedback", feedback);
					String responseJson = HttpUtil.httpRequest(HttpUtil.FEEDBACK, postParams, HttpUtil.POST);
					if(responseJson != null){
						if(parseResponseJson(responseJson)){
							handler.sendEmptyMessage(OK);
						}else{
							handler.sendEmptyMessage(OK);
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
