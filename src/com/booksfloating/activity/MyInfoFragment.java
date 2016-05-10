package com.booksfloating.activity;

import com.xd.booksfloating.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @author shenhen
 * Modify by liuwenyuan
 *
 */
public class MyInfoFragment extends Fragment implements OnClickListener{
	private Button btn_my_info_public = null;
	private Button btn_my_info_order = null;
	private Button btn_my_info_remind = null;
	private Button btn_my_info_creditScore = null;
	private Button btn_my_info_set = null;
	private Button btn_my_info_exit = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_info, container, false);
		btn_my_info_public = (Button) view.findViewById(R.id.btn_my_info_publish);
		btn_my_info_order = (Button) view.findViewById(R.id.btn_my_info_order);
		btn_my_info_remind = (Button) view.findViewById(R.id.btn_my_info_remind);
		btn_my_info_creditScore = (Button) view.findViewById(R.id.btn_my_info_creditScore);
		btn_my_info_set = (Button) view.findViewById(R.id.btn_my_info_set);
		btn_my_info_exit = (Button) view.findViewById(R.id.btn_my_info_exit);
		
		btn_my_info_public.setOnClickListener(this);
		btn_my_info_order.setOnClickListener(this);
		btn_my_info_remind.setOnClickListener(this);
		btn_my_info_creditScore.setOnClickListener(this);
		btn_my_info_set.setOnClickListener(this);
		btn_my_info_exit.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_my_info_publish:
			Intent publicIntent = new Intent(getActivity(), MyInfoPublish.class);
			startActivity(publicIntent);
			break;
		case R.id.btn_my_info_order:
			Intent orderIntent = new Intent(getActivity(), MyInfoOrderFragmentActivity.class);
			startActivity(orderIntent);
			break;
		case R.id.btn_my_info_remind:
			Intent remindIntent = new Intent(getActivity(), MyInfoRemind.class);
			startActivity(remindIntent);
			break;
		case R.id.btn_my_info_creditScore:
			Intent creditScoreIntent = new Intent(getActivity(), MyInfoCreditScore.class);
			startActivity(creditScoreIntent);
			break;
		case R.id.btn_my_info_set:
			Intent setIntent = new Intent(getActivity(), MyInfoSet.class);
			startActivity(setIntent);
			break;
		case R.id.btn_my_info_exit:
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("退出");
			dialog.setMessage("退出后您将不能借书，确定退出吗？");
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), "您已退出", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			dialog.create();
			dialog.show();
			
			
			
		default:
				break;
		}
		
	}
}
