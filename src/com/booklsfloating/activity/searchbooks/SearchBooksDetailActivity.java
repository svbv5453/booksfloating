package com.booklsfloating.activity.searchbooks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.booksfloating.adapter.DrawerLayoutAdapter;
import com.booksfloating.adapter.SearchBooksDetailAdapter;
import com.booksfloating.attr.BooksAttr;
import com.booksfloating.globalvar.Constants;
import com.booksfloating.parse.ParseBooksAttrJson;
import com.booksfloating.util.HttpUtil;
import com.booksfloating.util.ListViewCompat;
import com.booksfloating.util.ListViewCompat.OnLoadListener;
import com.booksfloating.util.ListViewCompat.OnRefreshListener;
import com.xd.booksfloating.R;
import com.xd.connect.PostParameter;
import com.xd.dialog.DialogFactory;

public class SearchBooksDetailActivity extends Activity implements OnClickListener,OnItemClickListener,
OnRefreshListener,OnLoadListener{
	private ListView lv_left_drawer;
	private ListViewCompat lv_books_list;
	private Button btn_filter, btn_search, btn_search_1, btn_filter_too;
	private EditText et_search_sh = null;
	private SearchView searchView = null;
	private SearchBooksDetailAdapter adapter;
	private DrawerLayout drawerLayout;
	private String[] schoolArray;
	private String filterSchool;
	private String searchKeyword;
	private int universityCode;
	private String unviersity;
	//向服务器发送请求的次数，刚启动为第一次
	private int requestTime = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbooks_detail);
		
		Intent intent = getIntent();
		searchKeyword = intent.getStringExtra("intent_keyword");
		universityCode = intent.getIntExtra("intent_universitycode", 0);
		unviersity = Constants.schoolIDtoNameMap.get(universityCode);
		System.out.println("intent_keyword:"+searchKeyword);
		System.out.println("intent_universitycode:"+universityCode);
		
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		schoolArray = getResources().getStringArray(R.array.spinner_item);

		btn_search = (Button)findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		et_search_sh = (EditText)findViewById(R.id.et_search_sh);
		et_search_sh.setImeOptions(EditorInfo.IME_ACTION_SEARCH);  
		et_search_sh.setInputType(InputType.TYPE_CLASS_TEXT);  
		et_search_sh.setSingleLine(true); 
		et_search_sh.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (!searchKeyword.equals(et_search_sh.getText().toString().trim())) {
						showLoadingDialog();
						booksAttrsList.clear();
						searchKeyword = et_search_sh.getText().toString();
						et_search_sh.setText("");
						refreshFromServer(Constants.SEARCH_KEYWORD);
						
						System.out.println("searchkeyword-------》 "+searchKeyword);
						System.out.println("universityCode----》 "+universityCode);
					}
					
					return true;
				}
				return false;
			}
		});

		lv_left_drawer = (ListView)findViewById(R.id.lv_left_drawer);
		//lv_left_drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, schoolArray));
		lv_left_drawer.setAdapter(new DrawerLayoutAdapter(this, schoolArray));
		lv_left_drawer.setOnItemClickListener(new DrawerItemClickListener());
		
			
		btn_filter = (Button)findViewById(R.id.btn_filter);
		btn_filter.setOnClickListener(this);
		
		btn_filter_too = (Button)findViewById(R.id.btn_filter_too);
		btn_filter_too.setOnClickListener(this);
		
		//testData();
		lv_books_list = (ListViewCompat)findViewById(R.id.lv_books_list);
		adapter = new SearchBooksDetailAdapter(this, booksAttrsList);
		lv_books_list.setAdapter(adapter);
		lv_books_list.setOnItemClickListener(this);
		lv_books_list.setOnRefreshListener(this);
		lv_books_list.setOnLoadListener(this);
		
		refreshFromServer(0);
	}
	
	//arraylist的测试数据
	private void testData(){
		List<String> list = new ArrayList<String>();
		list.add("西电");
		list.add("西工大");
		
		BooksAttr booksAttr = new BooksAttr();
		booksAttr.setBookTitle("thinking in java");
		booksAttr.setBookAuthor("埃克尔");
		booksAttr.setCanBorrowSchoolList(list);		
		booksAttrsList.add(booksAttr);
		
		BooksAttr booksAttr2 = new BooksAttr();
		booksAttr2.setBookTitle("深入学习C++");
		booksAttr2.setBookAuthor("埃克尔");
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
			
			System.out.println("position "+position);
			System.out.println("filterSchool"+filterSchool);
			if(drawerLayout.isDrawerOpen(Gravity.LEFT))
				drawerLayout.closeDrawer(Gravity.LEFT);
			else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			if(filterSchool.equals(unviersity))
			{
				//啥也不干
			}
			else{
				System.out.println("filterSchool"+filterSchool);
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
				
				if (booksAttrsList.size() == 0) {
					requestTime = 1;
					universityCode = Constants.schoolNameMap.get(filterSchool);
					showLoadingDialog();
					refreshFromServer(ListViewCompat.REFRESH);
				}
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
			dismissLoadingDialog();
			lv_books_list.onRefreshComplete();
			lv_books_list.onLoadComplete();
			
			switch (msg.what) {				
			case Constants.SEARCH_KEYWORD:
				et_search_sh.setVisibility(View.GONE);
				btn_filter.setVisibility(View.VISIBLE);
				btn_filter_too.setVisibility(View.VISIBLE);
				
				newList.clear();
				//解析返回的数据
				newList = pJson.parseBookList(jsonString);
				if (newList != null && newList.size() != 0) {
					booksAttrsList.clear();			
					booksAttrsList.addAll(newList);
					//刷新适配器
					adapter.notifyDataSetChanged();
					requestTime = 1;
				}else {
					Toast.makeText(SearchBooksDetailActivity.this, "无数据返回！", Toast.LENGTH_SHORT).show();
				}				
				break;
			case ListViewCompat.REFRESH:
				newList.clear();
				//解析返回的数据
				newList = pJson.parseBookList(jsonString);
				if (newList != null && newList.size() != 0) {
					booksAttrsList.clear();			
					booksAttrsList.addAll(newList);
					//刷新适配器
					adapter.notifyDataSetChanged();
					requestTime = 1;
				}else {
					Toast.makeText(SearchBooksDetailActivity.this, "无数据返回！", Toast.LENGTH_SHORT).show();
					
				}												
				break;
			case ListViewCompat.LOAD:
				newList.clear();
				//解析返回的数据
				newList = pJson.parseBookList(jsonString);
				if (newList != null && newList.size() != 0) {		
					booksAttrsList.addAll(newList);
					//刷新适配器
					adapter.notifyDataSetChanged();
					requestTime++;
				}else {
					Toast.makeText(SearchBooksDetailActivity.this, "已经到底啦！", Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.SERVER_ERROR:
				Toast.makeText(SearchBooksDetailActivity.this, "服务器错误，请重试！", Toast.LENGTH_SHORT).show();				
				break;
			default:
				break;
			}
		}
		
	};
		
	private void refreshFromServer(final int what)
	{
		//showLoadingDialog();		
		final PostParameter[] postParameters = new PostParameter[3];
		new Thread(new Runnable() {		
			
			@Override
			public void run() {
				// TODO Auto-generated method stub				
				//第一次请求
				if (requestTime == 1) {
					postParameters[0] = new PostParameter("keyword", searchKeyword);
					postParameters[1] = new PostParameter("university", Integer.toString(universityCode));
					postParameters[2] = new PostParameter("page", Integer.toString(requestTime));
				}
				else{
					postParameters[0] = new PostParameter("keyword", searchKeyword);
					postParameters[1] = new PostParameter("university", Integer.toString(universityCode));
					postParameters[2] = new PostParameter("page", Integer.toString(requestTime));
				}
				jsonString = HttpUtil.httpRequest(HttpUtil.USER_SEARCHBOOKS, postParameters, HttpUtil.POST);
				if (jsonString != null) {					
					Message msg = handler.obtainMessage();
					msg.what = what;
					handler.sendMessage(msg);					
				}
				else{
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}
	
	

	private int count = 0;
	private boolean flag = false;//判断搜索的关键字是否改变
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

		case R.id.btn_search:						
			if (et_search_sh.getVisibility() == View.GONE) {
				et_search_sh.setVisibility(View.VISIBLE);
				btn_filter.setVisibility(View.GONE);
				btn_filter_too.setVisibility(View.GONE);
			}else if (et_search_sh.getVisibility() == View.VISIBLE) {
				et_search_sh.setText("");
			}
			
			Toast.makeText(SearchBooksDetailActivity.this, "搜索测试", Toast.LENGTH_SHORT).show();
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
		intent.putExtra("intent_booksAttr", booksAttrsList.get(position-1));
		startActivity(intent);
	}
	
	private Dialog dialog = null;
	private void showLoadingDialog(){
		if(dialog == null)
		{
			dialog = DialogFactory.creatLoadingDialog(this, "正在搜索，请稍后...");
			dialog.show();
		}
		else {
			dialog.dismiss();
		}
	}
	
	private void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		refreshFromServer(ListViewCompat.LOAD);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		refreshFromServer(ListViewCompat.REFRESH);
	}

}
