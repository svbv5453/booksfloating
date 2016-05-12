package com.booksfloating.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.booksfloating.domain.MyInfoBookDetailBean;

public class SHMyComparator implements Comparator{
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		MyInfoBookDetailBean ba1 = (MyInfoBookDetailBean)lhs;
		MyInfoBookDetailBean ba2 = (MyInfoBookDetailBean)rhs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null,date2 = null;
		try {
			date1 = sdf.parse(ba1.getBookPublicshTime());
			date2 = sdf.parse(ba2.getBookPublicshTime());
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
