package com.booksfloating.adapter;

import java.util.ArrayList;
import java.util.List;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.util.LoadBookImage;
import com.booksfloating.util.LoaderImageUseVelloy;
import com.xd.booksfloating.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchBooksDetailAdapter extends BaseAdapter{

	private Context context;
	private List<BooksAttr> booksAttrsList = new ArrayList<BooksAttr>();
	private LayoutInflater inflater;
	
	public SearchBooksDetailAdapter(Context context, List<BooksAttr> booksAttrsList)
	{
		this.context = context;
		this.booksAttrsList = booksAttrsList;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.booksAttrsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return booksAttrsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}	

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		BooksAttr booksAttr = booksAttrsList.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.books_list_item, null);
			viewHolder.iv_books_image = (ImageView)convertView.findViewById(R.id.iv_books_image);
			viewHolder.tv_books_title = (TextView)convertView.findViewById(R.id.tv_books_title);
			viewHolder.tv_books_author = (TextView)convertView.findViewById(R.id.tv_books_author);
			viewHolder.tv_borrow_library = (TextView)convertView.findViewById(R.id.tv_borrow_library);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		if(booksAttr.getBookImageUrl() == null)
		{
			//加载本地图片
			Drawable drawable = context.getResources().getDrawable(booksAttr.getLocalResId());
			viewHolder.iv_books_image.setBackgroundDrawable(drawable);
		}
		else {
			//加载网络图片
			new LoaderImageUseVelloy().LoaderImage(context, viewHolder.iv_books_image, booksAttr.getBookImageUrl());
		}
		
		viewHolder.tv_books_title.setText(booksAttr.getBookTitle());
		viewHolder.tv_books_author.setText(booksAttr.getBookAuthor());
		StringBuffer borrowLibrary = new StringBuffer();

		if(booksAttr.getCanBorrowSchoolList().size() == 1)
		{
			borrowLibrary.append(booksAttr.getCanBorrowSchoolList().get(0));
		}
		else {
			for (String str : booksAttr.getCanBorrowSchoolList()) {
				borrowLibrary.append(str);
				borrowLibrary.append(",");
			}
			borrowLibrary.deleteCharAt(borrowLibrary.length()-1);
		}
		
		viewHolder.tv_borrow_library.setText(borrowLibrary.toString());
		
		return convertView;
	}
	
	public static class ViewHolder{
		public ImageView iv_books_image;
		public TextView tv_books_title;
		public TextView tv_books_author;
		public TextView tv_borrow_library;
	}

}
