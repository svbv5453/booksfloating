package com.booksfloating.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.booksfloating.globalvar.Constants;

/**
 * 从服务器上解析bookAttr
 * @author wenyuanliu
 *
 */
public class ParseBooksAttrJson {

	private ArrayList<BooksAttr> booksAttrsList = new ArrayList<BooksAttr>();
	private List<String> strList = null;
	private List<BorrowInfo> borrowInfoList = new ArrayList<BorrowInfo>();
	private BorrowInfo borrowInfo = null;
	private BooksAttr booksAttr = null;
	
	public ArrayList<BooksAttr> parseBrowseNoticeList(String json){		
		try {
			JSONObject jObject = new JSONObject(json);
			String status = jObject.optString("status");
			if (status.equals("1")) {
				booksAttrsList.clear();
				JSONArray noticeListArray = jObject.optJSONArray("notice_list");
				for (int i = 0; i < noticeListArray.length(); i++) {
					booksAttr = new BooksAttr();
					borrowInfo = new BorrowInfo();
					
					JSONObject tempObject = (JSONObject) noticeListArray.get(i);
					booksAttr.setBookAuthor(tempObject.optString("author"));
					booksAttr.setBookTitle(tempObject.optString("book"));
					booksAttr.setBookImageUrl(tempObject.optString("picture"));
					booksAttr.setOrderID(tempObject.optString("orderID"));
					booksAttr.setNoticePublishTime(tempObject.optString("publish_time"));
					booksAttr.setRemark(tempObject.optString("remarks"));
					booksAttr.setBookPublisher(tempObject.optString("publisher"));
					booksAttr.setPublishDate(tempObject.optString("publishdate"));
					borrowInfo.borrowIndex = tempObject.optString("index");
					String university = tempObject.optString("university");
					//borrowInfo.borrowLoc = Constants.schoolIDtoNameMap.get(Integer.parseInt(university));
					borrowInfo.borrowLoc = university;
					booksAttr.setBorrowSchool(borrowInfo.borrowLoc);
					booksAttr.setBorrowInfo(borrowInfo);
					booksAttrsList.add(booksAttr);
				}
			}
			else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return booksAttrsList;
	}
	
	public ArrayList<BooksAttr> parseBookList(String json)
	{
		
		try {
			JSONObject jsonObj = new JSONObject(json);
			String status = jsonObj.optString("status");
			if(status.equals("1"))
			{
				booksAttrsList.clear();
				//请求成功，返回书单数组
				JSONArray bookListArray = jsonObj.optJSONArray("booklist");
				for (int i = 0; i < bookListArray.length(); i++) {
					booksAttr = new BooksAttr();					
					strList = new ArrayList<String>();
					borrowInfoList = new ArrayList<BooksAttr.BorrowInfo>();
					
					JSONObject object = (JSONObject)bookListArray.get(i);
					booksAttr.setBookTitle(object.optString("book"));
					booksAttr.setBookAuthor(object.optString("author"));
					booksAttr.setBookPublisher(object.optString("publisher"));
					booksAttr.setPublishDate(object.optString("date"));
					booksAttr.setBookImageUrl(object.optString("picture"));
					
					JSONArray universityArray = object.optJSONArray("university");
					for (int j = 0; j < universityArray.length(); j++) {
						borrowInfo = new BorrowInfo();
						JSONObject tempObject = (JSONObject)universityArray.get(j);
						strList.add(tempObject.getString("univ"));
						borrowInfo.borrowLoc = tempObject.getString("univ");
						borrowInfo.borrowIndex = tempObject.getString("index");
						borrowInfoList.add(borrowInfo);
					}
					booksAttr.setCanBorrowSchoolList(strList);
					booksAttr.setBorrowInfoList(borrowInfoList);					
					booksAttrsList.add(booksAttr);
				}
				
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
