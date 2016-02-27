package com.booksfloating.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;



public class MyInfoOrderDetailFragment extends Fragment{
	private TextView orderNumber = null;
	private TextView orderDate = null;
	private TextView orderMessage = null;
	private TextView borrowDate = null;
	private TextView returnDate = null;
	private TextView phoneNumber = null;
	private TextView university = null;
	//废弃类
	private static String urlTest = "http://www.imooc.com/api/teacher?type=4&num=30";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//暂时不用
		View view = inflater.inflate(R.layout.myinfo_ask_order_detail, container, false);
		orderNumber = (TextView) view.findViewById(R.id.tv_myinfo_ask_orderNumber);
		orderDate = (TextView) view.findViewById(R.id.tv_myinfo_ask_date);
		university = (TextView) view.findViewById(R.id.tv_myinfo_ask_university);
		
		orderMessage = (TextView) view.findViewById(R.id.tv_myinfo_ask_message);
		borrowDate = (TextView) view.findViewById(R.id.tv_myinfo_ask_borrowDate);
		returnDate = (TextView) view.findViewById(R.id.tv_myinfo_ask_returnDate);
		phoneNumber = (TextView) view.findViewById(R.id.tv_myinfo_ask_phoneNumber);
		String jsonData = HttpUtil.getJsonData(urlTest);
		parseJsonData(jsonData);
		
		return view;
		
	}
	
	public void parseJsonData(String jsonData) {
		try {
			
			JSONObject jsonObject = new JSONObject(jsonData);
			if(jsonObject.getString("status").equals("1")){
				/*String userName = jsonObject.getString("user_name");
				String bookName = jsonObject.getString("book");
				String bookAuthor = jsonObject.getString("author");
				String bookLocation = jsonObject.getString("book_location");
				orderMessage.setText("昵称为" + userName + "的用户已接受您关于\"" + bookName + ", " + bookAuthor + ", " + bookLocation + ",的求助 ");
				borrowDate.setText("借书日: " + jsonObject.getString("lend_time"));
				returnDate.setText("还书日: " + jsonObject.getString("return_time"));
				university.setText("他所在的学校为: " + jsonObject.getString("university"));
				phoneNumber.setText("他的手机号为: " + jsonObject.getString("phone"));*/
				
				
				
				JSONArray jsonArray = jsonObject.getJSONArray("data");
					jsonObject = jsonArray.getJSONObject(1);
					borrowDate.setText("借书日: " + jsonObject.getString("name"));
					returnDate.setText("还书日: " + jsonObject.getString("description"));
					
					
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	

}
