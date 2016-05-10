package com.booksfloating.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpCallBackListener;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.HttpUtilCheck;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoSet extends Activity implements OnClickListener{

	
	private Button btn_reset_password = null;
	private Button btn_feedback = null;
	private Button btn_version_check = null;
	private Button btn_about = null;
	private Button btn_back = null;
	private final static int SHOW_MESSAGE = 0;
	private final static int SHOW_ERROR = -1;
	
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
		btn_back = (Button)findViewById(R.id.back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(MyInfoRemind.this, MyInfoFragment.class);
				startActivity(intent);*/
				finish();
			}
		});
		
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
			versionChecked(MyInfoSet.this, HttpUtil.VERSION_CHECK);
			
			break;
		case R.id.btn_my_info_set_about :
			Intent aboutIntent = new Intent(MyInfoSet.this, MyInfoSetAbout.class);
			startActivity(aboutIntent);
			break;
		default :
				break;
			
		}
	}
	public void versionChecked(Context context, String url){
		if(isNetwrokAvaliable(context)){
			loadData(context, url);
		}else {
			
			Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	public boolean isNetwrokAvaliable(Context context){
		
		ConnectivityManager connectivityMananger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityMananger == null){
			return false;
		}else {
			NetworkInfo[] networkInfo = connectivityMananger.getAllNetworkInfo();
			if(networkInfo != null){
				for(int i = 0; i < networkInfo.length; i++){
					if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
			
		}
		return false;
		
	}
	public void loadData(final Context context, String url){
		
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				System.out.println(response.toString());
				parseData(response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MyInfoSet.this, "服务器异常，请稍后访问", Toast.LENGTH_SHORT).show();
			}
		});
		requestQueue.add(jsonObjectRequest);
		
	}
	protected void parseData(JSONObject response) {
		// TODO Auto-generated method stub
		if(response != null){
			try {
				
				if(response.getString("status").equals("1")){
					Toast.makeText(MyInfoSet.this, "当前版本为最新版本", Toast.LENGTH_SHORT).show();
				
				}else if(response.getString("status").equals("2")){
					//有新版本，跳转到新版本界面	
					Intent versionCheckIntent = new Intent(MyInfoSet.this, MyInfoSetVersionCheck.class);
					startActivity(versionCheckIntent);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
