package com.booklsfloating.activity.searchbooks;

import com.booksfloating.activity.LoginActivity;
import com.booksfloating.adapter.MySpinnerAdapter;
import com.booksfloating.globalvar.Constants;
import com.xd.booksfloating.R;
import com.xd.dialog.DialogFactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchBooksFragment extends Fragment implements OnClickListener,OnItemSelectedListener{
	private Spinner spinner;
	private View view;
	private Button btn_login,btn_search_books;
	private EditText et_search_books;
	private Intent intent = null;
	private String university = "所有学校";
	private String[] items;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_search_books_main, container,false);		
		spinner = (Spinner)view.findViewById(R.id.spinner_schools);
		btn_login = (Button)view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_search_books = (Button)view.findViewById(R.id.btn_search_books);
		btn_search_books.setOnClickListener(this);
		et_search_books = (EditText)view.findViewById(R.id.et_search_books);
		initView();
		return view;
	}

	public void initView()
	{	
		// 建立数据源
		items = getResources().getStringArray(R.array.spinner_item);
		// 建立Adapter并且绑定数据源,其中android.R.layout.simple_spinner_item为系统内置的样式
		MySpinnerAdapter adapter = new MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, items);
		//设置展开的时候下拉菜单的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.btn_search_books:
			if(et_search_books.getText().toString() != null && et_search_books.getText().toString().length() > 0)
			{
				String keyword = et_search_books.getText().toString();
				intent = new Intent(getActivity(),SearchBooksDetailActivity.class);
				intent.putExtra("intent_keyword", keyword);
				intent.putExtra("intent_university", university);
				getActivity().startActivity(intent);
			}
			else {
				DialogFactory.AlertDialog(getActivity(), "提示", "请输入查询词！");
			}
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String univ = items[position];
		
		university = univ;
		System.out.println("选择的学校："+university);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
}
