package com.appfree.dailyhoroscopes.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appfree.dailyhoroscopes.MainActivity;
import com.appfree.dailyhoroscopes.R;

public class NotificationHelper {
	private Context context;
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	private PendingIntent mContentIntent;
	private int mNOTIFICATION_ID = 1;

	public NotificationHelper(Context context) {
		this.context = context;

	}

	@SuppressWarnings("deprecation")
	public void onCreateNotificaion() {

		String contentText = context.getString(R.string.content_text);
		String title = context.getString(R.string.app_name);
		long when = System.currentTimeMillis();
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = new Notification(R.drawable.ic_launcher, contentText,
				when);
		Intent notificationIntent = new Intent();
		mContentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		mNotification.setLatestEventInfo(context, title, contentText,
				mContentIntent);
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.contentIntent = pIntent;
		mNotificationManager.notify(mNOTIFICATION_ID, mNotification);

	}
}
