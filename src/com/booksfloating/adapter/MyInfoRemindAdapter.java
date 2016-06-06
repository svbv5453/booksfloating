package com.booksfloating.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.booksfloating.domain.MyInfoPublishBookBean;

import com.xd.booksfloating.R;
import com.xd.imageloader.ImageLoader;

public class MyInfoRemindAdapter extends BaseAdapter {
	List<MyInfoPublishBookBean> publishBookBeanList;
	LayoutInflater mLayoutInflater;

	Context mContext;
	

	public MyInfoRemindAdapter(Context context, List<MyInfoPublishBookBean> publishBookBeanList) {
		super();
		this.mContext = context;
		this.publishBookBeanList = publishBookBeanList;
		this.mLayoutInflater = LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		return publishBookBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return publishBookBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.myinfo_remind_item, null);
			
			viewHolder.bookName = (TextView) convertView.findViewById(R.id.tv_my_info_remind_book_name);
			viewHolder.bookAuthor = (TextView) convertView.findViewById(R.id.tv_my_info_remind_book_author);
			viewHolder.bookLocation = (TextView) convertView.findViewById(R.id.tv_my_info_remind_book_location);
			viewHolder.expirationTime = (TextView) convertView.findViewById(R.id.tv_my_info_remind_expirationTime);
			viewHolder.bookImage = (ImageView) convertView.findViewById(R.id.iv_my_info_remind_book_image);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		
		String url = publishBookBeanList.get(position).bookIconUrl;
		if(url == null)
		{
			//加载本地图片
			viewHolder.bookImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_book));
		}
		else {
			//加载网络图片
			//ImageManager.from(context).displayImage(viewHolder.iv_books_image, booksAttr.getBookImageUrl(), R.drawable.default_book);
			ImageLoader imageLoader = new ImageLoader(mContext);
			imageLoader.DisplayImage(url, viewHolder.bookImage, false, R.drawable.default_book);
		}
		viewHolder.bookName.setText(publishBookBeanList.get(position).bookName);
		viewHolder.bookAuthor.setText(publishBookBeanList.get(position).bookAuthor);
		viewHolder.bookLocation.setText(publishBookBeanList.get(position).bookLocation);
		viewHolder.expirationTime.setText(publishBookBeanList.get(position).bookExpirationTime);
		//viewHolder.bookImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_book));
		
		//viewHolder.bookImage.setTag(url);
		return convertView;
	}
	public static class ViewHolder{
		ImageView bookImage;
		TextView bookName;
		TextView bookAuthor;
		TextView bookLocation;
		TextView expirationTime;
		
		
	}
	

}
