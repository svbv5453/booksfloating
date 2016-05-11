package com.booksfloating.domain;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class MyInfoBookDetailBean implements Serializable{
	private String bookName;
	private String bookAuthor;
	private String bookLocation;
	private String lenderName;
	private String lenderUniversity;
	private String borrowTime;
	private String bookPublicshTime;
	private String returnTime;
	private String phoneNumber;
	
	
	public MyInfoBookDetailBean() {
		super();
	}

	
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookLocation() {
		return bookLocation;
	}
	public void setBookLocation(String bookLocation) {
		this.bookLocation = bookLocation;
	}
	public String getLenderName() {
		return lenderName;
	}
	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}
	public String getLenderUniversity() {
		return lenderUniversity;
	}
	public void setLenderUniversity(String lenderUniversity) {
		this.lenderUniversity = lenderUniversity;
	}
	public String getBorrowTime() {
		return borrowTime;
	}
	public void setBorrowTime(String borrowTime) {
		this.borrowTime = borrowTime;
	}
	public String getBookPublicshTime() {
		return bookPublicshTime;
	}
	public void setBookPublicshTime(String bookPublicshTime) {
		this.bookPublicshTime = bookPublicshTime;
	}
	public String getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/*@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(bookAuthor);
		dest.writeString(bookName);
		dest.writeString(bookLocation);
		dest.writeString(lenderName);
		dest.writeString(lenderUniversity);
		dest.writeString(borrowTime);
		dest.writeString(bookPublicshTime);
		dest.writeString(returnTime);
		dest.writeString(phoneNumber);
	
		
	}
	public final static Parcelable.Creator<MyInfoBookDetailBean> CRETOR = new Creator<MyInfoBookDetailBean>() {
		
		@Override
		public MyInfoBookDetailBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MyInfoBookDetailBean[size];
		}
		
		@Override
		public MyInfoBookDetailBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MyInfoBookDetailBean(source);
		}
	};
	public MyInfoBookDetailBean(Parcel source){
		this.bookName = source.readString();
		this.bookAuthor = source.readString();
		this.bookLocation = source.readString();
		this.lenderName = source.readString();
		this.lenderUniversity = source.readString();
		this.borrowTime = source.readString();
		this.bookPublicshTime = source.readString();
		this.returnTime = source.readString();
		this.phoneNumber = source.readString();
	}*/
	

}
