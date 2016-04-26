package com.booksfloating.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.xd.booksfloating.R;

public class LoaderImageUseVelloy {
	
	public void LoaderLocalImage(Context context, ImageView imageView, int resId){
		
	}
	public void LoaderImage(Context context, ImageView imageView, String imageUrl){
		RequestQueue requestQueue = SingleRequestQueue.getInstance(context);
		ImageLoader imageLoader = new ImageLoader(requestQueue, new MyImageCache());
		ImageListener listener = ImageLoader.getImageListener(imageView, 0, R.drawable.default_book);
		imageLoader.get(imageUrl, listener);
	}
	class MyImageCache implements ImageCache{
		private LruCache<String, Bitmap> lruCache;

		public MyImageCache(){
			int maxSize = 10 * 1024 *1024;
			lruCache = new LruCache<String, Bitmap>(maxSize){

				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
				
			};
			
			
		}
		@Override
		public Bitmap getBitmap(String url) {
			
			return lruCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			
			lruCache.put(url, bitmap);
		}
		
	}

}
