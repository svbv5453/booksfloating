package com.booksfloating.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;

public class ParseBooksAttrJson {

	private ArrayList<BooksAttr> booksAttrsList = new ArrayList<BooksAttr>();
	private List<String> strList = new ArrayList<String>();
	private List<BorrowInfo> borrowInfoList = new ArrayList<BorrowInfo>();
	private BorrowInfo borrowInfo = new BorrowInfo();
	private BooksAttr booksAttr;
	
	public ArrayList<BooksAttr> parseBookList(String json)
	{
		try {
			JSONObject jsonObj = new JSONObject(json);
			String status = jsonObj.optString("status");
			if(status.equals("1"))
			{
				//请求成功，返回书单数组
				JSONArray bookListArray = jsonObj.optJSONArray("booklist");
				for (int i = 0; i < bookListArray.length(); i++) {
					JSONObject object = (JSONObject)bookListArray.get(i);
					booksAttr.setBookTitle(object.optString("book"));
					booksAttr.setBookAuthor(object.optString("author"));
					booksAttr.setBookPublisher(object.optString("publisher"));
					booksAttr.setPublishDate(object.optString("date"));
					
					JSONArray universityArray = object.optJSONArray("university");
					for (int j = 0; j < universityArray.length(); j++) {
						JSONObject tempObject = (JSONObject)universityArray.get(i);
						strList.add(tempObject.getString("univ"));
						borrowInfo.borrowLoc = tempObject.getString("univ");
						borrowInfo.borrowIndex = tempObject.getString("index");
						borrowInfoList.add(borrowInfo);
					}
					booksAttr.setCanBorrowSchoolList(strList);
					
					booksAttr.setBookImageUrl(object.optString("picture"));
				}
				booksAttrsList.add(booksAttr);
			}else {
				//请求不成功
				return null;
			}
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return booksAttrsList;		
	}
}
