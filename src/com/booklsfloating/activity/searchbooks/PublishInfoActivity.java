package com.booklsfloating.activity.searchbooks;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.activity.LoginActivity;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PublishInfoActivity extends Activity implements OnClickListener{
	private Button btn_back, btn_sure, btn_direct_release,btn_login;
	private EditText et_remark;
	private BooksAttr booksAttr;
	private ArrayList<BorrowInfo> borrowInfoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		borrowInfoList = new ArrayList<BooksAttr.BorrowInfo>();
		booksAttr = new BooksAttr();
		setContentView(R.layout.activity_release_information);
		
		booksAttr = (BooksAttr) getIntent().getExtras().getSerializable("intent_booksAttr");
		borrowInfoList = (ArrayList<BorrowInfo>) getIntent().getExtras().getSerializable("intent_borrowInfoList");
		//要把这个infoList重新放到booksAttr中
		booksAttr.setBorrowInfoList(borrowInfoList);
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_sure = (Button)findViewById(R.id.btn_sure);
		btn_direct_release = (Button)findViewById(R.id.btn_direct_release);
		
		btn_back.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		btn_direct_release.setOnClickListener(this);
		
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		et_remark = (EditText)findViewById(R.id.et_remark);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_sure:
			if (et_remark.getText().toString() != null && et_remark.getText().toString().length() != 0) {
				booksAttr.setRemark(et_remark.getText().toString());
				if (Constants.isLogin) {
					for (int i = 0; i < booksAttr.getBorrowInfoList().size(); i++) {
						submitDataToServer(booksAttr.getBorrowInfoList().get(i));
						SystemClock.sleep(1000);
					}
					
				}else {
					DialogFactory.AlertDialog(PublishInfoActivity.this, "提示", "您还未登录，请先登录！");
				}				
			}
			else {
				DialogFactory.AlertDialog(this, "提示！", "你没有填写备注，如果不需要备注，请点击“直接发布”按钮吧");
			}
			break;
			
		case R.id.btn_direct_release:
			if (Constants.isLogin) {
				System.out.println("booksAttr.getBorrowInfoList().size():"+booksAttr.getBorrowInfoList().size());
				for (int i = 0; i < booksAttr.getBorrowInfoList().size(); i++) {
					submitDataToServer(booksAttr.getBorrowInfoList().get(i));
					SystemClock.sleep(1000);
				}				
			}else {
				DialogFactory.AlertDialog(PublishInfoActivity.this, "提示", "您还未登录，请先登录！");
			}			
			break;
		case R.id.btn_login:
			Intent intent3 = new Intent();
			intent3.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(intent3);			
			break;
		case R.id.btn_back:
			finish();
			break;
			
		default:
			break;
		}
	}
		
	String status = null;
	String jsonString = null;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.OK:
				//解析json数据
				try {
					JSONObject jObject = new JSONObject(jsonString);
					status = jObject.getString("status");
					if (status.equals("1")) {
						Toast.makeText(PublishInfoActivity.this, "信息发布成功！", Toast.LENGTH_SHORT).show();
						SystemClock.sleep(Toast.LENGTH_SHORT);
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), MainActivity.class);
						intent.putExtra("intent_fragmentId", MainActivity.TAB_INFO_NOTICE);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
						
					}else {
						Toast.makeText(PublishInfoActivity.this, "信息发布失败，您可以选择重新发布！", Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;
			case Constants.NULL_ERROR:
				Toast.makeText(PublishInfoActivity.this, "服务器错误，请稍后重试", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};

	//这里发布信息可能发布多条数据
	private void submitDataToServer(final BorrowInfo borrowInfo){
		final PostParameter[] postParameters = new PostParameter[9];
		final SharePreferenceUtil sp = new SharePreferenceUtil(PublishInfoActivity.this, Constants.SAVE_USER);
		
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				postParameters[0] = new PostParameter("book", booksAttr.getBookTitle());
				postParameters[1] = new PostParameter("author", booksAttr.getBookAuthor());
				postParameters[2] = new PostParameter("university", borrowInfo.borrowLoc);
				postParameters[3] = new PostParameter("index", borrowInfo.borrowIndex);
				postParameters[4] = new PostParameter("remarks", booksAttr.getRemark());
				postParameters[5] = new PostParameter("publishdate", booksAttr.getPublishDate());
				postParameters[6] = new PostParameter("publisher", booksAttr.getBookPublisher());
				postParameters[7] = new PostParameter("borrower", sp.getAccount());
				postParameters[8] = new PostParameter("token", sp.getToken());
								
				jsonString = HttpUtil.httpRequest(HttpUtil.PUBLISH_INFO, postParameters, HttpUtil.POST);
				if (jsonString != null) {
					handler.sendEmptyMessage(Constants.OK);
				}
				else if(jsonString == null)
				{
					handler.sendEmptyMessage(Constants.NULL_ERROR);					
				
				}
			}
		}).start();
	}

}
