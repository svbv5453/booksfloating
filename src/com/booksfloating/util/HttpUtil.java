package com.booksfloating.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

import com.booksfloating.activity.LoginActivity;
import com.booksfloating.globalvar.Constants;
import com.xd.connect.PostParameter;

public class HttpUtil {
	public static final String UTF_8 = "UTF-8";
	public static final String POST = "POST";
	public static final String GET = "GET";
	
	private final static int READ_TIMEOUT = 20000;
	private final static int CONNECT_TIMEOUT = 20000;
	private final static int TEST_TIMEOUT = 3000;
	
	public static final String USER_LOGIN = Constants.SERVER_IP + "/login";
	public static final String USER_REGISTER = Constants.SERVER_IP+"/register";
	public static final String USER_SEARCHBOOKS = Constants.SERVER_IP+"/search";
	public static final String UPDATE_BOOKINFO = Constants.SERVER_IP+"/update";
	public static final String HELP_BORROW = Constants.SERVER_IP+"/help";
	public static final String PUBLISH_INFO = Constants.SERVER_IP+"/publish";
	public static final String BROWSE_BOOK = Constants.SERVER_IP+"/browse";
	
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
	
	/**
	 * 通用的http请求类（默认采用POST方式）
	 * 这种方式是将参数作为post的地址的参数传输的
	 * @param url 请求地址
	 * @param postParams post传递的参数
	 * @param httpMethod http的请求方法 默认采用post
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String httpRequest(String url, PostParameter[] postParams,String httpMethod) {
//		System.out.println(">>>>>>>> 开始时间 : "+new Date());
        InputStream is = null;
        String jsonStr = null;
        try {
            HttpURLConnection con = null;
            OutputStream ostream = null;
            try {
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setDoInput(true);
                if (null != postParams || POST.equals(httpMethod))
                {
                	
                   con.setRequestMethod(POST);
                   con.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					con.setRequestProperty("Charset", "UTF-8");
					con.setDoOutput(true); 
					
                   con.setReadTimeout(READ_TIMEOUT);
                   con.setConnectTimeout(CONNECT_TIMEOUT);
                   
                   String postParam = "";
                   if (postParams != null) {
                	   //将参数进行编码防止中文乱码问题
                        postParam = encodeParameters(postParams);
                   }
                   byte[] bytes = postParam.getBytes("UTF-8");
					con.setRequestProperty("Content-Length",
							Integer.toString(bytes.length));

                   System.out.println("URL地址:"+con.getURL()+"?" + postParam);
                   ostream = con.getOutputStream();
                   ostream.write(bytes);
                   ostream.flush();
                   ostream.close();
                }
				if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					is = con.getInputStream();
					jsonStr = Inputstr2Str_Reader(is, "utf-8");
					is.close();
					Log.i("return_json", jsonStr);
				}
            }
            catch (Exception e){
                e.printStackTrace();
            }finally {}
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//        	System.out.println(">>>>>>>> 结束时间 : "+new Date());
        	return jsonStr;
        }
    }

	/**
	 * 将传递的参数进行编码    
	 * @param postParams 参数数组
	 * @return String
	 */
    private static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
            	if(null!=postParams[j])
            	{
            		if(null!=postParams[j].getKey())
                	{
                		buf.append(URLEncoder.encode(postParams[j].getKey(), "utf-8")).append("=");
                	}
                	if(null!=postParams[j].getValue())
                	{
                		buf.append(URLEncoder.encode(postParams[j].getValue(), "utf-8"));
                	}
            	}
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
          }
        return buf.toString();
    }
    
    /** 
     * 利用BufferedReader实现Inputstream转换成String <功能详细描述> 
     *  
     * @param in 
     * @return String 
     */  
      
    public static String Inputstr2Str_Reader(InputStream in, String encode)  
    {  
          
        String str = "";  
        try  
        {  
            if (encode == null || encode.equals(""))  
            {  
                // 默认以utf-8形式  
                encode = "utf-8";  
            }  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));  
            StringBuffer sb = new StringBuffer();  
              
            while ((str = reader.readLine()) != null)  
            {  
                sb.append(str).append("\n");  
            }  
            return sb.toString();  
        }  
        catch (UnsupportedEncodingException e1)  
        {  
            e1.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
          
        return str;  
    }  
      
}
