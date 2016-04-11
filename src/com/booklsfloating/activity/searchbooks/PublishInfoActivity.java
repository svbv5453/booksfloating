package com.booklsfloating.activity.searchbooks;

import java.util.ArrayList;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.xd.booksfloating.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PublishInfoActivity extends Activity implements OnClickListener{
	private Button btn_back, btn_sure, btn_direct_release;
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
		//这里应该还要判断是否有这个值传过来
		borrowInfoList = (ArrayList<BorrowInfo>) getIntent().getExtras().getSerializable("intent_borrowInfoList");
		
		btn_back = (Button)findViewById(R.id.btn_back);
		btn_sure = (Button)findViewById(R.id.btn_sure);
		btn_direct_release = (Button)findViewById(R.id.btn_direct_release);
		
		btn_back.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
		btn_direct_release.setOnClickListener(this);
		
		et_remark = (EditText)findViewById(R.id.et_remark);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
