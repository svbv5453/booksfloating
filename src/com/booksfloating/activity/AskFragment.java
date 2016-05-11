package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.adapter.BookRecommendAdapter;
import com.booksfloating.adapter.MyInfoOrderAdapter;
import com.booksfloating.adapter.MyInfoPublishAdapter;
import com.booksfloating.domain.BooksRecommendBean;
import com.booksfloating.domain.MyInfoBookDetailBean;
import com.booksfloating.domain.MyInfoPublishBookBean;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class AskFragment extends Fragment {
	
	
	private List<MyInfoBookDetailBean> booksOrderList;
	private List<MyInfoBookDetailBean> booksOrderList2;
	private MyInfoBookDetailBean bookOrder;

	private ListView myInfoOrderListView = null;
	//private Button btn_myinfo_search_book = null;
	private Button btn_myinfo_search_book = null;
	private EditText et_search = null;
	
	private static String urlTest = "http://www.imooc.com/api/teacher?type=4&num=30";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view = inflater.inflate(R.layout.myinfo_order_askfragment_test, container, false);
		View view = inflater.inflate(R.layout.myinfo_order_layout, container, false);
		myInfoOrderListView = (ListView)view.findViewById(R.id.lv_my_info_book_order);
		 et_search = (EditText) view.findViewById(R.id.et_my_info_search_order);
		 btn_myinfo_search_book = (Button) view.findViewById(R.id.btn_my_info_search_book);
			btn_myinfo_search_book.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String key = et_search.getText().toString().trim();
					
					if(!TextUtils.isEmpty(key)){
						booksOrderList2 =new ArrayList<MyInfoBookDetailBean>();
						for(MyInfoBookDetailBean myorderBook : booksOrderList){
							if(myorderBook.getBookName().equalsIgnoreCase(key)){
								
								booksOrderList2.add(myorderBook);
							}
						}
						if(!booksOrderList2.isEmpty()){
							MyInfoOrderAdapter adapter = new MyInfoOrderAdapter(getActivity(), booksOrderList2);
							myInfoOrderListView.setAdapter(adapter);
						}else{
							Toast.makeText(getActivity(), "未查到相关数据，请输入正确的书名", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getActivity(), "请输入查询数据", Toast.LENGTH_SHORT).show();
					}
				}
			});
		//loadData(getActivity(), urlTest);
		/**
		 * 实际方法，
		 */
		SharePreferenceUtil sp = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
		String url = HttpUtil.BORROW_ORDER + "?token=" + sp.getToken();
		loadData(getActivity(), url);
		
		myInfoOrderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(parent.getId()){
				case R.id.lv_my_info_book_order:
					IntentToActivity(position);
					break;
				}
				
			}
		});
		return view;
	}
	
	
	private void IntentToActivity(int position) {
		
		
		Intent intent = new Intent(getActivity(), MyInfoOrderAskDetailActivity.class);
		
		/*Bundle mBundle = new Bundle();
		mBundle.putParcelable("borrowOrder", booksOrderList.get(position));
		System.out.println(booksOrderList.get(position).getLenderName());
		intent.putExtras(mBundle);*/
		intent.putExtra("borrowOrder", booksOrderList.get(position));
		startActivity(intent);
		
	}
	public void loadData(Context context, String url){
		if(isNetwrokAvaliable(context)){
			loadListData(context, url);
		}else {
			
			JSONObject response = ACache.get(context).getAsJSONObject("求助订单");
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
				ACache.get(context).put("求助订单", response);
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
		MyInfoOrderAdapter adapter = new MyInfoOrderAdapter(getActivity(), booksOrderList);
		myInfoOrderListView.setAdapter(adapter);
		
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


	public List<MyInfoBookDetailBean> parseJsonData(JSONObject jsonObject) {
		
		booksOrderList = new ArrayList<MyInfoBookDetailBean>();
		try {
			
			
			if(jsonObject.getString("status").equals("1")){
				
				//预留解析
				
				JSONArray jsonArray = jsonObject.getJSONArray("message");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					
					
					String bookName = jsonObject.getString("book");
					String bookAuthor = jsonObject.getString("author");
					String bookLocation = jsonObject.getString("university");
					String bookPublicshTime = parseDate(jsonObject.getString("lend_time"));
					String lenderName = jsonObject.getString("lender");
					//String lenderUniversity = jsonObject.getString("lender_university");
					String borrowTime = parseDate(jsonObject.getString("lend_time"));
					String returnTime = parseDate(jsonObject.getString("return_time"));
					String phoneNumber = jsonObject.getString("phone");
					
					bookOrder = new MyInfoBookDetailBean();
					bookOrder.setBookAuthor(bookAuthor);
					bookOrder.setBookLocation(bookLocation);
					bookOrder.setBookName(bookName);
					bookOrder.setBookPublicshTime(bookPublicshTime);
					bookOrder.setBorrowTime(borrowTime);
					bookOrder.setLenderName(lenderName);
					bookOrder.setPhoneNumber(phoneNumber);
					bookOrder.setReturnTime(returnTime);
					booksOrderList.add(bookOrder);
					
					
				}
				
				
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
