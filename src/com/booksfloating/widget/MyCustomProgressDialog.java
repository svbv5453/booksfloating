
/**************************************************************************************
* [Project]
*       MyProgressDialog
* [Package]
*       com.lxd.widgets
* [FileName]
*       CustomProgressDialog.java
* [Copyright]
*       Copyright 2012 LXD All Rights Reserved.
* [History]
*       Version          Date              Author                        Record
*--------------------------------------------------------------------------------------
*       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
**************************************************************************************/
	
package com.booksfloating.widget;




import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.xd.booksfloating.R;




public class MyCustomProgressDialog extends Dialog {
	private Context context;
	private static MyCustomProgressDialog customProgressDialog = null;
	
	public MyCustomProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public MyCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	public static MyCustomProgressDialog createDialog(Context context){
		customProgressDialog = new MyCustomProgressDialog(context,R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		
		return customProgressDialog;
	}
 
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	if (customProgressDialog == null){
    		return;
    	}
    	
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }
 
    /**
     * 
     * [Summary]
     *       setTitile
     * @param strTitle
     * @return
     *
     */
    public MyCustomProgressDialog setTitile(String strTitle){
    	return customProgressDialog;
    }
    
    /**
     * 
     * [Summary]
     *       setMessage 
     * @param strMessage
     * @return
     *
     */
    public MyCustomProgressDialog setMessage(String strMessage){
    	TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
    	
    	if (tvMsg != null){
    		tvMsg.setText(strMessage);
    	}
    	
    	return customProgressDialog;
    }
}
