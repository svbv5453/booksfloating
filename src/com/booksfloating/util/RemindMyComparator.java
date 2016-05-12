package com.booksfloating.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.booksfloating.domain.MyInfoPublishBookBean;

public class RemindMyComparator implements Comparator{
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		MyInfoPublishBookBean ba1 = (MyInfoPublishBookBean)lhs;
		MyInfoPublishBookBean ba2 = (MyInfoPublishBookBean)rhs;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null,date2 = null;
		try {
			date1 = sdf.parse(ba1.getBookExpirationTime());
			date2 = sdf.parse(ba2.getBookExpirationTime());
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
		return date1.compareTo(date2);
	}	
}
