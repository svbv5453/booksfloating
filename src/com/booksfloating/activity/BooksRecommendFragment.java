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
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.ListViewCompat;
import com.booksfloating.util.ListViewCompat.OnLoadListener;
import com.booksfloating.util.ListViewCompat.OnRefreshListener;
import com.booksfloating.util.SingleRequestQueue;
import com.booksfloating.widget.MyCustomProgressDialog;
import com.booksfloating.widget.MyPullToRefreshListView;
import com.booksfloating.widget.MyPullToRefreshListView.MyOnRefreshListener;
import com.xd.booksfloating.R;

public class BooksRecommendFragment extends Fragment implements OnRefreshListener, OnLoadListener{
	
	private static String TAG = "BooksRecommendFragment";
	
	private static String url = "www.baidu.com";
	private static String book_info = "{\"status\":\"1\",\"booklist\":" +
			"[{\"book\":\"java编程思想\",\"author\":\"Bruce Eckel\",\"picture\":\"https://img1.doubanio.com/lpic/s1320039.jpg\"}," +
			"{\"book\":\"我们仨\",\"author\":\"杨绛\",\"picture\":\"https://img1.doubanio.com/lpic/s1015872.jpg\"}," +
			"{\"book\":\"围城\",\"author\":\"钱钟书\",\"picture\":\"https://img1.doubanio.com/lpic/s1070222.jpg\"}]}";
	private static String urltest = "http://www.imooc.com/api/teacher?type=4&num=30";
	private ListView booksRecommendList = null;
	private List<BooksRecommendBean> booksBeanList = new ArrayList<BooksRecommendBean>();
	private List<BooksRecommendBean> booksList = new ArrayList<BooksRecommendBean>();
	
	private MyPullToRefreshListView prtListView;
	private ListViewCompat lwy_prtListView;
	private MyCustomProgressDialog myCustomProgressDialog;
	private BookRecommendAdapter adapter;
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//loadData(getActivity(), urltest);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
		Log.d("TAG", "Max memory is " + maxMemory + "KB");
		View view = inflater.inflate(R.layout.books_recommend, container, false);
		//booksRecommendList = (ListView) view.findViewById(R.id.books_recommend_list);
		/*prtListView = (MyPullToRefreshListView) view.findViewById(R.id.books_myprtListView);
		prtListView.setOnRefreshListener(this);*/
		lwy_prtListView = (ListViewCompat) view.findViewById(R.id.books_lwy_prtListView);
		lwy_prtListView.setOnLoadListener(this);
		lwy_prtListView.setOnRefreshListener(this);
		//startLoadingAnimation();
		
		adapter = new BookRecommendAdapter(getActivity(), booksList);
		lwy_prtListView.setAdapter(adapter);
		loadData(getActivity(), HttpUtil.BOOK_RECOMMEND);
		
		return view;
	}
	
	/*@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			
		}
	}*/
	public  void startLoadingAnimation(){
		
		if(myCustomProgressDialog == null){
			myCustomProgressDialog = MyCustomProgressDialog.createDialog(getActivity());
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
	
	
	
	public void loadData(Context context, String url){
		if(isNetwrokAvaliable(context)){
			loadListData(context, url);
			
		}else {
			
			JSONObject response = ACache.get(context).getAsJSONObject("bookRecommend");
			if(response != null){
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
				parseJsonData(response);
				if(booksList.size() > 0){
					booksList.clear();
				}
				booksList.addAll(booksBeanList);
				booksBeanList.clear();
				adapter.notifyDataSetChanged();
				
			}
			Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			//stopLoadingAnimation();
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
				System.out.println(response.toString());
				ACache.get(context).put("bookRecommend", response);
				parseJsonData(response);
				//stopLoadingAnimation();
				
				
				if(booksList.size() > 0){
					booksList.clear();
				}
				booksList.addAll(booksBeanList);
				booksBeanList.clear();
				//prtListView.hideHeaderView();
				lwy_prtListView.onRefreshComplete();
				adapter.notifyDataSetChanged();
				
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				//stopLoadingAnimation();
				//prtListView.hideHeaderView();
				Toast.makeText(context, "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show();
			}
		});
		requestQueue.add(jsonObjectRequest);
		
	}
	
	
	private List<BooksRecommendBean> parseJsonData(JSONObject jsonObject) {
		
		try {
			
			//JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				
				JSONArray jsonArray = jsonObject.getJSONArray("booklist");
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					BooksRecommendBean booksRecommendBean = new BooksRecommendBean();
					booksRecommendBean.bookName = jsonObject.getString("book");
					booksRecommendBean.bookAuthor = jsonObject.getString("author");
					booksRecommendBean.bookImageUrl = jsonObject.getString("picture");
					booksRecommendBean.bookRanking = "No."+(i+1);
					booksBeanList.add(booksRecommendBean);
					
					
				}
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return booksBeanList;
		
		
	}

	/**
	 * 刘文苑下拉刷新
	 */

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData(getActivity(), HttpUtil.BOOK_RECOMMEND);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "已经到底了，没有更多数据了！", Toast.LENGTH_SHORT).show();
		lwy_prtListView.onLoadComplete();
	}
	
}
