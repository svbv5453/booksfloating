package com.booksfloating.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class MyInfoIntegralRule extends Activity{
	private Button btn_integral_rule_sign = null;
	private Button btn_back = null;
	private long preLongTime = 0;
	private long currentTime = 0;
	private SharePreferenceUtil sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_integral_rule);
		Intent intent = getIntent();
		
		btn_integral_rule_sign = (Button) findViewById(R.id.btn_integral_rule_sign);
		btn_integral_rule_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sp = new SharePreferenceUtil(MyInfoIntegralRule.this, Constants.SAVE_USER);
				if(isNetwrokAvaliable(MyInfoIntegralRule.this)){
					/*Time t = new Time();
					int nowMonth = t.month + 1;
					String nowTime = t.year + "年" + nowMonth + "月" + t.monthDay + "日";
					String lastTime = sp.getQianDao().toString();*/
					if(!sp.getToken().isEmpty()){
						QianDao();
						/*if(sp.getQianDao().isEmpty()){
							sp.setCreditScore(2);
							sp.setQianDao(nowTime);
							Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
							finish();
						}else if(lastTime.equals(nowTime)){
							Toast.makeText(MyInfoIntegralRule.this, "今天您已经签过到，明天再来吧！", Toast.LENGTH_SHORT).show();
							}else{
								sp.setCreditScore(2);
								sp.setQianDao(nowTime);
								Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
								finish();
							}*/
					}else{
						Toast.makeText(MyInfoIntegralRule.this, "您尚未登录！", Toast.LENGTH_SHORT).show();
					}
					/*if(!TextUtils.isEmpty(sp.getAccount())){
						if(preLongTime == 0){
							preLongTime = new Date(System.currentTimeMillis()).getTime();
							sp.setCreditScore(2);
							Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
							System.out.println(preLongTime);
						}else {
							currentTime = new Date(System.currentTimeMillis()).getTime();
							System.out.println("当前时间是" + currentTime);
							if((currentTime - preLongTime)/(1000*360*24) > 24){
								preLongTime = currentTime;
								sp.setCreditScore(2);
								Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(MyInfoIntegralRule.this, "今天您已经签过到，明天再来吧！", Toast.LENGTH_SHORT).show();
							}
							
						}
						
						
					}else{
						Toast.makeText(MyInfoIntegralRule.this, "您尚未登录！", Toast.LENGTH_SHORT).show();
					}*/
					
				}else {
					Toast.makeText(MyInfoIntegralRule.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
				}
				
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
	public void QianDao(){
		String token = sp.getToken();
		String url = HttpUtil.SIGN_IN + "?token=" + token;
		System.out.println(url);
		RequestQueue rq = SingleRequestQueue.getInstance(MyInfoIntegralRule.this);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				System.out.println(response.toString());
				if(parseResponse(response)){
					Toast.makeText(MyInfoIntegralRule.this, "签到成功", Toast.LENGTH_SHORT).show();
					
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(MyInfoIntegralRule.this, "服务器错误，请稍后访问", Toast.LENGTH_SHORT).show();
			}
		});
		rq.add(jsonObjectRequest);
	}
	protected boolean parseResponse(JSONObject response) {
		
		try {
			String	status = response.getString("status");
			if(status.equals("1")){
				return true;
			} else if(status.equals("0")){
				Toast.makeText(MyInfoIntegralRule.this, "签到失败，请稍后访问", Toast.LENGTH_SHORT).show();
			} else if(status.equals("-1")){
				Toast.makeText(MyInfoIntegralRule.this, "登陆已过期，请重新登陆", Toast.LENGTH_SHORT).show();
			} else if(status.equals("-2")){
				Toast.makeText(MyInfoIntegralRule.this, "您今天已经签过到了，明天再来", Toast.LENGTH_SHORT).show();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	return false;
	
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

}
