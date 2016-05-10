package com.booksfloating.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class ImageLoader {
	private HashMap<String, SoftReference<Drawable>> imageMap;
	public ImageLoader() {
		imageMap = new HashMap<String, SoftReference<Drawable>>();
	}

	
	public void loadDrawable(final String imageUrl,
			final RequestCallback imageCallback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		boolean load = false;
		if (imageMap.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageMap.get(imageUrl);
			Drawable drawable = softReference.get();
			if (null != drawable) {
				load = false;
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			} else {
				load = true;
				imageMap.remove(imageUrl);
			}
		} else {
			load = true;
		}
		if (load) {
			new Thread() {
				@Override
				public void run() {
					Drawable drawable = loadImageFromUrl(imageUrl);
					imageMap.put(imageUrl,
							new SoftReference<Drawable>(drawable));
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				}
			}.start();
		}
	}

	public Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(i, "src");
		return drawable;
	}
	
	public interface RequestCallback {
		public void imageLoaded(Drawable drawable, String url);
	}
}
