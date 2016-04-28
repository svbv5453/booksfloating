package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.booksfloating.adapter.MyInfoPublishAdapter;
import com.booksfloating.domain.MyInfoPublishBookBean;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoPublish extends Activity{
	
	private static String urlTest = "http://www.imooc.com/api/teacher?type=4&num=30";
	private static String book_info = "{\"status\":\"1\",\"booklist\":" +
			"[{\"book\":\"java编程思想\",\"author\":\"Bruce Eckel\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1320039.jpg\"}," +
			"{\"book\":\"我们仨\",\"author\":\"杨绛\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1015872.jpg\"}," +
			"{\"book\":\"围城\",\"author\":\"钱钟书\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1070222.jpg\"}]}";
	
	private ListView myInfoBookPublishListView = null;
	private Button btn_myinfo_search_book = null;
	private Button btn_back = null;
	private List<MyInfoPublishBookBean> myPublishBookBeanList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_public_layout);
		//getActionBar().setTitle("我的发布");
		
		
		Intent intent =getIntent();
		myInfoBookPublishListView= (ListView)findViewById(R.id.lv_my_info_book_publish);
		btn_myinfo_search_book = (Button) findViewById(R.id.btn_my_info_search_book);
		btn_back = (Button)findViewById(R.id.btn_back);
		/**
		 * 有的问题，过渡不流畅
		 */
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentBack = new Intent(MyInfoPublish.this, MyInfoFragment.class);
				startActivity(intentBack);
				
			}
		});
		btn_myinfo_search_book.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(MyInfoPublish.this, "预留搜索", Toast.LENGTH_SHORT).show();
			}
		});
		loadData(this, urlTest);
		//loadData(this, HttpUtil.MY_PUBLISH);
		
		
		
	}
	public void loadData(Context context, String url){
		if(isNetworkAvailable(context)){
			loadListData(context, url);
		}else {
			
			JSONObject response = ACache.get(context).getAsJSONObject("myInfoPublish");
			if(response != null){
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
				showListData(context, response);
			}
			Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			
		}
	}
	private void loadListData(final Context context, String url) {
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectReqest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				ACache.get(context).put("myInfoPublish", response);
				showListData(context, response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
		requestQueue.add(jsonObjectReqest);
		
	}
	protected void showListData(Context context, JSONObject response) {
		parseJsonData(response);
		MyInfoPublishAdapter adapter = new MyInfoPublishAdapter(context, myPublishBookBeanList);
		myInfoBookPublishListView.setAdapter(adapter);
	}
	public boolean isNetworkAvailable(Context context){
		ConnectivityManager conecntivityMananger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = conecntivityMananger.getAllNetworkInfo();
		if(info != null){
			for(int i = 0; i < info.length; i++){
				if(info[i].getState() == NetworkInfo.State.CONNECTED){
					return true;
				}
			}
		}
		
		return false;
		
		
	}
	
	
	
	private List<MyInfoPublishBookBean> parseJsonData(JSONObject jsonObject) {
		myPublishBookBeanList = new ArrayList<MyInfoPublishBookBean>();
		try {
			//JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				/*JSONArray jsonArray = jsonObject.getJSONArray("message");
				for(int i = 0; i<jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					MyInfoPublishBookBean publishBookBean = new MyInfoPublishBookBean();
					publishBookBean.bookName = jsonObject.getString("book");
					publishBookBean.bookAuthor = jsonObject.getString("author");
					publishBookBean.bookLocation = jsonObject.getString("university");
					publishBookBean.bookPublicshTime = jsonObject.getString("publish_time");
					publishBookBean.bookRemark = jsonObject.getString("remarks");
					publishBookBean.bookIconUrl =jsonObject.getString("picture");
					myPublishBookBeanList.add(publishBookBean);
					
				}*/
				
				
				
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					MyInfoPublishBookBean publishBookBean = new MyInfoPublishBookBean();
					publishBookBean.bookName = jsonObject.getString("name");
					publishBookBean.bookAuthor = jsonObject.getString("description");
					publishBookBean.bookIconUrl = jsonObject.getString("picSmall");
					
					myPublishBookBeanList.add(publishBookBean);
					
				}
				return myPublishBookBeanList;
				
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	

}
