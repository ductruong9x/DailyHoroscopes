package com.gamefree.dailyhoroscopes.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gamefree.dailyhoroscopes.R;
import com.gamefree.dailyhoroscopes.fragment.CommentFragment;
import com.gamefree.dailyhoroscopes.fragment.DetialFragment;
import com.gamefree.dailyhoroscopes.view.OnShareListener;

public class ViewPagerHomeAdapter extends FragmentPagerAdapter {
	private Context context;
	private String[] CONTENT;
	private String time, content, post_id;
	private OnShareListener onShare;

	public ViewPagerHomeAdapter(Context context, FragmentManager fm,
			String time, String content, String post_id,
			OnShareListener onShareListener) {

		super(fm);
		this.context = context;
		CONTENT = new String[] { context.getString(R.string.detail),
				context.getString(R.string.comment), };
		this.onShare = onShareListener;
	}

	Fragment fragment;

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
		if (position == 0) {
			DetialFragment detialFragment = (DetialFragment) object;
			detialFragment.setOnShare(onShare);
			detialFragment.init();
		} else if (position == 1) {
			CommentFragment commentFragment = (CommentFragment) object;
			commentFragment.init();
		}

	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			fragment = new DetialFragment();

			break;
		case 1:
			fragment = new CommentFragment();
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