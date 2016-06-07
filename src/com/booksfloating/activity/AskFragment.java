package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.booksfloating.adapter.MyInfoOrderAdapter;
import com.booksfloating.domain.MyInfoBookDetailBean;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.ACache;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.LoadingAnimation;
import com.booksfloating.util.SHMyComparator;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.booksfloating.widget.MyCustomProgressDialog;
import com.xd.booksfloating.R;
import com.xd.dialog.DialogFactory;

public class AskFragment extends Fragment {
	private static String TAG = "Fragment";
	
	private List<MyInfoBookDetailBean> booksOrderList = new ArrayList<MyInfoBookDetailBean>();
	private List<MyInfoBookDetailBean> booksOrderList2;
	private MyInfoBookDetailBean bookOrder;

	private ListView myInfoOrderListView = null;
	//private Button btn_myinfo_search_book = null;
	private Button btn_myinfo_search_book = null;
	private EditText et_search = null;
	private MyCustomProgressDialog myCustomProgressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "Ask--------onCreateView()");
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
		
		if(!sp.getToken().isEmpty()){
			//startLoadingAnimation();
			showLoadingDialog();
			loadData(getActivity(), url);
			
		}else{
			Toast.makeText(getActivity(), "你尚未登录，无法查看您的信息", Toast.LENGTH_SHORT).show();
		}
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
			//stopLoadingAnimation();
			dismissLoadingDialog();
			Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
			
			
		}
	}
	public void loadListData(final Context context, String url){
		
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				System.out.println("AskFragment" + response.toString());
				ACache.get(context).put("求助订单", response);
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
		requestQueue.add(jsonObjectRequest);
		
	}
	public void showListData(Context context, JSONObject response){
		parseJsonData(response);
		if(booksOrderList.size() > 1){
			SHMyComparator comparator = new SHMyComparator();
			Collections.sort(booksOrderList, comparator);
		}
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
		
		
		try {
			if(jsonObject.getString("status").equals("1")){
				JSONArray jsonArray = jsonObject.getJSONArray("message");
				if(jsonArray.length() > 0){
					for(int i = 0; i < jsonArray.length(); i++){
						jsonObject = jsonArray.getJSONObject(i);
						
						
						String bookName = jsonObject.getString("book");
						String bookAuthor = jsonObject.getString("author");
						String bookLocation = jsonObject.getString("university");
						String bookPublicshTime = jsonObject.getString("publish_time");
						String lenderName = jsonObject.getString("lender");
						//String lenderUniversity = jsonObject.getString("lender_university");
						String borrowTime = jsonObject.getString("lend_time");
						String returnTime = jsonObject.getString("return_time");
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
				}else {
					Toast.makeText(getActivity(), "您还没有求助订单", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}else if(jsonObject.getString("status").equals("0")){
				Toast.makeText(getActivity(), "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show();
			}else if(jsonObject.getString("status").equals("-1")){
				Toast.makeText(getActivity(), "登陆超时，请重新登陆", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return booksOrderList;
		
	}
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
	
	/**
	 * 刘文苑的加载动画
	 */
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(getActivity(), "正在搜索，请稍后...");
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
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "Ask--------onResume()");
		if(booksOrderList.size() > 0){
			MyInfoOrderAdapter adapter = new MyInfoOrderAdapter(getActivity(), booksOrderList);
			myInfoOrderListView.setAdapter(adapter);
		}
		
		
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "Ask--------onActivityCreated()");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "Ask--------onActivityResult()");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d(TAG, "Ask--------onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Ask--------onCreate()");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "Ask--------onDestroy()");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "Ask--------onDestroyView()");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG, "Ask--------onDetach()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "Ask--------onPause()");
	}

	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "Ask--------onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "Ask--------onStop()");
	}

}
