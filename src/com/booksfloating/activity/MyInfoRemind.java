package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.adapter.MyInfoRemindAdapter;
import com.booksfloating.domain.MyInfoBookDetailBean;
import com.booksfloating.domain.MyInfoPublishBookBean;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoRemind extends Activity{
	private Button btn_back = null;
	private ListView myinfo_listview = null;

	private List<MyInfoPublishBookBean> booksOrderList;
	private MyInfoPublishBookBean bookOrder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_remind_layout);
		//getActionBar().setTitle("到期提醒");
		
		//是从本地订单中获取还是请求服务器；
		btn_back = (Button)findViewById(R.id.back);
		myinfo_listview = (ListView) findViewById(R.id.lv_myinfo_remind);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(MyInfoRemind.this, MyInfoFragment.class);
				startActivity(intent);*/
				finish();
			}
		});
		SharePreferenceUtil sp = new SharePreferenceUtil(MyInfoRemind.this, Constants.SAVE_USER);
		String url = HttpUtil.BORROW_ORDER + "?token=" + sp.getToken();
		
		if(!sp.getToken().isEmpty()){
			loadData(MyInfoRemind.this, url);
		}else{
			Toast.makeText(MyInfoRemind.this, "你尚未登录，无法查看您的信息", Toast.LENGTH_SHORT).show();
		}
	}
	public void loadData(Context context, String url){
		if(isNetwrokAvaliable(context)){
			loadListData(context, url);
		}else {
			
			JSONObject response = ACache.get(context).getAsJSONObject("到期提醒");
			if(response != null){
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
				showListData(context, response);
			}
			Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			
			
		}
	}
	public void loadListData(final Context context, String url){
		
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				System.out.println(response.toString());
				ACache.get(context).put("到期提醒", response);
				showListData(context, response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
		requestQueue.add(jsonObjectRequest);
		
	}
	public void showListData(Context context, JSONObject response){
		parseJsonData(response);
		MyInfoRemindAdapter adapter = new MyInfoRemindAdapter(MyInfoRemind.this, booksOrderList);
		myinfo_listview.setAdapter(adapter);
		
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


	public List<MyInfoPublishBookBean> parseJsonData(JSONObject jsonObject) {
		
		booksOrderList = new ArrayList<MyInfoPublishBookBean>();
		try {
			
			
			if(jsonObject.getString("status").equals("1")){
				
				//预留解析
				
				JSONArray jsonArray = jsonObject.getJSONArray("message");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					bookOrder = new MyInfoPublishBookBean();
					
					bookOrder.bookName = jsonObject.getString("book");
					bookOrder.bookAuthor = jsonObject.getString("author");
					bookOrder.bookLocation = jsonObject.getString("university");
					
					bookOrder.bookExpirationTime = parseDate(jsonObject.getString("return_time"));
					
					booksOrderList.add(bookOrder);
					
					
				}
				
				
			}else if(jsonObject.getString("status").equals("0")){
				Toast.makeText(MyInfoRemind.this, "您没有借书记录", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return booksOrderList;
		
	}
	private String parseDate(String date){
		if(date != null){
			//String[] dateString = date.split("-");
			String[] dateYMD = date.split("-");
			//String[] dateHM = dateString[1].split(":");
			return dateYMD[0] + "年" + dateYMD[1] + "月" + dateYMD[2] + "日";
		}
		
		return null;
	}
}
