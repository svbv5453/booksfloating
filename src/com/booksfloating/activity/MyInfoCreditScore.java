package com.booksfloating.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoCreditScore extends Activity{
	
	private Button btn_integral_rule = null;
	private Button btn_back = null;
	private ProgressBar progressBar1 = null;
	private ProgressBar progressBar2 = null;
	private TextView tv_currentScore = null;	
	private SharePreferenceUtil sp;
	private String credit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_layout);
		btn_integral_rule = (Button) findViewById(R.id.btn_myinfo_integral_rule);
		progressBar1 = (ProgressBar) findViewById(R.id.pb_myinfo_crditscore);
		progressBar2 = (ProgressBar) findViewById(R.id.pb_myinfo_crditscore2);
		tv_currentScore = (TextView) findViewById(R.id.current_score);
		
		getCreditScore();
		
		btn_integral_rule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyInfoCreditScore.this, MyInfoIntegralRule.class);
				startActivity(intent);
				
			}
		});
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("MyInfoCreditScore", "-----onResume()");
		getCreditScore();
	}
	public void getCreditScore(){
		sp = new SharePreferenceUtil(MyInfoCreditScore.this, Constants.SAVE_USER);
		if(!sp.getToken().isEmpty()){
			if(isNetwrokAvaliable(MyInfoCreditScore.this)){
				getCreditScoreFromServe();
			}else {
				Toast.makeText(MyInfoCreditScore.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
				tv_currentScore.setText(sp.getCreditScroe() + "");
				if(sp.getCreditScroe()/5 <= 100){
					progressBar1.setProgress(sp.getCreditScroe()/5);
				}else{
					progressBar1.setProgress(100);
					progressBar2.setProgress((sp.getCreditScroe() - 100*5)/5);
				}
			}
			
		}else {
			tv_currentScore.setText("您尚未登陆，无法查看");
		}
		
		
		/*if(sp.getCreditScroe() != 0){
			tv_currentScore.setText(sp.getCreditScroe() + "");
			if(sp.getCreditScroe() <= 100){
				progressBar1.setProgress(sp.getCreditScroe());
			}else{
				progressBar1.setProgress(100);
				progressBar2.setProgress(sp.getCreditScroe() - 100);
			}
			
			
		}else if(!TextUtils.isEmpty(sp.getToken())){
			sp.setCreditScore(5);
			tv_currentScore.setText("5");
			progressBar1.setProgress(5);
		}else{
			tv_currentScore.setText("0");
			progressBar1.setProgress(0);
		}*/
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
	public void getCreditScoreFromServe(){
		String token = sp.getToken();
		String url = HttpUtil.MY_CREDIT + "?token=" + token;
		System.out.println(url);
		RequestQueue rq = SingleRequestQueue.getInstance(MyInfoCreditScore.this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				if(parseResponse(response)){
					tv_currentScore.setText(sp.getCreditScroe() + "");
					if(sp.getCreditScroe()/5 <= 100){
						progressBar1.setProgress(sp.getCreditScroe()/5);
					}else{
						progressBar1.setProgress(100);
						progressBar2.setProgress((sp.getCreditScroe() - 100*5)/5);
					}
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(MyInfoCreditScore.this, "服务器错误，请稍后访问", Toast.LENGTH_SHORT).show();
			}
		});
		rq.add(jsonObjectRequest);
	}
	protected boolean parseResponse(JSONObject response) {
		
		try {
			String	status = response.getString("status");
			if(status.equals("1")){
				/**
				 * 设置积分
				 */
				credit = response.getString("credit");
				sp.setCreditScore(Integer.parseInt(credit));
				
				return true;
			} else if(status.equals("0")){
				Toast.makeText(MyInfoCreditScore.this, "查询失败，请稍后访问", Toast.LENGTH_SHORT).show();
			} else if(status.equals("-1")){
				Toast.makeText(MyInfoCreditScore.this, "登陆已过期，请重新登陆", Toast.LENGTH_SHORT).show();
			} 
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	return false;
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		
			default:
				return super.onOptionsItemSelected(item);
		}
		
		
	}
}
