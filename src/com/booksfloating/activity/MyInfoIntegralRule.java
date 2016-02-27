package com.booksfloating.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.xd.booksfloating.R;

public class MyInfoIntegralRule extends BaseActionBarActivity{
	private Button btn_integral_rule_sign = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_integral_rule);
		Intent intent = getIntent();
		//getActionBar().setTitle("积分规则");
		btn_integral_rule_sign = (Button) findViewById(R.id.btn_integral_rule_sign);
		btn_integral_rule_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent myInformationActivityIntent = new Intent(MyInfoIntegralRule.this, MyInfoFragment.class);
				startActivity(myInformationActivityIntent);
				Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
