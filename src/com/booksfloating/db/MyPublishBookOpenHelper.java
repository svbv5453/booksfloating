package com.booksfloating.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyPublishBookOpenHelper extends SQLiteOpenHelper{
	
	/**
	 *  MyPublishBook表建表语句
	 */
	public static final String CREATE_MYPUBLISBOOK = "create table MyPublishBook ("
				+ "id integer primary key autoincrement, " 
				+ "book_name text, "
				+ "book_author text,"
				+ "book_location text,"
				+ "book_publish_time text,"
				+ "book_remark text)";

	public MyPublishBookOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_MYPUBLISBOOK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
