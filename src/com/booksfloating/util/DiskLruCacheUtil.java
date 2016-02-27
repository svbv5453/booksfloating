package com.booksfloating.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import libcore.io.DiskLruCache;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
//缓存图片，不用该类了；
public class DiskLruCacheUtil {
	
	DiskLruCache myDiskLruCache;
	
	
	
	
	
	public void writeDiskLurCache(Context context, final String imageUrl){
		
		File cacheDir = getDiskCacheDir(context, "Bitmap");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		int appVersion = getAppVersion(context);
		try {
			myDiskLruCache = DiskLruCache.open(cacheDir, appVersion, 1, 10*1024*1024);
			/**
			 * 第一个参数是数据的缓存地址；
			 * 第二个参数是指定当前应用程序的版本号；
			 * 第三个参数是指定同一个key可以对应多少个缓存文件；
			 * 第四个参数是指定最多可以缓存多少字节的数据；
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				
				String key = hashKeyForDisk(imageUrl);
				try {
					DiskLruCache.Editor editor = myDiskLruCache.edit(key);
					if(editor != null){
						OutputStream outputStream = editor.newOutputStream(0);
						if(downloadUrlToStream(imageUrl, outputStream)){
							editor.commit();//出错了
						}else{
							editor.abort();
						}
					}
					myDiskLruCache.flush();
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}

		}).start();
		
		
		
	}
	public void readDiskLruCache(ImageView imageView, String imageUrl){
		
		String key = hashKeyForDisk(imageUrl);
		try {
			DiskLruCache.Snapshot snapShot = myDiskLruCache.get(key);
			if(snapShot != null){
				InputStream inputStream = snapShot.getInputStream(0);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				imageView.setImageBitmap(bitmap);
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected boolean downloadUrlToStream(String imageUrl,
			OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			URL url = new URL(imageUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = urlConnection.getInputStream();
			in = new BufferedInputStream(inputStream, 8*1024);
			out = new BufferedOutputStream(outputStream, 8*1024);
			int b;
			while((b = in.read()) != -1){
				out.write(b);
			}
			return true;
			
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			
				try {
					if(out != null){
						out.close();
					}
					if(in != null){
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(urlConnection != null){
				urlConnection.disconnect();
			}
		
		return false;
		
	}
	protected String hashKeyForDisk(String imageUrl) {
		String cacheKey;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(imageUrl.getBytes());
			byte[] urlKey = md.digest();
			cacheKey = bytesToHexString(urlKey);
			
			
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(imageUrl.hashCode());
		}
		
		
		return cacheKey;
		
	}
	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i++){
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if(hex.length() == 1){
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	public File getDiskCacheDir(Context context, String uniqueName){
		
		String cachePath;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory()) || !Environment.isExternalStorageRemovable()){
			cachePath = context.getExternalCacheDir().getPath();
		}else{
			cachePath = context.getCacheDir().getPath();
		}
		
		return new File(cachePath + File.separator + uniqueName);
		
	}
	public int getAppVersion(Context context){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
