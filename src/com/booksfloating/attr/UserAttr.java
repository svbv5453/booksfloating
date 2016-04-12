package com.booksfloating.attr;

import java.io.Serializable;
/**
 * 存储用户类，里面包括用户的所有属性，包括用户名、密码等，以及属性的get和set方法
 * @author wenyuanliu
 *@date 20160302
 */
public class UserAttr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	//用户验证码
	private String token;
	private int phoneNum;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(int phoneNum) {
		this.phoneNum = phoneNum;
	}  
	
	
}
