package com.xd.connect;
/**
 * HTTP请求中的参数
 * @author liuwenyuan
 *@date 20160228
 */
public class PostParameter {
	private String key;
	private String value;
	
	public PostParameter(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
