package com.booksfloating.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
	
	public static String getJsonData(final String urlString) {
		
				try {
					URL url = new URL(urlString);
					InputStream is = url.openStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
					StringBuilder result = new StringBuilder();
					String line = "";
					while((line = bufferedReader.readLine()) != null){
						result.append(line);
					}
					return result.toString();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			
		
		return null;
	}

}
