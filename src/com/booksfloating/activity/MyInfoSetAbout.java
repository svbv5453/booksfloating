package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoSetAbout extends Activity{

	private TextView tv_about_us = null;
	private Button btn_back = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myinfo_set_about_layout);
		tv_about_us = (TextView) findViewById(R.id.tv_about_us);
		btn_back = (Button)findViewById(R.id.back);
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intentBack = new Intent(MyInfoPublish.this, MyInfoFragment.class);
				startActivity(intentBack);*/
				finish();
				
			}
		});
		loadData(MyInfoSetAbout.this, HttpUtil.ABOUT_US);
		
		
		//getActionBar().setTitle("关于");
	}
	public boolean isNetAvailable(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
		if(networkInfos.length != 0){
			for(int i = 0; i < networkInfos.length; i++){
				if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public void loadData(final Context context, String url){
		if(isNetAvailable(context)){
			RequestQueue rq = SingleRequestQueue.getInstance(context);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					
					String data = parseResponse(context, response);
					showData(data);
					
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(context, "服务器异常，请稍后连接", Toast.LENGTH_SHORT).show();
					
				}
			});
			rq.add(jsonObjectRequest);
		}else{
			String data = ACache.get(context).getAsString("about_us");
			showData(data);
			//Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			
		}
		
		
	}
	protected void showData(String data) {
		// TODO Auto-generated method stub
		if(data != null){
			tv_about_us.setText(data);
		}
		
		
	}
	protected String parseResponse(Context context, JSONObject response) {
		String message = null;
		try {
			if(response.getString("status").equals("1")){
				message = response.getString("message");
				ACache.get(context).put("about_us", message);
				
			}
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
		
	}
	
}
