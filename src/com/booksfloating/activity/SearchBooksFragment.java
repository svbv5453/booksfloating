package com.booksfloating.activity;

import com.booksfloating.adapter.MySpinnerAdapter;
import com.xd.booksfloating.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class SearchBooksFragment extends Fragment{
	private Spinner spinner;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_search_books_main, container,false);		
		spinner = (Spinner)view.findViewById(R.id.spinner_schools);
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
        spinner.setSelection(items.length - 1);

	}
	
}
