package com.booksfloating.globalvar;

import java.util.HashMap;

import android.R.integer;

public class Constants {

	//设备类型
	public static final String DEVICE_TYPE = "Android";
	
	public static final String SERVER_IP = "http://10.170.67.215:8080/booksfloating";// 服务器ip（这里是随便写的一个地址）
	public static final int SERVER_PORT = 8080;// 服务器端口

	public static final int REGISTER_FAIL = 0;//注册失败
	public static final String SAVE_USER = "SaveUser";//保存用户信息的xml文件名
	
	//cookie
	public final static int COOKIE_VERIFY_FAIL=1111;
	public final static String COOKIE_VERIFY_FAIL_RECODE="登录验证失败";
	
	public final static HashMap<String,Integer> schoolNameMap = new HashMap<String,Integer>();
	static{
		schoolNameMap.put("西安电子科技大学雁塔校区", 1);
		schoolNameMap.put("西安电子科技大学长安校区", 2);
		schoolNameMap.put("西北工业大学友谊校区", 3);
		schoolNameMap.put("西北工业大学长安校区", 4);
		schoolNameMap.put("西安交通大学兴庆校区", 5);
		schoolNameMap.put("西安交通大学雁塔校区", 6);
		schoolNameMap.put("西安交通大学曲江校区", 7);
		schoolNameMap.put("陕西师范大学雁塔校区", 8);
		schoolNameMap.put("陕西师范大学长安校区", 9);
		schoolNameMap.put("长安大学雁塔校区", 10);
		schoolNameMap.put("长安大学渭水校区", 11);
		schoolNameMap.put("西安工业大学金花校区", 12);
		schoolNameMap.put("西安工业大学未央校区", 13);
		schoolNameMap.put("西安工业大学洪庆校区", 14);
		schoolNameMap.put("西安工程大学金花校区", 15);
		schoolNameMap.put("西安科技大学雁塔校区", 16);
	}
	
	/*编号	学校名称
	001	西安电子科技大学雁塔校区
	002	西安电子科技大学长安校区
	003	西北工业大学友谊校区
	004	西北工业大学长安校区
	005	西安交通大学兴庆校区
	006	西安交通大学雁塔校区
	007	西安交通大学曲江校区
	008	陕西师范大学雁塔校区
	009	陕西师范大学长安校区
	010	长安大学雁塔校区
	011	长安大学渭水校区
	012	西安工业大学金花校区
	013	西安工业大学未央校区
	014	西安工业大学洪庆校区
	015	西安工程大学金花校区
	016	西安科技大学雁塔校区*/

	
}
