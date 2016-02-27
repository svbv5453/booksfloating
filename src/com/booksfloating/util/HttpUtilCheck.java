package com.booksfloating.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtilCheck {
	
	public static void sendHttpRequest(final String address, final HttpCallBackListener listener){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("get");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					InputStream is = connection.getInputStream();
					BufferedReader buferedReader = new BufferedReader(new InputStreamReader(is));
					StringBuilder result = new StringBuilder();
					String line = "";
					while((line = buferedReader.readLine())!= null){
						result.append(line);
					}
					if(listener != null){
						listener.onFinish(result.toString());
					}
					
				
				} catch (Exception e) {
					if(listener != null){
						listener.onError(e);
					}
				}finally{
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}
	

}
