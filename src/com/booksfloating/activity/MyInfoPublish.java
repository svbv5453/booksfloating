package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.adapter.MyInfoPublishAdapter;
import com.booksfloating.domain.MyInfoPublishBookBean;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.LoadingAnimation;
import com.booksfloating.util.PSHMyComparator;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.booksfloating.widget.MyCustomProgressDialog;
import com.xd.booksfloating.R;
import com.xd.dialog.DialogFactory;

public class MyInfoPublish extends Activity{
	
	
	private static String book_info = "{\"status\":\"1\",\"booklist\":" +
			"[{\"book\":\"java编程思想\",\"author\":\"Bruce Eckel\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1320039.jpg\"}," +
			"{\"book\":\"我们仨\",\"author\":\"杨绛\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1015872.jpg\"}," +
			"{\"book\":\"围城\",\"author\":\"钱钟书\",\"university\":\"西安电子科技大学北校区\",\"publishTime\":\"2016年2月1日15时01分\",\"remark\":\"希望好心人尽快帮我借到！\",\"picture\":\"https://img1.doubanio.com/lpic/s1070222.jpg\"}]}";
	
	private ListView myInfoBookPublishListView = null;
	private Button btn_myinfo_search_book = null;
	private Button btn_back = null;
	private EditText et_search = null;	
	private List<MyInfoPublishBookBean> myPublishBookBeanList;
	private List<MyInfoPublishBookBean> myPublishBookBeanList2;
	private MyCustomProgressDialog myCustomProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_public_layout);
		Intent intent =getIntent();
		myInfoBookPublishListView= (ListView)findViewById(R.id.lv_my_info_book_publish);
		btn_myinfo_search_book = (Button) findViewById(R.id.btn_my_info_search_book);
		btn_back = (Button)findViewById(R.id.back);
		et_search = (EditText) findViewById(R.id.et_my_info_search_publish);
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intentBack = new Intent(MyInfoPublish.this, MyInfoFragment.class);
				startActivity(intentBack);*/
				finish();
				
			}
		});
		btn_myinfo_search_book.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String key = et_search.getText().toString().trim();
				
				if(!TextUtils.isEmpty(key)){
					myPublishBookBeanList2 = new ArrayList<MyInfoPublishBookBean>();
					for(MyInfoPublishBookBean myInfoPublishBookBean : myPublishBookBeanList){
						if(myInfoPublishBookBean.getBookName().equalsIgnoreCase(key)){
							myPublishBookBeanList2.add(myInfoPublishBookBean);
							
						}
					}
					if(!myPublishBookBeanList2.isEmpty()){
						MyInfoPublishAdapter adapter = new MyInfoPublishAdapter(MyInfoPublish.this, myPublishBookBeanList2);
						myInfoBookPublishListView.setAdapter(adapter);
					}else{
						Toast.makeText(MyInfoPublish.this, "未查到相关数据，请输入正确的书名", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(MyInfoPublish.this, "请输入查询数据", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		SharePreferenceUtil sp = new SharePreferenceUtil(MyInfoPublish.this, Constants.SAVE_USER);
		String url = HttpUtil.MY_PUBLISH+"?token=" + sp.getToken();
		if(!sp.getToken().isEmpty()){
			//startLoadingAnimation();
			showLoadingDialog();
			loadData(MyInfoPublish.this, url);
			
			
		}else{
			Toast.makeText(MyInfoPublish.this, "你尚未登录，无法查看您的信息", Toast.LENGTH_SHORT).show();
		}
		
		
		
		
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
			//stopLoadingAnimation();
			dismissLoadingDialog();
		}
	}
	private void loadListData(final Context context, String url) {
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectReqest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				System.out.println("MyinPublish" + response.toString());
				ACache.get(context).put("myInfoPublish", response);
				//stopLoadingAnimation();
				dismissLoadingDialog();
				showListData(context, response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//stopLoadingAnimation();
				dismissLoadingDialog();
				Toast.makeText(context, "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show();
				
			}
		});
		requestQueue.add(jsonObjectReqest);
		
	}
	protected void showListData(Context context, JSONObject response) {
		parseJsonData(response);
		if(myPublishBookBeanList.size() > 1){
			PSHMyComparator comparator = new PSHMyComparator();
			Collections.sort(myPublishBookBeanList, comparator);
		}
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
		System.out.println(jsonObject.toString());
		try {
			//JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				JSONArray jsonArray = jsonObject.getJSONArray("message");
				if(jsonArray.length() > 0){
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
						
					}
				}else {
					Toast.makeText(MyInfoPublish.this, "您尚未发布信息", Toast.LENGTH_SHORT).show();
				}
				
				
			}else if(jsonObject.getString("status").equals("0")){
				Toast.makeText(MyInfoPublish.this, "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show();
				
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return myPublishBookBeanList;
	}

	public  void startLoadingAnimation(){
		
		if(myCustomProgressDialog == null){
			myCustomProgressDialog = MyCustomProgressDialog.createDialog(MyInfoPublish.this);
			myCustomProgressDialog.setMessage("正在拼命加载中...");
		}
		
		myCustomProgressDialog.show();
	}
	public  void stopLoadingAnimation(){
		if(myCustomProgressDialog != null){
			myCustomProgressDialog.dismiss();
			myCustomProgressDialog = null;
		}
	}
	/**
	 * 刘文苑的加载动画
	 */
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在搜索，请稍后...");
			dialog.show();
		}
		else {
			dialog.dismiss();
		}
	}
	
	private void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	

	

}
