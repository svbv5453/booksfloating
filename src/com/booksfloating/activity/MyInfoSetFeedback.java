package com.booksfloating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xd.booksfloating.R;

public class MyInfoSetFeedback extends Activity{
	private EditText et_feedback = null;
	private Button btn_feedback = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myinfo_set_feedback_layout);
		
		//getActionBar().setTitle("反馈问题");
		
		et_feedback = (EditText) findViewById(R.id.et_myinfo_set_feedback);
		btn_feedback = (Button) findViewById(R.id.btn_myinfo_set_feedback);
		btn_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String et_feedbackString = et_feedback.getText().toString();
				Toast.makeText(MyInfoSetFeedback.this, "感谢您对我们的大力支持！", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MyInfoSetFeedback.this, MyInfoSet.class);
				startActivity(intent);
			}
		});
		
	}
}
