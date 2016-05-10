package com.booksfloating.adapter;

import java.util.ArrayList;
import java.util.List;

import com.booksfloating.attr.BooksAttr;
import com.booksfloating.util.ImageManager;
import com.xd.booksfloating.R;

import android.content.Context;
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
		final ViewHolder viewHolder;
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
			viewHolder.iv_books_image.setImageDrawable(context.getResources().getDrawable(R.drawable.default_book));
		}
		else {
			ImageManager.from(context).displayImage(viewHolder.iv_books_image, booksAttr.getBookImageUrl(), R.drawable.default_book);
		}
		
		viewHolder.tv_books_title.setText(booksAttr.getBookTitle());
		viewHolder.tv_books_author.setText(booksAttr.getBookAuthor());
		StringBuffer borrowLibrary = new StringBuffer();
		
		if(booksAttr.getCanBorrowSchoolList().size() == 1)
		{	
			//Integer temp = Integer.parseInt(booksAttr.getCanBorrowSchoolList().get(0));
			//borrowLibrary.append(Constants.schoolIDtoNameMap.get(temp));
			borrowLibrary.append(booksAttr.getCanBorrowSchoolList().get(0));
		}
		else if(booksAttr.getCanBorrowSchoolList().size() > 1){
			for (String str : booksAttr.getCanBorrowSchoolList()) {
				//Integer temp2 = Integer.parseInt(str);
				//borrowLibrary.append(Constants.schoolIDtoNameMap.get(str));
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
