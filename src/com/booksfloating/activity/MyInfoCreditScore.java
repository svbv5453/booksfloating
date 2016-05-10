package com.booksfloating.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xd.booksfloating.R;

public class MyInfoCreditScore extends Activity{
	
	private Button btn_integral_rule = null;
	private Button btn_back = null;
	private ProgressBar progressBar1 = null;
	private ProgressBar progressBar2 = null;
	private TextView tv_currentScore = null;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_layout);
		btn_integral_rule = (Button) findViewById(R.id.btn_myinfo_integral_rule);
		progressBar1 = (ProgressBar) findViewById(R.id.pb_myinfo_crditscore);
		progressBar2 = (ProgressBar) findViewById(R.id.pb_myinfo_crditscore2);
		tv_currentScore = (TextView) findViewById(R.id.current_score);
		
		btn_integral_rule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyInfoCreditScore.this, MyInfoIntegralRule.class);
				startActivity(intent);
				
			}
		});
		btn_back = (Button)findViewById(R.id.back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(MyInfoRemind.this, MyInfoFragment.class);
				startActivity(intent);*/
				finish();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		
			default:
				return super.onOptionsItemSelected(item);
		}
		
		
	}

}
