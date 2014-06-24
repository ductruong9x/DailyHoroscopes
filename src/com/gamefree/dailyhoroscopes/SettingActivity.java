package com.gamefree.dailyhoroscopes;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.Html;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.gamefree.dailyhoroscopes.service.LocalPushService;

public class SettingActivity extends SherlockPreferenceActivity {
	private ActionBar actionBar;
	private CheckBoxPreference switchNotifi, switchLoadImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff' size='25'>"
				+ getString(R.string.caidat) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#2357bc")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.setting);
		SharedPreferences sharedPreferences = getSharedPreferences("SETTING",
				MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPreferences.edit();
		switchNotifi = (CheckBoxPreference) findPreference("caidatthongbao");
		switchLoadImage = (CheckBoxPreference) findPreference("loadanh");
		switchLoadImage.setChecked(sharedPreferences.getBoolean("LOAD", true));
		switchNotifi.setChecked(sharedPreferences.getBoolean("NOTI", true));
		switchLoadImage
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						switchLoadImage.setChecked(Boolean.valueOf(newValue
								.toString()));
						if (Boolean.valueOf(newValue.toString())) {
							pushLocal();
						} else {
							cancelAlarmNoti();
						}
						SharedPreferences sharedPreferences = getSharedPreferences(
								"SETTING", MODE_PRIVATE);
						SharedPreferences.Editor edit = sharedPreferences
								.edit();
						edit.putBoolean("LOAD",
								Boolean.valueOf(newValue.toString()));
						edit.commit();

						return false;
					}
				});
		switchNotifi
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						switchNotifi.setChecked(Boolean.valueOf(newValue
								.toString()));
						SharedPreferences sharedPreferences = getSharedPreferences(
								"SETTING", MODE_PRIVATE);
						SharedPreferences.Editor edit = sharedPreferences
								.edit();
						edit.putBoolean("NOTI",
								Boolean.valueOf(newValue.toString()));
						edit.commit();
						return false;
					}
				});

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

	public void cancelAlarmNoti() {

		Intent intent = new Intent(SettingActivity.this, LocalPushService.class);
		PendingIntent sender = PendingIntent.getService(SettingActivity.this,
				0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);

	}

	public void pushLocal() {
		Intent intent = new Intent(SettingActivity.this, LocalPushService.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(
				SettingActivity.this, 0, intent, 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
	}
}
