package com.booksfloating.adapter;

import java.util.HashMap;
import java.util.List;

import com.booksfloating.attr.BooksAttr.BorrowInfo;
import com.xd.booksfloating.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BorrowBookDetailInfoAdapter extends BaseAdapter{
	private List<BorrowInfo> borrowInfoList;
	private Context context;
	private LayoutInflater inflater;
	private static HashMap<Integer, Boolean> isSelected;
	
	public BorrowBookDetailInfoAdapter(Context context, List<BorrowInfo> borrowInfoList)
	{
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		this.borrowInfoList = borrowInfoList;
		isSelected = new HashMap<Integer, Boolean>();
		
		//初始化数据
		initData();
	}
	
	private void initData()
	{
		for (int i = 0; i < borrowInfoList.size(); i++) {
			isSelected.put(i, false);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return borrowInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return borrowInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.library_collection_list_item, null);
			viewHolder.cb_choose = (CheckBox)convertView.findViewById(R.id.cb_choose);
			viewHolder.tv_index_number = (TextView)convertView.findViewById(R.id.tv_index_number);
			viewHolder.tv_lib_collection_loc = (TextView)convertView.findViewById(R.id.tv_lib_collection_loc);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.cb_choose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelected.get(position)) {
					isSelected.put(position, false);
					//setSelected(isSelected);
				}else {
					isSelected.put(position, true);
					//setSelected(isSelected);
				}
			}
		});
		
		viewHolder.cb_choose.setChecked(isSelected.get(position));
		BorrowInfo borrowInfo = borrowInfoList.get(position);
		viewHolder.tv_lib_collection_loc.setText(borrowInfo.borrowLoc);
		viewHolder.tv_index_number.setText(borrowInfo.borrowIndex);
		
		return convertView;
	}
	public static HashMap<Integer, Boolean> getSelected(){
		return isSelected;
	}
	
	public static void setSelected(HashMap<Integer, Boolean> map){
		BorrowBookDetailInfoAdapter.isSelected = map;
	}
	
	public static class ViewHolder{
		private CheckBox cb_choose;
		private TextView tv_lib_collection_loc;
		private TextView tv_index_number;
	}

}
