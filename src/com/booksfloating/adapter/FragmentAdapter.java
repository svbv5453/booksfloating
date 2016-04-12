package com.booksfloating.adapter;

import com.booklsfloating.activity.searchbooks.SearchBooksFragment;
import com.booksfloating.activity.BooksRecommendFragment;
import com.booksfloating.activity.InfoNoticeFragment;
import com.booksfloating.activity.MainActivity;
import com.booksfloating.activity.MyInfoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author liuwenyuan
 *
 */
public class FragmentAdapter extends FragmentPagerAdapter{
	public final static int TAB_COUNT = 4;
	public FragmentAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case MainActivity.TAB_SEARCH_BOOKS:
			SearchBooksFragment sbFragment = new SearchBooksFragment();
			return sbFragment;
		case MainActivity.TAB_INFO_NOTICE:
			InfoNoticeFragment inFragment = new InfoNoticeFragment();
			return inFragment;
		case MainActivity.TAB_BOOKS_RECOMMEND:
			BooksRecommendFragment brFragment = new BooksRecommendFragment();
			return brFragment;
		case MainActivity.TAB_MY_INFO:
			MyInfoFragment miFragment = new MyInfoFragment();
			return miFragment;

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TAB_COUNT;
	}
}
