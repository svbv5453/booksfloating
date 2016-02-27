package com.booksfloating.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xd.booksfloating.R;

public class MyInfoSetAbout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myinfo_set_about_layout);
		//getActionBar().setTitle("关于");
	}
}
