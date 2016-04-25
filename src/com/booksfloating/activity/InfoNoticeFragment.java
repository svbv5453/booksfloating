package com.booksfloating.activity;

import java.util.ArrayList;
import java.util.List;

import com.booksfloating.activity.infonotice.BooksInfoActivity;
import com.booksfloating.adapter.DrawerLayoutAdapter;
import com.booksfloating.adapter.InfoNoticeAdapter;
import com.booksfloating.attr.BooksAttr;
import com.booksfloating.parse.ParseBooksAttrJson;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.ListViewCompat;
import com.booksfloating.util.ListViewCompat.OnLoadListener;
import com.booksfloating.util.ListViewCompat.OnRefreshListener;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class InfoNoticeFragment extends Fragment implements OnItemClickListener,OnRefreshListener,OnLoadListener,OnClickListener{
	private View view;
	private ListViewCompat lv_info_notice;
	private Button btn_login;
	private InfoNoticeAdapter adapter = null;
	private List<BooksAttr> booksAttrsList = new ArrayList<BooksAttr>();
	private DrawerLayout drawerLayout;
	private ListView lv_left_drawer;
	private String[] schoolArray;
	private String filterSchool;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.activity_info_notice_main, container, false);
		lv_info_notice = (ListViewCompat)view.findViewById(R.id.lv_info_notice);
		adapter = new InfoNoticeAdapter(getActivity(), booksAttrsList);
		lv_info_notice.setAdapter(adapter);
		lv_info_notice.setOnItemClickListener(this);
		lv_info_notice.setOnRefreshListener(this);
		lv_info_notice.setOnItemClickListener(this);
		
		schoolArray = getResources().getStringArray(R.array.spinner_item);
		lv_left_drawer = (ListView)view.findViewById(R.id.lv_left_drawer);
		lv_left_drawer.setAdapter(new DrawerLayoutAdapter(getActivity(), schoolArray));
		lv_left_drawer.setOnItemClickListener(new DrawerItemClickListener());
		
		btn_login = (Button)view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		requestFromServer(0);
		return view;
	}
	
	private class DrawerItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			//根据用户选择的item，设置筛选条件
			filterSchool  = schoolArray[position];
			if(drawerLayout.isDrawerOpen(Gravity.LEFT))
				drawerLayout.closeDrawer(Gravity.LEFT);
			else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			if(filterSchool.equals("所有学校"))
			{
				//啥也不干
			}
			else{
				Log.i("filterSchool :", filterSchool);
				//根据筛选条件设置主页面的list
				newList.clear();
				BooksAttr booksAttr = new BooksAttr();
				List<String> tempList = new ArrayList<String>();
				for (int i = 0; i < booksAttrsList.size(); i++) {
					booksAttr = booksAttrsList.get(i);
					if(booksAttr.getCanBorrowSchoolList().contains(filterSchool))
					{
						//把原来的canBorrowSchoolList修改为只含有这个指定学校的List
						tempList.clear();
						tempList.add(filterSchool);
						booksAttr.setCanBorrowSchoolList(tempList);
						newList.add(booksAttr);
					}
				}
				booksAttrsList.clear();
				booksAttrsList.addAll(newList);
			}
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BooksAttr booksAttr = (BooksAttr)adapter.getItem(position-1);
		Intent intent = new Intent();
		intent.setClass(getActivity(), BooksInfoActivity.class);
		intent.putExtra("intent_booksAttr", booksAttr);
		getActivity().startActivity(intent);
	}
	
	private int requestTime = 1;
	private ParseBooksAttrJson parseJson = new ParseBooksAttrJson();
	private ArrayList<BooksAttr> newList = new ArrayList<BooksAttr>();
	
	private void requestFromServer(final int what)
	{
		final PostParameter[] postParameters = new PostParameter[2];
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(requestTime == 1)
				{
					postParameters[0] = new PostParameter("university", Integer.toString(0));
					postParameters[1] = new PostParameter("page", Integer.toString(1));
				}else {
					postParameters[0] = new PostParameter("university", filterSchool);
					postParameters[1] = new PostParameter("page", Integer.toString(requestTime));
				}
				String json = null;
				json = HttpUtil.httpRequest(HttpUtil.BROWSE_BOOK, postParameters, HttpUtil.POST);
				if (json != null) {
					Message msg = handler.obtainMessage();
					msg.what = what;
					msg.obj = json;
					handler.sendMessage(msg);					
				}else {
					handler.sendEmptyMessage(LoginActivity.SERVER_ERROR);
				}
			}
		}).start();
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String json = (String) msg.obj;
			switch (msg.what) {
			case ListViewCompat.REFRESH:				
				newList.clear();
				newList = parseJson.parseBrowseNoticeList(json);
				if(newList != null && newList.size() > 0)
				{
					booksAttrsList.clear();
					booksAttrsList.addAll(newList);
				}
				break;
			case ListViewCompat.LOAD:
				newList.clear();
				newList = parseJson.parseBrowseNoticeList(json);
				if(newList != null && newList.size() > 0)
				{
					booksAttrsList.addAll(newList);
				}
				break;
			case LoginActivity.SERVER_ERROR:
				Toast.makeText(getActivity(), "服务器错误，请重试！", Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
		}
		
	};

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		requestFromServer(ListViewCompat.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		requestFromServer(ListViewCompat.REFRESH);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}

}
