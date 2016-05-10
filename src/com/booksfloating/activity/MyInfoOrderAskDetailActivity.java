package com.booksfloating.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.booksfloating.domain.MyInfoBookDetailBean;
import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;

public class MyInfoOrderAskDetailActivity extends FragmentActivity{
	private TextView orderNumber = null;
	private TextView orderDate = null;
	private TextView helper = null;
	private TextView bookName = null;
	private TextView author = null;
	private TextView bookLocation = null;
	private TextView borrowDate = null;
	private TextView returnDate = null;
	private TextView phoneNumber = null;
	private TextView university = null;
	private MyInfoBookDetailBean bookDetailBean;
	private MyInfoBookDetailBean bookOrder;
	private static String urlTest = "http://www.imooc.com/api/teacher?type=4&num=30";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_ask_order_detail);
		
		//getActionBar().setTitle("订单详情");
		
		bookOrder = (MyInfoBookDetailBean) getIntent().getExtras().getSerializable("borrowOrder");
		
		
		/**
		 * 通过使用传递对象，则不需要进行向服务器请求数据了
		 */
		helper = (TextView) findViewById(R.id.tv_myinfo_ask_helper);
		bookName = (TextView) findViewById(R.id.tv_myinfo_ask_book_name);
		author = (TextView) findViewById(R.id.tv_myinfo_ask_book_author);
		bookLocation = (TextView) findViewById(R.id.tv_myinfo_ask_book_location);
		orderNumber = (TextView) findViewById(R.id.tv_myinfo_ask_orderNumber);
		orderDate = (TextView) findViewById(R.id.tv_myinfo_ask_date);
		university = (TextView) findViewById(R.id.tv_myinfo_ask_university);
		borrowDate = (TextView) findViewById(R.id.tv_myinfo_ask_borrowDate);
		returnDate = (TextView) findViewById(R.id.tv_myinfo_ask_returnDate);
		phoneNumber = (TextView) findViewById(R.id.tv_myinfo_ask_phoneNumber);
		
		/*MyInfoOrderDetailAsyncTask orderDetailAsyncTask = new MyInfoOrderDetailAsyncTask();
		orderDetailAsyncTask.execute(urlTest);*/
		
			showData();
		
		
	}
		
		private void showData() {
		// TODO Auto-generated method stub
			helper.setText(bookOrder.getLenderName());
			bookName.setText(bookOrder.getBookName());
			author.setText(bookOrder.getBookAuthor());
			bookLocation.setText(bookOrder.getBookLocation());
			borrowDate.setText(bookOrder.getBorrowTime());
			returnDate.setText(bookOrder.getReturnTime());
			university.setText(bookOrder.getLenderUniversity());
			phoneNumber.setText( bookOrder.getPhoneNumber());
	}

		/*class MyInfoOrderDetailAsyncTask extends AsyncTask<String, Void, MyInfoBookDetailBean>{

			@Override
			protected MyInfoBookDetailBean doInBackground(String... params) {
				String jsonData = HttpUtil.getJsonData(params[0]);
				
				
				return parseJsonData(jsonData);
			}
			@Override
			protected void onPostExecute(MyInfoBookDetailBean result) {
				super.onPostExecute(result);
				//预留返回
				orderMessage.setText("昵称为" + bookDetailBean.userName + "的用户已接受您关于\"" 
				+ bookDetailBean.bookName + ", " + bookDetailBean.bookAuthor + ", " + bookDetailBean.bookLocation + ",的求助 ");
					borrowDate.setText("借书日: " + bookDetailBean.borrowTime);
					returnDate.setText("还书日: " + bookDetailBean.returnTime);
					university.setText("他所在的学校为: " + bookDetailBean.userUniversity);
					phoneNumber.setText("他的手机号为: " + bookDetailBean.phoneNumber);
					
					borrowDate.setText("求助订单_借书日: " + result.borrowTime);
					returnDate.setText("求助订单_还书日: " + result.returnTime);
			}
			
		}
		//线程错误
		HttpUtilCheck.sendHttpRequest(urlTest, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				bookDetailBean = parseJsonData(response);
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						orderMessage.setText("昵称为" + bookDetailBean.userName + "的用户已接受您关于\"" 
					+ bookDetailBean.bookName + ", " + bookDetailBean.bookAuthor + ", " + bookDetailBean.bookLocation + ",的求助 ");
						borrowDate.setText("借书日: " + bookDetailBean.borrowTime);
						returnDate.setText("还书日: " + bookDetailBean.returnTime);
						university.setText("他所在的学校为: " + bookDetailBean.userUniversity);
						phoneNumber.setText("他的手机号为: " + bookDetailBean.phoneNumber);
						
						borrowDate.setText("借书日: " + bookDetailBean.borrowTime);
						returnDate.setText("还书日: " + bookDetailBean.returnTime);
						
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//Toast.makeText(getApplicationContext(), "系统繁忙，请稍后...", Toast.LENGTH_SHORT).show();
						
					}
				});
				
			}
		});
		
		
	
	
	
	
	
	public MyInfoBookDetailBean parseJsonData(String jsonData) {
		
		try {
			MyInfoBookDetailBean bookDetailBean = new MyInfoBookDetailBean();
			JSONObject jsonObject = new JSONObject(jsonData);
			
			if(jsonObject.getString("status").equals("1")){
				//预留解析
				bookDetailBean.userName = jsonObject.getString("user_name");
				bookDetailBean.bookName = jsonObject.getString("book");
				bookDetailBean.bookAuthor = jsonObject.getString("author");
				bookDetailBean.bookLocation = jsonObject.getString("book_location");
				bookDetailBean.borrowTime = jsonObject.getString("lend_time");
				bookDetailBean.returnTime = jsonObject.getString("return_time");
				bookDetailBean.userUniversity = jsonObject.getString("university");
				bookDetailBean.phoneNumber = jsonObject.getString("phone");
				
				
				
				
				JSONArray jsonArray = jsonObject.getJSONArray("data");
					jsonObject = jsonArray.getJSONObject(0);
					Log.d("MyInfoOrderDetailActivity", jsonData);
					bookDetailBean.borrowTime = jsonObject.getString("name");
					bookDetailBean.returnTime = jsonObject.getString("description");
					
					
				return bookDetailBean;
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
*/
}
