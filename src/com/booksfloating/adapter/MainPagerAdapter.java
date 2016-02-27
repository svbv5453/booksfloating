package com.booksfloating.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * 
 * @author liuwenyuan
 *
 */
public class MainPagerAdapter extends PagerAdapter{
	private ArrayList<View> views;
	
	public MainPagerAdapter(ArrayList<View> views) {
		// TODO Auto-generated constructor stub
		this.views = views;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager)container).removeView(views.get(position));
	}

}
