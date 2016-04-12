package com.booksfloating.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * 
 * @author liuwenyuan
 *
 */
public class MySpinnerAdapter extends ArrayAdapter<String>{
	public MySpinnerAdapter(Context context, int textViewResourceId,
			String[] items) {
		// TODO Auto-generated constructor stub
		super(context, textViewResourceId,items);
	}

	@Override
    public int getCount() {
        return super.getCount(); // This makes the trick: do not show last item
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
