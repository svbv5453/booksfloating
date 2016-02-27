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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.booksfloating.adapter.MyInfoOrderAdapter;
import com.booksfloating.domain.BooksRecommendBean;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;

public class AskFragment extends Fragment {
	
	private List<BooksRecommendBean> booksBeanList;

	private ListView myInfoOrderListView = null;
	private Button btn_myinfo_search_book = null;
	private static String urlTest = "http://www.imooc.com/api/teacher?type=4&num=30";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View view = inflater.inflate(R.layout.myinfo_order_askfragment_test, container, false);
		View view = inflater.inflate(R.layout.myinfo_order_layout, container, false);
		myInfoOrderListView = (ListView)view.findViewById(R.id.lv_my_info_book_order);
		btn_myinfo_search_book = (Button) view.findViewById(R.id.btn_my_info_search_book);
		btn_myinfo_search_book.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getActivity(), "预留搜索", Toast.LENGTH_SHORT).show();
			}
		});
		loadData(getActivity(), urlTest);
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
		intent.putExtra("position", position);
		startActivity(intent);
		
	}
	public void loadData(Context context, String url){
		if(isNetwrokAvaliable(context)){
			MyInfoOrderAsyncTask myInfoOrderAsyncTask = new MyInfoOrderAsyncTask();
			myInfoOrderAsyncTask.execute(url);
		}else {
			
			String response = ACache.get(context).getAsString("求助订单");
			if(response != null){
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
				MyInfoOrderAdapter adapter = new MyInfoOrderAdapter(getActivity(), parseJsonData(response));
				myInfoOrderListView.setAdapter(adapter);
			}
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


	class MyInfoOrderAsyncTask extends AsyncTask<String, Void, List<BooksRecommendBean>>{

		@Override
		protected List<BooksRecommendBean> doInBackground(String... params) {
			String jsonData = HttpUtil.getJsonData(params[0]);
			ACache.get(getActivity()).put("求助订单", jsonData);
			
			return parseJsonData(jsonData);
		}
		@Override
		protected void onPostExecute(List<BooksRecommendBean> result) {
			super.onPostExecute(result);
			
			MyInfoOrderAdapter adapter = new MyInfoOrderAdapter(getActivity(), result);
			myInfoOrderListView.setAdapter(adapter);
		}
		
	}
	public List<BooksRecommendBean> parseJsonData(String jsonData) {
		booksBeanList = new ArrayList<BooksRecommendBean>();
		try {
			
			JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				//预留解析
				/*
				JSONArray jsonArray = jsonObject.getJSONArray("bookList");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					BooksRecommendBean booksRecommendBean = new BooksRecommendBean();
					booksRecommendBean.bookName = jsonObject.getString("book");
					booksRecommendBean.bookAuthor = jsonObject.getString("author");
					booksRecommendBean.bookLocation = jsonObject.getString("university");
					booksRecommendBean.bookPublicshTime = jsonObject.getString("publishTime");
					
					booksBeanList.add(booksRecommendBean);
					
					
				}*/
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					BooksRecommendBean booksRecommendBean = new BooksRecommendBean();
					booksRecommendBean.bookName = jsonObject.getString("name");
					booksRecommendBean.bookAuthor = jsonObject.getString("description");
					booksBeanList.add(booksRecommendBean);
					
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return booksBeanList;
		
	}

}
