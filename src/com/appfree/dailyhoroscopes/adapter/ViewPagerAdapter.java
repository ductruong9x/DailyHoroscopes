package com.appfree.dailyhoroscopes.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.appfree.dailyhoroscopes.R;
import com.appfree.dailyhoroscopes.fragment.CodeFragment;
import com.appfree.dailyhoroscopes.fragment.NewsFeedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context context;
	private String[] CONTENT;

	public ViewPagerAdapter(Context context, FragmentManager fm) {

		super(fm);
		this.context = context;
		CONTENT = new String[] { context.getString(R.string.news_feed),
				context.getString(R.string.code), };
	}

	Fragment fragment;

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
		if (position == 0) {
			NewsFeedFragment newfFeedFragment = (NewsFeedFragment) object;
			newfFeedFragment.init();
		} else if (position == 1) {
			CodeFragment codeFragment = (CodeFragment) object;
			codeFragment.init();
		}

	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			fragment = new NewsFeedFragment();

			break;
		case 1:
			fragment = new CodeFragment();
			break;

		default:
			break;
		}
		return fragment;

	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CONTENT[position];
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

}