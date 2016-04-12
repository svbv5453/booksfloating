package com.booklsfloating.activity.searchbooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.protocol.HTTP;

import com.booksfloating.adapter.DrawerLayoutAdapter;
import com.booksfloating.adapter.SearchBooksDetailAdapter;
import com.booksfloating.attr.BooksAttr;
import com.booksfloating.parse.ParseBooksAttrJson;
import com.booksfloating.util.HttpUtil;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SearchBooksDetailActivity extends Activity implements OnClickListener,OnItemClickListener{
	private ListView lv_books_list,lv_left_drawer;
	private Button btn_filter, btn_search, btn_filter_too;
	private SearchBooksDetailAdapter adapter;
	private DrawerLayout drawerLayout;
	private String[] schoolArray;
	private String filterSchool;
	private String searchKeyword;
	private String universityCode;
	//向服务器发送请求的次数，刚启动为第一次
	private int requestTime = 1;
	
    private ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbooks_detail);
		
		//Intent intent = getIntent();
		//searchKeyword = intent.getStringExtra("searchKeyword");
		//universityCode = intent.getStringExtra("universityCode");
		
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		schoolArray = getResources().getStringArray(R.array.spinner_item);
		lv_left_drawer = (ListView)findViewById(R.id.lv_left_drawer);
		//lv_left_drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, schoolArray));
		lv_left_drawer.setAdapter(new DrawerLayoutAdapter(this, schoolArray));
		lv_left_drawer.setOnItemClickListener(new DrawerItemClickListener());
		// ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.filter_bg,  /* nav drawer image to replace 'Up' caret */
                R.string.filter,  /* "open drawer" description for accessibility */
                R.string.filter  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle("筛选");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle("筛选");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        //drawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            //selectItem(0);
        }
        
		
		btn_filter = (Button)findViewById(R.id.btn_filter);
		btn_filter.setOnClickListener(this);
		
		btn_filter_too = (Button)findViewById(R.id.btn_filter_too);
		btn_filter_too.setOnClickListener(this);
		
		testData();
		lv_books_list = (ListView)findViewById(R.id.lv_books_list);
		adapter = new SearchBooksDetailAdapter(this, booksAttrsList);
		lv_books_list.setAdapter(adapter);
		lv_books_list.setOnItemClickListener(this);
		//refreshFromServer();
		//还要添加下拉刷新和上拉刷新
	}
	
	//arraylist的测试数据
	private void testData(){
		List<String> list = new ArrayList<String>();
		list.add("西电");
		list.add("西工大");
		
		BooksAttr booksAttr = new BooksAttr();
		booksAttr.setBookTitle("thinking in java");
		booksAttr.setBookAuthor("埃克尔");
		booksAttr.setBookImageUrl(R.drawable.thinking_in_java);
		booksAttr.setCanBorrowSchoolList(list);		
		booksAttrsList.add(booksAttr);
		
		BooksAttr booksAttr2 = new BooksAttr();
		booksAttr2.setBookTitle("深入学习java");
		booksAttr2.setBookAuthor("埃克尔");
		booksAttr2.setBookImageUrl(R.drawable.c_primer);
		booksAttr2.setCanBorrowSchoolList(list);
		booksAttrsList.add(booksAttr2);
	}
	
	private class DrawerItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			//根据用户选择的item，设置筛选条件
			filterSchool  = schoolArray[position];
			if(filterSchool.equals("所有学校"))
			{
				//啥也不干
			}
			else{
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
	
	private String jsonString;
	private ParseBooksAttrJson pJson = new ParseBooksAttrJson();
	//这个booksAtrrsList是与listview关联的数据源list
	private ArrayList<BooksAttr> booksAttrsList = new ArrayList<BooksAttr>();
	//这个list是每次向服务器请求数据的list，存储的是每次新的page返回的数据
	private ArrayList<BooksAttr> newList = new ArrayList<BooksAttr>();
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			//解析成功返回0，否则返回-1
			case 0:
				//刷新适配器
				adapter.notifyDataSetChanged();
				dismissLoadingDialog();
				break;
			case -1:
				Toast.makeText(SearchBooksDetailActivity.this, "服务器出错，请重试！", Toast.LENGTH_SHORT).show();				
				break;
			default:
				break;
			}
		}
		
	};
		
	private void refreshFromServer()
	{
		showLoadingDialog();		
		final PostParameter[] postParameters = new PostParameter[3];
		new Thread(new Runnable() {		
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				//第一次请求
				if (requestTime == 1) {
					postParameters[0] = new PostParameter("keyword", searchKeyword);
					postParameters[1] = new PostParameter("university", universityCode);
					postParameters[2] = new PostParameter("page", Integer.toString(requestTime));
				}
				else{
					postParameters[0] = new PostParameter("keyword", searchKeyword);
					postParameters[1] = new PostParameter("university", universityCode);
					postParameters[2] = new PostParameter("page", Integer.toString(requestTime));
				}
				jsonString = HttpUtil.httpRequest(HttpUtil.USER_SEARCHBOOKS, postParameters, HttpUtil.POST);
				newList.clear();
				//解析返回的数据
				newList = pJson.parseBookList(jsonString);
				if (newList != null || newList.size() != 0) {
					booksAttrsList.addAll(newList);
					handler.sendEmptyMessage(0);
				}
				else {
					handler.sendEmptyMessage(-1);
				}
				requestTime++;
			}
		}).start();
	}
	
	

	private int count = 0;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_filter_too:
		case R.id.btn_filter:
			if(drawerLayout.isDrawerOpen(Gravity.LEFT))
				drawerLayout.closeDrawer(Gravity.LEFT);
			else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//长按某一项跳转到另一个页面
		Intent intent = new Intent();
		intent.setClass(this, BorrowBooksDetailInfoActivity.class);
		intent.putExtra("intent_booksAttr", booksAttrsList.get(position));
		startActivity(intent);
	}
	
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在注册，请稍后...");
			dialog.show();
		}
		else {
			dialog.dismiss();
		}
	}
	
	private void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
