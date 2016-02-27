package com.booksfloating.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.xd.booksfloating.R;

public class MyInfoCreditScore extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_layout);
		//getActionBar().setTitle("信用积分");
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
		case R.id.action_integral_rule:
			Intent intent = new Intent(MyInfoCreditScore.this, MyInfoIntegralRule.class);
			startActivity(intent);
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
		
	}

}
