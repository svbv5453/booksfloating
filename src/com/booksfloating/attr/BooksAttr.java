package com.booksfloating.attr;

import java.io.Serializable;
import java.util.List;

import android.R.integer;

public class BooksAttr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//书名
	private String bookTitle;
	//书的作者
	private String bookAuthor;
	//可以借到这本书的学校，这里只记录了学校
	private List<String> canBorrowSchoolList;
	//书的缩略图
	private String bookImageUrl;
	//本地图片资源
	private int resImageId;
	//出版社
	private String bookPublisher;
	//可以借到这本书的信息列表，BorrowInfo中包括了可借的学校位置和索书号
	private List<BorrowInfo> borrowInfoList;
	//单个的可借学校
	private String borrowSchool;
	//出版的时间
	private String publishDate;
	//要借书的人写的备注
	private String remark;
	
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public List<String> getCanBorrowSchoolList() {
		return canBorrowSchoolList;
	}
	public void setCanBorrowSchoolList(List<String> canBorrowSchoolList) {
		this.canBorrowSchoolList = canBorrowSchoolList;
	}
	
	public String getBorrowSchool() {
		return borrowSchool;
	}
	public void setBorrowSchool(String borrowSchool) {
		this.borrowSchool = borrowSchool;
	}
	
	public String getBookImageUrl() {
		return bookImageUrl;
	}
	public void setBookImageUrl(String bookImageUrl) {
		this.bookImageUrl = bookImageUrl;
	}
	
	public int getLocalResId()
	{
		return resImageId;
	}
	public void setBookImageUrl(int resImageId)
	{
		this.resImageId = resImageId;
	}
	
	public String getBookPublisher() {
		return bookPublisher;
	}
	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}
	
	
	public List<BorrowInfo> getBorrowInfoList() {
		return borrowInfoList;
	}
	public void setBorrowInfoList(List<BorrowInfo> borrowInfoList) {
		this.borrowInfoList = borrowInfoList;
	}


	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}


	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


	public static class BorrowInfo{
		public String borrowLoc;
		public String borrowIndex; 
	}
	
}
