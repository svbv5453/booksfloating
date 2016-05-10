package com.booksfloating.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.booklsfloating.activity.searchbooks.SearchBooksFragment;
import com.booksfloating.activity.BooksRecommendFragment;
import com.booksfloating.activity.InfoNoticeFragment;
import com.booksfloating.activity.MyInfoFragment;

public class MyFragmentPagerAdater extends FragmentPagerAdapter{

	private final static int PAGER_COUNT = 4;
	private SearchBooksFragment searchBookFragment;
	private InfoNoticeFragment infoNoticeFragment;
	private BooksRecommendFragment bookRecommendActivity;
	private MyInfoFragment myInfoFragment;
	public MyFragmentPagerAdater(FragmentManager fm) {
		super(fm);
		searchBookFragment = new SearchBooksFragment();
		infoNoticeFragment = new InfoNoticeFragment();
		bookRecommendActivity = new BooksRecommendFragment();
		myInfoFragment = new MyInfoFragment();
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		/*switch(position){
		case MainActivity.PAGE_ONE:
			fragment = searchBookFragment;
			break;
		case MainActivity.PAGE_TWO:
			fragment = infoNoticeFragment;
			break;
		case MainActivity.PAGE_THREE:
			fragment = bookRecommendActivity;
			break;
		case MainActivity.PAGE_FOUR:
			fragment = myInformationActivity;
			break;
 
		}*/
		return fragment;
	}

	@Override
	public int getCount() {
		return PAGER_COUNT;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	
}
