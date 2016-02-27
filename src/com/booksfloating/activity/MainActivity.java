package com.booksfloating.activity;

import java.util.ArrayList;

import com.booksfloating.adapter.FragmentAdapter;
import com.booksfloating.adapter.MainPagerAdapter;
import com.booksfloating.widget.MyRadioButton;
import com.xd.booksfloating.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author liuwenyuan
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener{
	private ViewPager viewPager;
	private MyRadioButton mrb_search_books,mrb_info_notice,
			mrb_books_recommend,mrb_my_info;
	private ArrayList<View> views;
	private MainPagerAdapter adapter;
	public static final int TAB_SEARCH_BOOKS = 0;
	public static final int TAB_INFO_NOTICE = 1;
	public static final int TAB_BOOKS_RECOMMEND = 2;
	public static final int TAB_MY_INFO = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewPager();
	}
	
	private void initViewPager()
	{
		//views = new ArrayList<View>();
		//adapter = new MainPagerAdapter(views);
		viewPager = (ViewPager)findViewById(R.id.vp_main_activity);
		mrb_search_books = (MyRadioButton)findViewById(R.id.mrb_search_books);
		mrb_info_notice = (MyRadioButton)findViewById(R.id.mrb_info_notice);
		mrb_books_recommend = (MyRadioButton)findViewById(R.id.mrb_books_recommend);
		mrb_my_info = (MyRadioButton)findViewById(R.id.mrb_my_info);
		
		mrb_search_books.setOnClickListener(this);
		mrb_info_notice.setOnClickListener(this);
		mrb_books_recommend.setOnClickListener(this);
		mrb_my_info.setOnClickListener(this);
		
		FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(0);
		ViewPagerListener();
	}

	private void ViewPagerListener()
	{
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case TAB_SEARCH_BOOKS:
					mrb_search_books.setChecked(true);
					System.out.println("选择1");
					break;
				case TAB_INFO_NOTICE:
					mrb_info_notice.setChecked(true);
					System.out.println("选择2");
					break;
				case TAB_BOOKS_RECOMMEND:
					mrb_books_recommend.setChecked(true);
					System.out.println("选择3");
					break;
				case TAB_MY_INFO:
					mrb_my_info.setChecked(true);
					System.out.println("选择4");
				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mrb_search_books:
			viewPager.setCurrentItem(TAB_SEARCH_BOOKS);
			break;
		case R.id.mrb_info_notice:
			viewPager.setCurrentItem(TAB_INFO_NOTICE);
			break;
		case R.id.mrb_books_recommend:
			viewPager.setCurrentItem(TAB_BOOKS_RECOMMEND);
			break;
		case R.id.mrb_my_info:
			viewPager.setCurrentItem(TAB_MY_INFO);
			break;

		default:
			break;
		}
	}
}
