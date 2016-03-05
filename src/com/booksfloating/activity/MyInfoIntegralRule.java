package com.booksfloating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.xd.booksfloating.R;

public class MyInfoIntegralRule extends Activity{
	private Button btn_integral_rule_sign = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_integral_rule);
		Intent intent = getIntent();
		
		btn_integral_rule_sign = (Button) findViewById(R.id.btn_integral_rule_sign);
		btn_integral_rule_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
