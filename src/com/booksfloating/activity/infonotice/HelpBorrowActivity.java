package com.booksfloating.activity.infonotice;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.Toast;

public class HelpBorrowActivity extends Activity implements OnClickListener{
	private Button btn_suretoborrow;
	private TextView tv_expect_borrow_time, tv_expect_return_time;
	private DatePicker dp_expect_borrow_time, dp_expect_return_time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helptoborrow_info);
		
		tv_expect_borrow_time = (TextView)findViewById(R.id.tv_expect_borrow_time);
		tv_expect_return_time = (TextView)findViewById(R.id.tv_expect_return_time);
		
		dp_expect_borrow_time = (DatePicker)findViewById(R.id.dp_expect_borrow_time);		
		dp_expect_return_time = (DatePicker)findViewById(R.id.dp_expect_return_time);
		
		btn_suretoborrow = (Button)findViewById(R.id.btn_suretoborrow);
		btn_suretoborrow.setOnClickListener(this);
		initData();
	}

	private int year;
	private int month;
	private int day;
	private StringBuffer lend_time = new StringBuffer();
	private StringBuffer return_time = new StringBuffer();
	private void initData()
	{
		dp_expect_borrow_time.init(year, month, day, new OnDateChangedListener() {
			
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				HelpBorrowActivity.this.year = year;
				month = monthOfYear;
				day = dayOfMonth;
				lend_time.append(HelpBorrowActivity.this.year);
				lend_time.append("-");
				lend_time.append(month);
				lend_time.append("-");
				lend_time.append(day);
				
				tv_expect_borrow_time.setText(lend_time);
			}
		});
		
		dp_expect_return_time.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				HelpBorrowActivity.this.year = year;
				month = monthOfYear;
				day = dayOfMonth;
				
				return_time.append(HelpBorrowActivity.this.year);
				return_time.append("-");
				return_time.append(month);
				return_time.append("-");
				return_time.append(day);
				
				tv_expect_return_time.setText(return_time);
			}
		});
	}
	
	//上一个activity传过来的信息
	private BooksAttr booksAttr = new BooksAttr();
	//从booksAttr中解析得到borrowInfo
	private BorrowInfo borrowInfo = new BorrowInfo();
	private void submitData()
	{
		final PostParameter[] postParameters = new PostParameter[5];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//这个SharePreferenceUtil是注册的时候就存好的用户基本信息
				SharePreferenceUtil sp = new SharePreferenceUtil(HelpBorrowActivity.this, Constants.SAVE_USER);

				postParameters[0] = new PostParameter("lend_time", lend_time.toString());
				postParameters[1] = new PostParameter("return_time", return_time.toString());
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
							handler.sendEmptyMessage(-1);
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
				Toast.makeText(HelpBorrowActivity.this, "您成功帮助了一位同学！", Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(HelpBorrowActivity.this, "服务器错误，请稍后重试！", Toast.LENGTH_SHORT).show();
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
			submitData();
			break;

		default:
			break;
		}
	}
}
