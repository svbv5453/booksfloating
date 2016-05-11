package com.booksfloating.activity.infonotice;

import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.booksfloating.R.color;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;
import com.xd.imageloader.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这页的数据都可以从前一页中传值过来，但是要重新想服务器查询可借复本，如果可借复本为0
 * 则“去帮忙”的按钮变为灰色
 * @author wenyuanliu
 *
 */
public class BooksInfoActivity extends Activity implements OnClickListener{
	private Button btn_back, btn_gotohelp;
	private ImageView iv_books_image;
	private TextView tv_books_title, tv_books_author, tv_books_publisher,tv_books_publish_date;
	private TextView tv_info_publish_time, tv_library_location, tv_reference_number, tv_whether_borrow, tv_remarks;
	private BooksAttr booksAttr;
	private BorrowInfo borrowInfo;
	private ImageLoader imageLoader = new ImageLoader(BooksInfoActivity.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_info);
		
		booksAttr = new BooksAttr();
		borrowInfo = new BorrowInfo();
		booksAttr = (BooksAttr)getIntent().getSerializableExtra("intent_booksAttr");
		borrowInfo = booksAttr.getBorrowInfo();
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_gotohelp = (Button)findViewById(R.id.btn_gotohelp);
		btn_gotohelp.setOnClickListener(this);
		
		tv_books_title = (TextView)findViewById(R.id.tv_books_title);
		tv_books_title.setText(booksAttr.getBookTitle());
		tv_books_author = (TextView)findViewById(R.id.tv_books_author);
		tv_books_author.setText(booksAttr.getBookAuthor());
		tv_books_publisher = (TextView)findViewById(R.id.tv_books_publisher);
		tv_books_publisher.setText("出版社："+booksAttr.getBookPublisher());
		tv_books_publish_date = (TextView)findViewById(R.id.tv_books_publish_date);
		tv_books_publish_date.setText("出版时间："+booksAttr.getPublishDate());
		
		iv_books_image = (ImageView)findViewById(R.id.iv_books_image);
		if (booksAttr.getBookImageUrl() != null) {
			//ImageManager.from(this).displayImage(iv_books_image, booksAttr.getBookImageUrl(), R.drawable.default_book);
			imageLoader.DisplayImage(booksAttr.getBookImageUrl(), iv_books_image, false, R.drawable.default_book);
			
		}else {
			iv_books_image.setImageDrawable(getResources().getDrawable(R.drawable.default_book));
		}
		
		tv_info_publish_time = (TextView)findViewById(R.id.tv_info_publish_time);
		tv_info_publish_time.setText(booksAttr.getNoticePublishTime());
		
		tv_library_location = (TextView)findViewById(R.id.tv_library_location);
		tv_library_location.setText(borrowInfo.borrowLoc);
		tv_reference_number = (TextView)findViewById(R.id.tv_reference_number);
		tv_reference_number.setText(borrowInfo.borrowIndex);
		tv_remarks = (TextView)findViewById(R.id.tv_remarks);
		tv_remarks.setText(booksAttr.getRemark());
		tv_whether_borrow = (TextView)findViewById(R.id.tv_whether_borrow);
		tv_whether_borrow.setText(borrowInfo.libraryTotalBooksNum +"/"+borrowInfo.canBorrowBooksNum);
		refreshFromServer();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_gotohelp:
			Intent intent = new Intent();
			intent.setClass(BooksInfoActivity.this, HelpBorrowActivity.class);
			intent.putExtra("intent_booksAttr", booksAttr);
			startActivity(intent);
			break;
		default:
			break;
		}
	} 
	
	private String jsonString = null;
	private int total = -1;
	private int available = -1;
	private void refreshFromServer()
	{
		showLoadingDialog();
		final PostParameter[] postParameters = new PostParameter[2];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				postParameters[0] = new PostParameter("index", borrowInfo.borrowIndex);
				postParameters[1] = new PostParameter("university", borrowInfo.borrowLoc);
				System.out.println("borrowInfo.borrowLoc:"+borrowInfo.borrowLoc);
				jsonString = HttpUtil.httpRequest(HttpUtil.UPDATE_BOOKINFO, postParameters, HttpUtil.POST);
				System.out.println("jsonString"+jsonString);
				if(jsonString == null)
				{
					handler.sendEmptyMessage(-1);
				}else {
					//解析返回的json数据					
					try {
						JSONObject jObject = new JSONObject(jsonString);
						String status = jObject.getString("status");
						if(status.equals("1")){
							String totalString = jObject.getString("total");
							String availableString = jObject.getString("available");
							total = Integer.parseInt(totalString);
							available = Integer.parseInt(availableString);
							handler.sendEmptyMessage(0);
						}
						else {
							handler.sendEmptyMessage(-1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}).start();		
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dismissLoadingDialog();
			switch (msg.what) {
			case 0:
				borrowInfo.libraryTotalBooksNum = total;
				borrowInfo.canBorrowBooksNum = available;
				tv_whether_borrow.setText(borrowInfo.libraryTotalBooksNum +"/"+borrowInfo.canBorrowBooksNum);
				
				if(available == 0)
				{
					btn_gotohelp.setEnabled(false);
					btn_gotohelp.setBackgroundColor(color.gray);
				}
				break;
			case -1:
				Toast.makeText(BooksInfoActivity.this, "服务器错误，请重试！", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在查询，请稍后...");
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

}
