package com.booksfloating.activity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.booksfloating.domain.MyInfoBookDetailBean;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.booksfloating.util.SingleRequestQueue;
import com.xd.booksfloating.R;

public class MyInfoOrderHelpDetailActivity extends FragmentActivity{
	private TextView orderNumber = null;
	private TextView orderDate = null;
	private TextView orderMessage = null;
	private TextView helper = null;
	private TextView bookName = null;
	private TextView author = null;
	private TextView bookLocation = null;
	private TextView borrowDate = null;
	private TextView returnDate = null;
	private TextView phoneNumber = null;
	private TextView university = null;
	private Button btn_confirm = null;
	private MyInfoBookDetailBean bookDetailBean;
	private MyInfoBookDetailBean bookOrder;
	
	private SharePreferenceUtil sp;
	private String orderID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_help_order_detail);
		
		sp = new SharePreferenceUtil(MyInfoOrderHelpDetailActivity.this, Constants.SAVE_USER);
		
		//bookOrder = getIntent().getParcelableExtra("lendOrder");
		bookOrder = (MyInfoBookDetailBean) getIntent().getExtras().getSerializable("lendOrder");
		helper = (TextView) findViewById(R.id.tv_myinfo_ask_helper);
		bookName = (TextView) findViewById(R.id.tv_myinfo_ask_book_name);
		author = (TextView) findViewById(R.id.tv_myinfo_ask_book_author);
		bookLocation = (TextView) findViewById(R.id.tv_myinfo_ask_book_location);
		//orderNumber = (TextView) findViewById(R.id.tv_myinfo_ask_orderNumber);
		orderDate = (TextView) findViewById(R.id.tv_myinfo_ask_date);
		//university = (TextView) findViewById(R.id.tv_myinfo_ask_university);
		orderID = bookOrder.getOrderID();
		
		borrowDate = (TextView) findViewById(R.id.tv_myinfo_ask_borrowDate);
		returnDate = (TextView) findViewById(R.id.tv_myinfo_ask_returnDate);
		phoneNumber = (TextView) findViewById(R.id.tv_myinfo_ask_phoneNumber);
		btn_confirm = (Button) findViewById(R.id.btn_confirm_return);
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog(MyInfoOrderHelpDetailActivity.this);
			}
		});
			
			
				
		/**
		 * 从上页传递数据过来
		 */
		showData();
		
		
	}
		
	public void createDialog(Context context){
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("确认归还");
		dialog.setMessage("您确认对方已经归还了书籍");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				String token = sp.getToken();
				String url = HttpUtil.FINISH_ORDER + "?token=" + token + "&orderID=" + orderID;
				System.out.println(url);
				RequestQueue rq = SingleRequestQueue.getInstance(MyInfoOrderHelpDetailActivity.this);
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						System.out.println(response.toString());
						if(parseResponse(response)){
							Toast.makeText(MyInfoOrderHelpDetailActivity.this, "归还成功", Toast.LENGTH_SHORT).show();
							btn_confirm.setText("已确认归还");
							btn_confirm.setEnabled(false);
						}else {
							Toast.makeText(MyInfoOrderHelpDetailActivity.this, "归还失败，请稍后重试", Toast.LENGTH_SHORT).show();
						}
						
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(MyInfoOrderHelpDetailActivity.this, "服务器错误，请稍后访问", Toast.LENGTH_SHORT).show();
					}
				});
				rq.add(jsonObjectRequest);
				
			}

			
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.create().show();
		
		
		
	}

	


	/*protected void httpRequest(String url) throws IOException {
		// TODO Auto-generated method stub
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				// TODO Auto-generated method stub
				if(response.isSuccessful()){
					System.out.println("订单详情返回数据:" + response.body().toString());
					if(parseResponse(response.body().toString())){
						btn_confirm.setText("已确认归还");
						btn_confirm.setEnabled(false);
						Toast.makeText(MyInfoOrderHelpDetailActivity.this, "归还成功", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
			@Override
			public void onFailure(Call call, IOException ioException) {
				// TODO Auto-generated method stub
				Toast.makeText(MyInfoOrderHelpDetailActivity.this, "请求异常，请稍后访问", Toast.LENGTH_SHORT).show();
			}
		});
			
			
	}*/
	protected boolean parseResponse(JSONObject response) {
			
			try {
				String	status = response.getString("status");
				if(status.equals("1")){
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	
		return false;
		
	}

	private void showData() {
		// TODO Auto-generated method stub
	    orderDate.setText(bookOrder.getBookPublicshTime());
		helper.setText(bookOrder.getLenderName());
		bookName.setText(bookOrder.getBookName());
		author.setText(bookOrder.getBookAuthor());
		bookLocation.setText(bookOrder.getBookLocation());
		borrowDate.setText(bookOrder.getBorrowTime());
		returnDate.setText(bookOrder.getReturnTime());
		//university.setText(bookOrder.getLenderUniversity());
		phoneNumber.setText( bookOrder.getPhoneNumber());
	}

	
		
		
	
	
	
	
	

}
