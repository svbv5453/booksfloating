package com.booksfloating.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xd.booksfloating.R;
/**
 * 
 * @author SunHen;
 *
 */
public class MyInfoSetVersionCheck extends Activity{
	private Button btn_back = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_version_check_layout);
		//getActionBar().setTitle("版本检查");
		btn_back = (Button)findViewById(R.id.back);
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intentBack = new Intent(MyInfoPublish.this, MyInfoFragment.class);
				startActivity(intentBack);*/
				finish();
				
			}
		});
	}

}
