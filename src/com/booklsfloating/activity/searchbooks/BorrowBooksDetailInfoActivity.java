package com.booklsfloating.activity.searchbooks;

import java.util.ArrayList;

import com.booksfloating.adapter.BorrowBookDetailInfoAdapter;
import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.booksfloating.util.ImageLoader;
import com.booksfloating.util.ImageManager;
import com.booksfloating.util.LoaderImageUseVelloy;
import com.booksfloating.util.ImageLoader.RequestCallback;
import com.xd.booksfloating.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowBooksDetailInfoActivity extends Activity implements OnClickListener{
	private Button btn_back, btn_publish_info, btn_choose_all;
	
	private TextView tv_books_title,tv_books_author,tv_books_publisher,tv_books_publish_time;
	private ImageView iv_books_image;
	private ListView lv_library_collection;
	private ArrayList<BorrowInfo> borrowInfoList = new ArrayList<BorrowInfo>();
	private BooksAttr booksAttr = new BooksAttr();
	private BorrowBookDetailInfoAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_detail_info);
		booksAttr = (BooksAttr) getIntent().getExtras().getSerializable("intent_booksAttr");
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_choose_all = (Button)findViewById(R.id.btn_choose_all);
		btn_choose_all.setOnClickListener(this);
		btn_publish_info = (Button)findViewById(R.id.btn_publish_info);
		btn_publish_info.setOnClickListener(this);
				
		tv_books_title = (TextView)findViewById(R.id.tv_books_title);
		tv_books_author = (TextView)findViewById(R.id.tv_books_author);
		tv_books_publisher = (TextView)findViewById(R.id.tv_books_publisher);
		tv_books_publish_time = (TextView)findViewById(R.id.tv_books_publish_time);
		
		tv_books_title.setText(booksAttr.getBookTitle());
		tv_books_author.setText(booksAttr.getBookAuthor());
		tv_books_publisher.setText("出版社："+booksAttr.getBookPublisher());
		tv_books_publish_time.setText("出版时间："+booksAttr.getPublishDate());
		System.out.println("booksAttr.getPublishDate():"+booksAttr.getPublishDate());
		
		iv_books_image = (ImageView)findViewById(R.id.iv_books_image);
		if(booksAttr.getBookImageUrl() != null)
		{
			ImageManager.from(this).displayImage(iv_books_image, booksAttr.getBookImageUrl(), R.drawable.default_book);
			
		}else {
			iv_books_image.setImageDrawable(getResources().getDrawable(R.drawable.default_book));
		}
		
		lv_library_collection = (ListView)findViewById(R.id.lv_library_collection);
		borrowInfoList.clear();
		borrowInfoList.addAll(booksAttr.getBorrowInfoList());
		for (int i = 0; i < booksAttr.getBorrowInfoList().size(); i++) {
			System.out.println("borrowInfoList-------->"+borrowInfoList.get(i).borrowLoc);
		}
		adapter = new BorrowBookDetailInfoAdapter(this, borrowInfoList);
		lv_library_collection.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {		
		case R.id.btn_back:
			
			break;
		//如果是全选，则直接把booksAttr传到下一个页面即可
		case R.id.btn_choose_all:
			/*Intent intent1 = new Intent();
			intent1.setClass(this, PublishInfoActivity.class);
			intent1.putExtra("intent_booksAttr", booksAttr);
			startActivity(intent1);	*/	
			break;
		//需要先统计哪些checkbox被选中
		case R.id.btn_publish_info:
			if (calculateCheck()) {
				//将数据传递到下一页
				Intent intent = new Intent();
				intent.setClass(this, PublishInfoActivity.class);
				intent.putExtra("intent_booksAttr", booksAttr);
				intent.putExtra("intent_borrowInfoList", deliverBorrowInfoList);
				startActivity(intent);
			}	
			else {
				Toast.makeText(this, "请选择借书的学校！", Toast.LENGTH_LONG).show();
			}
			
			break;
		default:
			break;
		}
	}
	
	private ArrayList<BorrowInfo> deliverBorrowInfoList = new ArrayList<BorrowInfo>();
	private boolean calculateCheck()
	{
		for (int i = 0; i < borrowInfoList.size(); i++) {
			if (BorrowBookDetailInfoAdapter.getSelected().get(i)) {
				deliverBorrowInfoList.add(borrowInfoList.get(i));
			}
		}
		System.out.println("deliverBorrowInfoList.size():"+deliverBorrowInfoList.size());
		if(deliverBorrowInfoList.size() == 0){			
			return false;
		}
		return true;
	}

}
