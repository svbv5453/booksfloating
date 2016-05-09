package com.booksfloating.globalvar;

import java.util.HashMap;

import android.R.integer;

public class Constants {

	//设备类型
	public static final String DEVICE_TYPE = "Android";
	public static boolean isLogin = false;
	public static final int OK = 0,SERVER_ERROR = -1, NETWORK_ERROR = -2, NULL_ERROR = -3,USER_ERROR = -4;
	public static final int SEARCH_KEYWORD = 5;
	
	public static final String SERVER_IP = "http://10.170.56.249:8080/booksfloating";// 服务器ip（这里是随便写的一个地址）
	public static final int SERVER_PORT = 8080;// 服务器端口

	public static final int REGISTER_FAIL = 0;//注册失败
	public static final String SAVE_USER = "SaveUser";//保存用户信息的xml文件名
	
	//cookie
	public final static int COOKIE_VERIFY_FAIL=1111;
	public final static String COOKIE_VERIFY_FAIL_RECODE="登录验证失败";
	
	//默认图片
	public final static String DEFAULT_BOOK_IMAGE= "default_book.png";
	
	//public final static HashMap<String,Integer> schoolNameMap = new HashMap<String,Integer>();
	/*static{
		schoolNameMap.put("西安电子科技大学", 1);
		schoolNameMap.put("西安电子科技大学雁塔校区", 101);
		schoolNameMap.put("西安电子科技大学长安校区", 102);
		schoolNameMap.put("西北工业大学", 2);
		schoolNameMap.put("西北工业大学友谊校区", 202);
		schoolNameMap.put("西北工业大学长安校区", 203);
		schoolNameMap.put("西安交通大学", 3);
		schoolNameMap.put("西安交通大学兴庆校区", 301);
		schoolNameMap.put("西安交通大学雁塔校区", 302);
		schoolNameMap.put("西安交通大学曲江校区", 303);
		schoolNameMap.put("陕西师范大学", 4);
		schoolNameMap.put("陕西师范大学雁塔校区", 401);
		schoolNameMap.put("陕西师范大学长安校区", 402);
		schoolNameMap.put("长安大学", 5);
		schoolNameMap.put("长安大学雁塔校区", 501);
		schoolNameMap.put("长安大学渭水校区", 502);
		schoolNameMap.put("西安工业大学", 6);
		schoolNameMap.put("西安工业大学金花校区", 601);
		schoolNameMap.put("西安工业大学未央校区", 602);
		schoolNameMap.put("西安工业大学洪庆校区", 603);
		schoolNameMap.put("西安工程大学", 7);
		schoolNameMap.put("西安工程大学金花校区", 701);
		schoolNameMap.put("西安科技大学", 8);
		schoolNameMap.put("西安科技大学雁塔校区", 801);
		schoolNameMap.put("所有学校", 0);
	}*/
	//public final static HashMap<Integer,String> schoolIDtoNameMap = new HashMap<Integer,String>();
	/*static{
		schoolIDtoNameMap.put(1,"西安电子科技大学");
		schoolIDtoNameMap.put(101,"西安电子科技大学雁塔校区");
		schoolIDtoNameMap.put(102,"西安电子科技大学长安校区");
		schoolIDtoNameMap.put(2,"西北工业大学");
		schoolIDtoNameMap.put(202,"西北工业大学友谊校区");
		schoolIDtoNameMap.put(203,"西北工业大学长安校区");
		schoolIDtoNameMap.put(3,"西安交通大学");
		schoolIDtoNameMap.put(301,"西安交通大学兴庆校区");
		schoolIDtoNameMap.put(302,"西安交通大学雁塔校区");
		schoolIDtoNameMap.put(303,"西安交通大学曲江校区");
		schoolIDtoNameMap.put(4,"陕西师范大学");
		schoolIDtoNameMap.put(401,"陕西师范大学雁塔校区");
		schoolIDtoNameMap.put(402,"陕西师范大学长安校区");
		schoolIDtoNameMap.put(5,"长安大学");
		schoolIDtoNameMap.put(501,"长安大学雁塔校区");
		schoolIDtoNameMap.put(502,"长安大学渭水校区");
		schoolIDtoNameMap.put(6,"西安工业大学");
		schoolIDtoNameMap.put(601,"西安工业大学金花校区");
		schoolIDtoNameMap.put(602,"西安工业大学未央校区");
		schoolIDtoNameMap.put(603,"西安工业大学洪庆校区");
		schoolIDtoNameMap.put(7,"西安工程大学");
		schoolIDtoNameMap.put(701,"西安工程大学金花校区");
		schoolIDtoNameMap.put(8,"西安科技大学");
		schoolIDtoNameMap.put(801,"西安科技大学雁塔校区");
		schoolIDtoNameMap.put(0,"所有学校");
	}*/
	/*编号	学校名称
	1        西安电子科技大学
	101	西安电子科技大学雁塔校区
	102	西安电子科技大学长安校区
	2        西北工业大学
	201	西北工业大学友谊校区
	202	西北工业大学长安校区
	3        西安交通大学
	301	西安交通大学兴庆校区
	302	西安交通大学雁塔校区
	303	西安交通大学曲江校区
	4        陕西师范大学
	401	陕西师范大学雁塔校区
	402	陕西师范大学长安校区
	5        长安大学
	501	长安大学雁塔校区
	502	长安大学渭水校区
	6        西安工业大学
	601	西安工业大学金花校区
	602	西安工业大学未央校区
	603	西安工业大学洪庆校区
	7        西安工程大学
	701	西安工程大学金花校区
	8        西安科技大学
	801	西安科技大学雁塔校区
	0       所有学校
	*/

	
}
