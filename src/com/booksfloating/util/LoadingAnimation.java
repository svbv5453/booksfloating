package com.booksfloating.util;

import android.content.Context;

import com.booksfloating.widget.MyCustomProgressDialog;

public class LoadingAnimation {
	
	private  MyCustomProgressDialog myCustomProgressDialog;
	private  Context mContext;
	public LoadingAnimation(Context context){
		this.mContext = context;
	}
	
	public  void startLoadingAnimation(){
		
		if(myCustomProgressDialog == null){
			myCustomProgressDialog = MyCustomProgressDialog.createDialog(mContext);
			myCustomProgressDialog.setMessage("正在拼命加载中...");
		}
		
		myCustomProgressDialog.show();
	}
	public  void stopLoadingAnimation(){
		if(myCustomProgressDialog != null){
			myCustomProgressDialog.dismiss();
			myCustomProgressDialog = null;
		}
	}

}
