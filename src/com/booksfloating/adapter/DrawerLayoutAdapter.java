package com.booksfloating.adapter;

import java.security.PublicKey;

import com.xd.booksfloating.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerLayoutAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private String[] str;
	public DrawerLayoutAdapter(Context context, String[] str)
	{
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.str = str;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return str[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv_drawer_item = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
			tv_drawer_item = (TextView)convertView.findViewById(R.id.tv_drawer_item);
		}
		tv_drawer_item.setText(str[position]);
		return convertView;
	}

}
