package com.booksfloating.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.booksfloating.globalvar.Constants;
import com.booksfloating.util.SharePreferenceUtil;
import com.xd.booksfloating.R;
/**
 * 
 * @author shenhen
 * Modify by liuwenyuan
 *
 */
public class MyInfoFragment extends Fragment implements OnClickListener{
	private static String TAG = "MyInfoFragment";
	private Button btn_my_info_public = null;
	private Button btn_my_info_order = null;
	private Button btn_my_info_remind = null;
	private Button btn_my_info_creditScore = null;
	private Button btn_my_info_set = null;
	private Button btn_my_info_exit = null;
	
	private ImageView iv_myinfo_photo = null;
	private TextView tv_user = null;
	private TextView tv_user_school = null;
	private TextView tv_user_ranking = null;
	
	private LinearLayout ll_myinfo = null;
	private Button btn_goLogin = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "--------onCreateView()");
		View view = inflater.inflate(R.layout.my_info, container, false);
		btn_my_info_public = (Button) view.findViewById(R.id.btn_my_info_publish);
		btn_my_info_order = (Button) view.findViewById(R.id.btn_my_info_order);
		btn_my_info_remind = (Button) view.findViewById(R.id.btn_my_info_remind);
		btn_my_info_creditScore = (Button) view.findViewById(R.id.btn_my_info_creditScore);
		btn_my_info_set = (Button) view.findViewById(R.id.btn_my_info_set);
		btn_my_info_exit = (Button) view.findViewById(R.id.btn_my_info_exit);
		
		btn_goLogin = (Button) view.findViewById(R.id.btn_goLogin);
		ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
		
		iv_myinfo_photo = (ImageView) view.findViewById(R.id.iv_my_info_image);
		tv_user = (TextView) view.findViewById(R.id.tv_my_info_name);
		tv_user_school = (TextView) view.findViewById(R.id.tv_my_info_university);
		tv_user_ranking = (TextView) view.findViewById(R.id.tv_my_info_creditRating);
		
		btn_my_info_public.setOnClickListener(this);
		btn_my_info_order.setOnClickListener(this);
		btn_my_info_remind.setOnClickListener(this);
		btn_my_info_creditScore.setOnClickListener(this);
		btn_my_info_set.setOnClickListener(this);
		btn_my_info_exit.setOnClickListener(this);
		
		btn_goLogin.setOnClickListener(this);
		
		getUserInfo();
		
		
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_goLogin:
			Intent goLoginIntent = new Intent(getActivity(), LoginActivity.class);
			startActivity(goLoginIntent);
			
			break;
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
			SharePreferenceUtil sp1 = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
			if(!sp1.getToken().isEmpty()){
				AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setTitle("退出");
				dialog.setMessage("退出后您将不能借书，确定退出吗？");
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharePreferenceUtil sp2 = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
						sp2.setToken("");
						Toast.makeText(getActivity(), "您已退出", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						ll_myinfo.setVisibility(View.GONE);
						btn_goLogin.setVisibility(View.VISIBLE);
						
						
						
						
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
			}else{
				Toast.makeText(getActivity(), "请登录！", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
		default:
				break;
		}
		
	}
	public void getUserInfo(){
		SharePreferenceUtil sp = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
		if(!TextUtils.isEmpty(sp.getToken())){
			btn_goLogin.setVisibility(View.GONE);
			ll_myinfo.setVisibility(View.VISIBLE);
			tv_user.setText(sp.getAccount());
			tv_user_ranking.setText("信用等级：普通用户");
			
			
		}
		if(sp.getCreditScroe()/5 > 200){
			tv_user_ranking.setText("信用等级：金牌用户");
		}else if(sp.getCreditScroe()/5 > 100){
			tv_user_ranking.setText("信用等级：银牌用户");
		}else if(sp.getCreditScroe()/5 > 0){
			tv_user_ranking.setText("信用等级：普通用户");
		}
		if(!TextUtils.isEmpty(sp.getUserUniversity())){
			tv_user_school.setText(sp.getUserUniversity());
			
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "--------onResume()");
		getUserInfo();
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "--------onActivityCreated()");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "--------onActivityResult()");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d(TAG, "--------onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "--------onCreate()");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "--------onDestroy()");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "--------onDestroyView()");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG, "--------onDetach()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "--------onPause()");
	}

	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "--------onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "--------onStop()");
	}
	
	
}
