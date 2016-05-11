package com.booksfloating.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.booksfloating.domain.MyInfoPublishBookBean;



public class MyPublishBookDB {
	
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "publish_book";

	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static MyPublishBookDB myPublishBookDB;

	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化
	 */
	private MyPublishBookDB(Context context) {
		MyPublishBookOpenHelper dbHelper = new MyPublishBookOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取CoolWeatherDB的实例。
	 */
	public synchronized static MyPublishBookDB getInstance(Context context) {
		if (myPublishBookDB == null) {
			myPublishBookDB = new MyPublishBookDB(context);
		}
		return myPublishBookDB;
	}

	/**
	 * 将myInfoPublishBookBean实例存储到数据库。
	 */
	public void saveMyPublishBook(MyInfoPublishBookBean myInfoPublishBookBean) {
		if (myInfoPublishBookBean != null) {
			ContentValues values = new ContentValues();
			values.put("book_name", myInfoPublishBookBean.getBookName());
			values.put("book_author", myInfoPublishBookBean.getBookAuthor());
			values.put("book_location", myInfoPublishBookBean.getBookLocation());
			values.put("book_publish_time", myInfoPublishBookBean.getBookPublicshTime());
			values.put("book_remark", myInfoPublishBookBean.getBookAuthor());
			db.insert("MyPublishBook", null, values);
		}
	}

	/**
	 * 从数据库读取图书发布信息。
	 */
	public List<MyInfoPublishBookBean> loadMyPublishBooks() {
		List<MyInfoPublishBookBean> list = new ArrayList<MyInfoPublishBookBean>();
		Cursor cursor = db
				.query("MyPublishBook", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				MyInfoPublishBookBean myInfoPublishBookBean = new MyInfoPublishBookBean();
				
				myInfoPublishBookBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
				myInfoPublishBookBean.setBookName(cursor.getString(cursor
						.getColumnIndex("book_name")));
				myInfoPublishBookBean.setBookAuthor(cursor.getString(cursor
						.getColumnIndex("book_author")));
				myInfoPublishBookBean.setBookLocation(cursor.getString(cursor
						.getColumnIndex("book_location")));
				myInfoPublishBookBean.setBookPublicshTime(cursor.getString(cursor
						.getColumnIndex("book_publish_time")));
				myInfoPublishBookBean.setBookRemark(cursor.getString(cursor
						.getColumnIndex("book_remark")));
				
				list.add(myInfoPublishBookBean);
			} while (cursor.moveToNext());
		}
		return list;
	}

}
