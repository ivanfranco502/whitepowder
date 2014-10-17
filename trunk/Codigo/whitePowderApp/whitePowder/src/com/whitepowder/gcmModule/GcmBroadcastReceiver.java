package com.whitepowder.gcmModule;

import com.example.whitepowder.R;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.userManagement.User;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	private long[] mVibratePattern = { 0, 200, 200, 300 };
	private int MY_NOTIFICATION_ID = 1;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		if(User.getUserInstance().getRole().toString().equals("ROLE_SKIER")){
			Intent notifyAppIntent = new Intent(SkierActivity.GCM_ALERT_INTENT_ACTION);
			notifyAppIntent.putExtra("title", intent.getStringExtra("title"));
			notifyAppIntent.putExtra("body", intent.getStringExtra("body"));
			notifyAppIntent.putExtra("id", intent.getIntExtra("id", -1));
			
			context.sendOrderedBroadcast(
					notifyAppIntent, 
					null,
					new BroadcastReceiver() {
	
						@Override
						public void onReceive(Context context, Intent intent) {
	
							if (getResultCode() != Activity.RESULT_OK) {
								PendingIntent pendingIntent=null;
								
								Intent displayIntent = new Intent(context, AlertDisplayActivity.class);
								displayIntent.putExtras(intent.getExtras());
								
								pendingIntent = PendingIntent.getActivity(context, 0, displayIntent, 0);
								
								NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
									
									.setSmallIcon(R.drawable.ic_notif)
									.setContentTitle(intent.getStringExtra("title"))
									.setContentText(intent.getStringExtra("body"))
									.setContentIntent(pendingIntent)
									.setAutoCancel(true)
									.setVibrate(mVibratePattern);
				
							
									//TODO set sound alert
									//.setSound(soundURI)
	
								// Pass the Notification to the NotificationManager:
								NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);											
								mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());	
								
							}
						}
					}, 
					null, 
					0, 
					null, 
					null);
		}
	}
}
