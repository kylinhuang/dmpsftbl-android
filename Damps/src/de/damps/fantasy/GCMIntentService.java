package de.damps.fantasy;

import static de.damps.fantasy.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import de.damps.fantasy.activities.HomeActivity;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(SENDER_ID);
	}

	private static final String TAG = "===GCMIntentService===";
	private static final int HELLO_ID = 1;

	@Override
	protected void onError(Context arg0, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		Bundle extras = arg1.getExtras();
		String msg = extras.getString("message");
		String ttxt = extras.getString("tickerText");
		String ctitle = extras.getString("contentTitle");
		String ctxt = extras.getString("contentText");

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		if (msg.equals("message")) {
			int icon = R.drawable.ff;
			CharSequence tickerText = ttxt;
			long when = System.currentTimeMillis();

			Notification notification = new Notification(icon, tickerText, when);

			Context context = getApplicationContext();
			CharSequence contentTitle = ctitle;
			CharSequence contentText = ctxt;
			Intent notificationIntent = new Intent(this, HomeActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);

			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);

			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_SOUND;
			mNotificationManager.notify(HELLO_ID, notification);
		}

	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "unregistered = " + arg1);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

}
