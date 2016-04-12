package com.xd.dialog;

import com.booksfloating.util.ScreenUtil;
import com.xd.booksfloating.R;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
/**
 * 对话框汇总，全部的可复用的弹出式对话框都可在此实现
 * @author liuwenyuan
 *
 */
public class DialogFactory {

	public static void AlertDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setPositiveButton("确定", null).create().show();
	}
	
	public static Dialog creatLoadingDialog(Context context, String title){
		Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(R.layout.loading_animation);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int width = ScreenUtil.getScreenWidth(context);
		lp.width = (int)(0.6*width);
		
		TextView tv_load = (TextView)dialog.findViewById(R.id.tv_loading);
		if(title.length() > 0 && title != null){
			tv_load.setText(title);
		}
		return dialog;
	}
	
}
