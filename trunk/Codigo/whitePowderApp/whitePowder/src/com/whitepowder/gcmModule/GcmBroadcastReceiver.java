package com.whitepowder.gcmModule;

import com.example.whitepowder.R;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	private long[] mVibratePattern = { 0, 200, 200, 300 };
	private int MY_NOTIFICATION_ID = 1;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		Alert alerta = new Alert();
		alerta.setTitle(intent.getStringExtra("title"));
		alerta.setBody(intent.getStringExtra("body"));
		alerta.setUrgency(intent.getStringExtra("urgency"));
		
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
			//	.setTicker(tickerText)
				.setSmallIcon(R.drawable.ic_notif)
				.setContentTitle(alerta.getTitle())
				.setContentText(alerta.body)
			//	.setContentIntent(mContentIntent).setSound(soundURI)
				.setVibrate(mVibratePattern);

		// Pass the Notification to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());	

	}
}
