package com.xd.connect;

import java.util.List;

import com.booksfloating.attr.BooksAttr.BorrowInfo;

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
