package com.booksfloating.util;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.xd.booksfloating.R;
//缓存图片，不用该类了
public class LoaderBigImage {
	LruCache<String, Bitmap> myMemoryCache;
	public void test(){
		int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
		int cacheSize = maxMemory / 8;
		myMemoryCache = new LruCache<String, Bitmap>(cacheSize){

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				
				return bitmap.getByteCount()/1024;
			}
			
		};
		
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap){
		
		if(getBitmapFromMemoryCache(key) == null){
			myMemoryCache.put(key, bitmap);
		}
	}
	public Bitmap getBitmapFromMemoryCache(String key){
		
		return myMemoryCache.get(key);
	}
	
	public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeResource(res, resId, options);
		
	}

	private static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		final int width = options.outWidth;
		final int height = options.outHeight;
		int inSampleSize = 1;
		if(height > reqHeight || width > reqWidth){
			final int heightRatio = Math.round((float)height/(float)reqHeight);
			final int widthRatio = Math.round((float)width/(float)reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			//选择宽和高的最小比率作为inSampleSize的值，保证最终图片的宽和高一定会大于等于目标宽和高；
		}
		
		return inSampleSize;
	}
	
	public void loadBitmap(int resId, ImageView imageView){
		
		String imageKey = String.valueOf(resId);
		Bitmap bitmap = getBitmapFromMemoryCache(imageKey);
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
		}else{
			imageView.setImageResource(R.drawable.dog);
			BitmapWorkerTask task = new BitmapWorkerTask();
			task.execute(resId);
		}
		
	}
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(Integer... params) {
			Bitmap bitmap = decodeBitmapFromResource(null, params[0], 100, 100);//此处有错误
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}
		
	}
	

}
