package com.booksfloating.activity.infonotice;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.activity.MainActivity;
import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.Toast;

public class HelpBorrowActivity extends Activity implements OnClickListener{
	private Button btn_suretoborrow, btn_back;
	private TextView tv_expect_borrow_time, tv_expect_return_time;
	private DatePicker dp_expect_borrow_time, dp_expect_return_time;
	//上一个activity传过来的信息
	private BooksAttr booksAttr = new BooksAttr();
	//从booksAttr中解析得到borrowInfo
	private BorrowInfo borrowInfo = new BorrowInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helptoborrow_info);
		booksAttr = (BooksAttr)getIntent().getSerializableExtra("intent_booksAttr");
		borrowInfo = booksAttr.getBorrowInfo();
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		tv_expect_borrow_time = (TextView)findViewById(R.id.tv_expect_borrow_time);
		tv_expect_return_time = (TextView)findViewById(R.id.tv_expect_return_time);
		
		dp_expect_borrow_time = (DatePicker)findViewById(R.id.dp_expect_borrow_time);		
		dp_expect_return_time = (DatePicker)findViewById(R.id.dp_expect_return_time);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {  
			dp_expect_borrow_time.setCalendarViewShown(false);  
			dp_expect_return_time.setCalendarViewShown(false);
	    }  
		else {
			dp_expect_borrow_time.setCalendarViewShown(true);
			dp_expect_return_time.setCalendarViewShown(true);
		}

		btn_suretoborrow = (Button)findViewById(R.id.btn_suretoborrow);
		btn_suretoborrow.setOnClickListener(this);
		initData();
	}

	private int year1;
	private int month1;
	private int day1;
	private int year2;
	private int month2;
	private int day2;
			
	private void initData()
	{
		Calendar c =Calendar.getInstance();   
        year1 = c.get(Calendar.YEAR);
        c.add(Calendar.MONTH, 1);
        month1 = c.get(Calendar.MONTH); 
        day1 = c.get(Calendar.DAY_OF_MONTH);   
        tv_expect_borrow_time.setText(year1+"-"+month1+"-"+day1); 
		
		dp_expect_borrow_time.init(dp_expect_borrow_time.getYear(), dp_expect_borrow_time.getMonth(), 
				dp_expect_borrow_time.getDayOfMonth(), new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				year1 = year;
				month1 = monthOfYear+1;
				day1 = dayOfMonth;
				
				StringBuffer lend_time = new StringBuffer();
				lend_time.append(year1);
				lend_time.append("-");
				lend_time.append(month1);
				lend_time.append("-");
				lend_time.append(day1);
								
				tv_expect_borrow_time.setText(lend_time);
			}
		});
		
		 year2 = c.get(Calendar.YEAR); 
	     month2 = c.get(Calendar.MONTH);
	     day2 = c.get(Calendar.DAY_OF_MONTH); 
	     tv_expect_return_time.setText(year2+"-"+month2+"-"+ day2);
	     
		dp_expect_return_time.init(dp_expect_return_time.getYear(), dp_expect_return_time.getMonth(), 
				dp_expect_return_time.getDayOfMonth(), new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				year2 = year;
				month2 = monthOfYear+1;
				day2 = dayOfMonth;
				
				StringBuffer return_time = new StringBuffer();
				return_time.append(year2);
				return_time.append("-");
				return_time.append(month2);
				return_time.append("-");
				return_time.append(day2);
				
				tv_expect_return_time.setText(return_time);
			}
		});
	}
			
	private void submitData()
	{		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//这个SharePreferenceUtil是注册的时候就存好的用户基本信息
				SharePreferenceUtil sp = new SharePreferenceUtil(HelpBorrowActivity.this, Constants.SAVE_USER);
				final PostParameter[] postParameters = new PostParameter[5];
				postParameters[0] = new PostParameter("lend_time", tv_expect_borrow_time.getText().toString());
				postParameters[1] = new PostParameter("return_time", tv_expect_return_time.getText().toString());
				postParameters[2] = new PostParameter("orderID", booksAttr.getOrderID());
				postParameters[3] = new PostParameter("lender", sp.getAccount());
				postParameters[4] = new PostParameter("token", sp.getToken());
				
				String jsonString = null;
				jsonString = HttpUtil.httpRequest(HttpUtil.HELP_BORROW, postParameters, HttpUtil.POST);
				if (jsonString == null) {
					handler.sendEmptyMessage(-1);
				}else {
					try {
						JSONObject jsonObject = new JSONObject(jsonString);
						String status = jsonObject.getString("status");
						if (status.equals("1")) {
							handler.sendEmptyMessage(0);
						}
						else {
							handler.sendEmptyMessage(-2);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(HelpBorrowActivity.this, "恭喜你 !你成功帮助了一位同学！", Toast.LENGTH_SHORT);
				SharePreferenceUtil sp = new SharePreferenceUtil(HelpBorrowActivity.this, Constants.SAVE_USER);
				sp.setCreditScore(20);
				SystemClock.sleep(Toast.LENGTH_SHORT);
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				intent.putExtra("intent_fragmentId", MainActivity.TAB_INFO_NOTICE);
				startActivity(intent);
				finish();
				//HelpBorrowActivity.this.startActivityForResult(intent, requestCode)
				break;
			case -1:
				Toast.makeText(HelpBorrowActivity.this, "服务器错误，请稍后重试！", Toast.LENGTH_SHORT).show();
				break;
			case -2:
				Toast.makeText(HelpBorrowActivity.this, "帮忙失败，请稍后重试！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_suretoborrow:
			if (year1 > year2 || (year1 == year2 && month1 > month2) || (year1 == year2 && month1 == month2 && day1 > day2)) {
				DialogFactory.AlertDialog(this, "提示", "还书日期不能早于借书日期！");
			}
			else if (year1 < year2) {
				DialogFactory.AlertDialog(this, "提示", "同学，真的可以借这么久吗？");
			}else {
				submitData();
			}			
			break;
		
		case R.id.btn_back:
			finish();
			break;
			
		default:
			break;
		}
	}
}
