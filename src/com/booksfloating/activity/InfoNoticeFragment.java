package com.booksfloating.activity;

import com.xd.booksfloating.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class InfoNoticeFragment extends Fragment{
	private View view;
	private ListView lv_info_notice;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.activity_info_notice_main, container, false);
		lv_info_notice = (ListView)view.findViewById(R.id.lv_info_notice);
		return view;
	}
	
	private void initView()
	{
		
	}

}
