package com.booksfloating.activity;



import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;


public class BaseActionBarActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			//返回有问题！
			/*Intent upIntent = NavUtils.getParentActivityIntent(this);
			if(NavUtils.shouldUpRecreateTask(this, upIntent)){
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			}else{
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}*/
			return true;
			
		
		}
		return super.onOptionsItemSelected(item);
		
		
	}

}
