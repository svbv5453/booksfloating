package com.booksfloating.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.booksfloating.attr.BooksAttr;

public class MyComparator implements Comparator{
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		BooksAttr ba1 = (BooksAttr)lhs;
		BooksAttr ba2 = (BooksAttr)rhs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null,date2 = null;
		try {
			date1 = sdf.parse(ba1.getNoticePublishTime());
			date2 = sdf.parse(ba2.getNoticePublishTime());
			if(date1==null){
				return 1;
			}
			if(date2==null){
				return -1;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date2.compareTo(date1);
	}	
}
