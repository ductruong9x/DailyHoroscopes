package com.appfree.dailyhoroscopes;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appfree.dailyhoroscopes.adapter.ViewPagerAdapter;
import com.appfree.dailyhoroscopes.service.LocalPushService;
import com.appfree.dailyhoroscopes.util.Util;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.viewpagerindicator.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {
	private ActionBar actionBar;
	private boolean checknoti;
	private ViewPager vpMain;
	private PagerSlidingTabStrip mIndicator;
	private ViewPagerAdapter adapter;
	private String dev_id="108403113";
	private String app_id="207806321";
	private StartAppAd startappad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.app_color);
			tintManager.setNavigationBarTintResource(R.color.app_color);
		}
		StartAppSDK.init(this, dev_id, app_id);
		setContentView(R.layout.fragment_detail);
		StartAppAd.showSlider(this);
		// setup action bar
		startappad=new StartAppAd(this);
		startappad.loadAd();
		SharedPreferences sharedPreferences = getSharedPreferences("SETTING",
				MODE_PRIVATE);
		checknoti = sharedPreferences.getBoolean("NOTI", true);
		if (checknoti) {
			pushLocal();
		}
		actionBar = getActionBar();
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ getString(R.string.app_name) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#e91e63")));
		vpMain = (ViewPager) findViewById(R.id.vpMain);
		mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicatorTabHome);
		mIndicator.setBackgroundResource(R.color.app_color);
		mIndicator.setIndicatorColor(Color.parseColor("#FFFFFF"));
		mIndicator.setTextColorResource(R.color.white);
		adapter = new ViewPagerAdapter(MainActivity.this,
				getSupportFragmentManager());
		vpMain.setAdapter(adapter);
		vpMain.setCurrentItem(0);
		mIndicator.setAllCaps(false);
		mIndicator.setIndicatorHeight(6);
		mIndicator.setViewPager(vpMain);
		if (!Util.isOnline(this)) {
			finish();
			Toast.makeText(MainActivity.this,
					"Cần có kết nối Internet để sử dụng", Toast.LENGTH_LONG)
					.show();
			return;
		}

		danhGia();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	public void pushLocal() {
		Intent intent = new Intent(MainActivity.this, LocalPushService.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(
				MainActivity.this, 0, intent, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.setup:

			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.invate:
			sendRequestDialog();
			Toast.makeText(MainActivity.this, "Invate friend from Facebook",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.rate:
			Toast.makeText(MainActivity.this, "Thank you", Toast.LENGTH_SHORT)
					.show();
			Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
					.parse("market://details?id=" + getPackageName()));
			startActivity(goToMarket);
			break;
		case R.id.devinfo:
			Intent goMoreApp = new Intent(Intent.ACTION_VIEW)
					.setData(Uri
							.parse("https://play.google.com/store/apps/developer?id=Appfree+Center"));
			startActivity(goMoreApp);
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void sendRequestDialog() {
		Bundle params = new Bundle();
		params.putString("message", "Sử dụng Tử vi hàng ngày cùng mình nhé");

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
				MainActivity.this, Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Network Error", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							final String requestId = values
									.getString("request");
							if (requestId != null) {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request sent", Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(
										MainActivity.this
												.getApplicationContext(),
										"Request cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}

				}).build();
		requestsDialog.show();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	public void danhGia() {
		SharedPreferences getPre = getSharedPreferences("SAVE", MODE_PRIVATE);
		int i = getPre.getInt("VOTE", 0);
		SharedPreferences pre;
		SharedPreferences.Editor edit;
		switch (i) {
		case 0:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", 1);
			edit.commit();
			break;
		case 1:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 2:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 3:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 4:
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", i + 1);
			edit.commit();
			break;
		case 5:
			DialogVote dialog = new DialogVote(MainActivity.this);
			dialog.show();
			pre = getSharedPreferences("SAVE", MODE_PRIVATE);
			edit = pre.edit();
			edit.putInt("VOTE", 5);
			edit.commit();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startappad.onBackPressed();
		super.onBackPressed();
	}
}
