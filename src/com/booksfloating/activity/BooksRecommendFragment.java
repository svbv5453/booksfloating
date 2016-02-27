package com.booksfloating.activity;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.adapter.BookRecommendAdapter;
import com.booksfloating.domain.BooksRecommendBean;
import com.booksfloating.util.ACache;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class BooksRecommendFragment extends Fragment{
	private static String url = "www.baidu.com";
	private static String book_info = "{\"status\":\"1\",\"booklist\":" +
			"[{\"book\":\"java编程思想\",\"author\":\"Bruce Eckel\",\"picture\":\"https://img1.doubanio.com/lpic/s1320039.jpg\"}," +
			"{\"book\":\"我们仨\",\"author\":\"杨绛\",\"picture\":\"https://img1.doubanio.com/lpic/s1015872.jpg\"}," +
			"{\"book\":\"围城\",\"author\":\"钱钟书\",\"picture\":\"https://img1.doubanio.com/lpic/s1070222.jpg\"}]}";
	private static String urltest = "http://www.imooc.com/api/teacher?type=4&num=30";
	private ListView booksRecommendList = null;
	private List<BooksRecommendBean> booksBeanList;
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData(getActivity(), urltest);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
		Log.d("TAG", "Max memory is " + maxMemory + "KB");
		View view = inflater.inflate(R.layout.books_recommend, container, false);
		booksRecommendList = (ListView) view.findViewById(R.id.books_recommend_list);
		/*BooksRecommendAsyncTask booksRecommendAsyncTask = new BooksRecommendAsyncTask();
		booksRecommendAsyncTask.execute(urltest);
		*/
		loadData(getActivity(), urltest);
		
		return view;
	}
	
	//暂时不用
	/*class BooksRecommendAsyncTask extends AsyncTask<String, Void, List<BooksRecommendBean>>{

		@Override
		protected List<BooksRecommendBean> doInBackground(String... params) {
			String jsonData = HttpUtil.getJsonData(params[0]);
			return parseJsonData(jsonData);
		}
		@Override
		protected void onPostExecute(List<BooksRecommendBean> result) {
			super.onPostExecute(result);
			BookRecommendAdapter adapter = new BookRecommendAdapter(getActivity(), booksBeanList);
			booksRecommendList.setAdapter(adapter);
		}

		
	}*/
	public void loadData(Context context, String url){
		if(isNetwrokAvaliable(context)){
			loadListData(context, url);
		}else {
			
			JSONObject response = ACache.get(context).getAsJSONObject("bookRecommend");
			if(response != null){
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
				showListData(context, response);
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
	public void loadListData(final Context context, String url){
		
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				ACache.get(context).put("bookRecommend", response);
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
		BookRecommendAdapter adapter = new BookRecommendAdapter(context, booksBeanList);
		booksRecommendList.setAdapter(adapter);
	}
	
	private List<BooksRecommendBean> parseJsonData(JSONObject jsonObject) {
		booksBeanList = new ArrayList<BooksRecommendBean>();
		try {
			
			//JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				
				/*JSONArray jsonArray = jsonObject.getJSONArray("bookList");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					BooksRecommendBean booksRecommendBean = new BooksRecommendBean();
					booksRecommendBean.bookName = jsonObject.getString("book");
					booksRecommendBean.bookAuthor = jsonObject.getString("author");
					booksRecommendBean.bookImageUrl = jsonObject.getString("picture");
					booksRecommendBean.bookRanking = "No."+i;
					booksBeanList.add(booksRecommendBean);
					
					
				}*/
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					BooksRecommendBean booksRecommendBean = new BooksRecommendBean();
					booksRecommendBean.bookName = jsonObject.getString("name");
					booksRecommendBean.bookAuthor = jsonObject.getString("description");
					booksRecommendBean.bookImageUrl = jsonObject.getString("picSmall");
					
					booksRecommendBean.bookRanking = "No."+ (i+1);
					booksBeanList.add(booksRecommendBean);
					
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return booksBeanList;
		
		
	}
	
}
