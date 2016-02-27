package com.booksfloating.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class LoadBookImage {
	private ImageView mImageView;
	String mUrl;
	public Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		
			if(mImageView.getTag().equals(mUrl)){
			
				mImageView.setImageBitmap((Bitmap) msg.obj);
				
			}
		}
		
	};
	
	public void showImageByThread(ImageView iamgeView, final String url){
		this.mImageView = iamgeView;
		this.mUrl = url;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmap = getImage(url);
				Message message = Message.obtain();
				message.obj = bitmap;
				mHandler.sendMessage(message);
			}
		}).start();
	}
	public Bitmap getImage(String urlString){
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream is = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
