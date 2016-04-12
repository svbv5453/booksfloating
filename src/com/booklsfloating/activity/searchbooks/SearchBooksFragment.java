package com.booklsfloating.activity.searchbooks;

import com.booksfloating.activity.LoginActivity;
import com.booksfloating.adapter.MySpinnerAdapter;
import com.xd.booksfloating.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

public class SearchBooksFragment extends Fragment implements OnClickListener{
	private Spinner spinner;
	private View view;
	private Button btn_login,btn_search_books;
	Intent intent = null;
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
		
		initView();
		return view;
	}

	public void initView()
	{	
		// 建立数据源
		String[] items = getResources().getStringArray(R.array.spinner_item);
		// 建立Adapter并且绑定数据源,其中android.R.layout.simple_spinner_item为系统内置的样式
		MySpinnerAdapter adapter = new MySpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, items);
		//设置展开的时候下拉菜单的样式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
        spinner.setSelection(0);
        
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
			intent = new Intent(getActivity(),SearchBooksDetailActivity.class);
			getActivity().startActivity(intent);
		default:
			break;
		}
	}
	
}
