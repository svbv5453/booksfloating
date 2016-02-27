package com.booksfloating.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.booksfloating.domain.BooksRecommendBean;
import com.xd.booksfloating.R;

public class MyInfoOrderAdapter extends BaseAdapter {
	List<BooksRecommendBean> booksBeanList;
	LayoutInflater mLayoutInflater;

	public MyInfoOrderAdapter(Context context, List<BooksRecommendBean> booksBeanList) {
		super();
		this.booksBeanList = booksBeanList;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return booksBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return booksBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHoder;
		if(convertView == null){
			viewHoder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.myinfo_order_item, null);
			viewHoder.orderDate = (TextView) convertView.findViewById(R.id.tv_myinfo_order_date);
			viewHoder.orderMessage = (TextView) convertView.findViewById(R.id.tv_myinfo_order_message);
			convertView.setTag(viewHoder);
		}else{
			viewHoder = (ViewHolder) convertView.getTag();
		}
		viewHoder.orderDate.setText("时间:" + booksBeanList.get(position).bookPublicshTime);
		String bookName = booksBeanList.get(position).bookName;
		String bookAuthor = booksBeanList.get(position).bookAuthor;
		String bookLocation = booksBeanList.get(position).bookLocation;
		
		viewHoder.orderMessage.setText("关于\"" + bookName + "," + bookAuthor + "," + bookLocation +"\"的求助" );
		return convertView;
	}
	class ViewHolder{
		TextView orderDate;
		TextView orderMessage;
	}

}
