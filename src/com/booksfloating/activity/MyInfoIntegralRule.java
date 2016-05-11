package com.booksfloating.activity;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;

public class MyInfoIntegralRule extends Activity{
	private Button btn_integral_rule_sign = null;
	private long preLongTime = 0;
	private long currentTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_creditscore_integral_rule);
		Intent intent = getIntent();
		
		btn_integral_rule_sign = (Button) findViewById(R.id.btn_integral_rule_sign);
		btn_integral_rule_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharePreferenceUtil sp = new SharePreferenceUtil(MyInfoIntegralRule.this, Constants.SAVE_USER);
				
				
				if(!TextUtils.isEmpty(sp.getAccount())){
					if(preLongTime == 0){
						preLongTime = new Date(System.currentTimeMillis()).getTime();
						sp.setCreditScore(2);
						Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
						System.out.println(preLongTime);
					}else {
						currentTime = new Date(System.currentTimeMillis()).getTime();
						System.out.println("当前时间是" + currentTime);
						if((currentTime - preLongTime)/(1000*360*24) > 24){
							preLongTime = currentTime;
							sp.setCreditScore(2);
							Toast.makeText(MyInfoIntegralRule.this, "签到成功！", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(MyInfoIntegralRule.this, "今天您已经签过到，明天再来吧！", Toast.LENGTH_SHORT).show();
						}
						
					}
					
					
				}else{
					Toast.makeText(MyInfoIntegralRule.this, "您尚未登录！", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}

}
