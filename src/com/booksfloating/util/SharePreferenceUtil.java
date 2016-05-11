package com.booksfloating.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 存储用户基本信息以及一些配置信息
 * @author liuwenyuan
 * @date 20160227
 *
 */
public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	public SharePreferenceUtil(Context context, String file){
		//通过getSharedPreferences函数获得SharedPreference对象
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		//通过.edit()函数获得Editor对象
		editor = sp.edit();
	}
	
	//设置用户账号
	public void setAccount(String account){
		editor.putString("account", account);
		editor.commit();
	}
	public String getAccount(){
		return sp.getString("account", "");
	}
	
	//用户密码
	public void setPassword(String password) {
		editor.putString("password", password);
		editor.commit();
	}
	public String getPassword() {
		return sp.getString("password", "");
	}
	
	// 用户的id
	public void setUserId(String id) {
		editor.putString("userid", id);
		editor.commit();
	}
	public String getUserId() {
		return sp.getString("userid", "");
	}
	
	// 用户的Cookie
	public String getCookie() {
		return sp.getString("cookie", "");
	}
	public void setCookie(String cookie) {
		editor.putString("cookie", cookie);
		editor.commit();
	}
	
	//用户是否登录身份验证token，在每次用户登录的时候更新token
	//用户退出的时候要注销token
	public String getToken(){
		return sp.getString("token", "");
	}
	public void setToken(String token){
		editor.putString("token", token);
		editor.commit();
	}
	
	// 是否第一次运行本应用
	public void setIsFirst(boolean isFirst) {
		editor.putBoolean("isFirst", isFirst);
		editor.commit();
	}
	public boolean getisFirst() {
		return sp.getBoolean("isFirst", true);
	}
	
	// 是否继续自动检查更新
	public void setIsAutoCheckUpdate(boolean isAutoCheckUpdate)
	{
		editor.putBoolean("isAutoCheckUpdate", isAutoCheckUpdate);
		editor.commit();
	}
	public Boolean isAutoCheckUpdate()
	{
		return sp.getBoolean("isAutoCheckUpdate", true);
	}
	//用户所在学校
	public void setUserUniversity(String university){
		editor.putString("university", university);
		editor.commit();
	}
	public String getUserUniversity(){
		return sp.getString("university", "");
		
	}
	//信用积分
	public void setCreditScore(int number){
		int temp = getCreditScroe();
		editor.putInt("creditScore", temp + number);
		editor.commit();
	}
	public int getCreditScroe(){
		return sp.getInt("creditScore", 0);
	}
}
