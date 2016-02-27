package com.booksfloating.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingleRequestQueue {
	private static RequestQueue requestQueue;
	private SingleRequestQueue(Context context){
		requestQueue = Volley.newRequestQueue(context);
	}
	
	public static synchronized RequestQueue getInstance(Context context){
		if(requestQueue == null){
			new SingleRequestQueue(context.getApplicationContext());
		}
		return requestQueue;
	}

}
