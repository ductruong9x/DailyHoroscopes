package com.gamefree.dailyhoroscopes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.gamefree.dailyhoroscopes.adapter.ViewPagerHomeAdapter;
import com.gamefree.dailyhoroscopes.view.OnShareListener;
import com.viewpagerindicator.PagerSlidingTabStrip;

public class DetailActivity extends SherlockFragmentActivity implements
		OnShareListener {

	private ViewPager vpMain;
	private PagerSlidingTabStrip mIndicator;
	private ViewPagerHomeAdapter adapter;
	private ActionBar actionBar;
	private String time, content, post_id;
	private UiLifecycleHelper uiHelper;
	private Session currensesson;
	private int like_count;

	private Session.StatusCallback mCallBack = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			Toast.makeText(DetailActivity.this, "Share", Toast.LENGTH_LONG)
					.show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		uiHelper = new UiLifecycleHelper(this, mCallBack);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		time = bundle.getString("TIME");
		content = bundle.getString("CONTENT");
		post_id = bundle.getString("POST_ID");
		like_count = bundle.getInt("LIKE_COUNT");
		MyApplication.setTime(time);
		MyApplication.setContent(content);
		MyApplication.setPost_id(post_id);
		MyApplication.setLike(like_count);
		actionBar = getSupportActionBar();
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ getString(R.string.detail) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#2357bc")));
		actionBar.setSubtitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ time + "</font>"));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		currensesson = Session.getActiveSession();
		vpMain = (ViewPager) findViewById(R.id.vpMain);
		mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicatorTabHome);
		mIndicator.setIndicatorColor(Color.parseColor("#2357bc"));
		mIndicator.setTextColor(Color.BLACK);
		adapter = new ViewPagerHomeAdapter(DetailActivity.this,
				getSupportFragmentManager(), time, content, post_id, this);
		vpMain.setAdapter(adapter);
		vpMain.setCurrentItem(0);
		mIndicator.setAllCaps(false);
		mIndicator.setIndicatorHeight(6);
		mIndicator.setViewPager(vpMain);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onShare() {
		// TODO Auto-generated method stub
		// if (!Session.getActiveSession().getPermissions()
		// .contains("publish_action")) {
		// final ArrayList<String> permis = new ArrayList<String>();
		// permis.add("publish_action");
		// Session.getActiveSession().requestNewPublishPermissions(
		// new Session.NewPermissionsRequest(DetailActivity.this,
		// permis));
		//
		// } else {
		// Session.openActiveSession(DetailActivity.this, true, mCallBack);
		// }

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);
		uiHelper.onActivityResult(resultCode, requestCode, data);
	}

}
