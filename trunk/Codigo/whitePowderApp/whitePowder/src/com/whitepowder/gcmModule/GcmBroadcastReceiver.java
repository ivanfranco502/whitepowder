package com.whitepowder.gcmModule;

import com.example.whitepowder.R;
import com.whitepowder.rescuer.RescuerActivity;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.userManagement.User;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	private long[] mVibratePattern = { 0, 200, 200, 300 };
	private int MY_NOTIFICATION_ID = 1;
	private int ACCIDENT_NOTIFICATION_ID = 2;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String role=null;
		if(User.getUserInstance().getRole()==null){		
			SharedPreferences sharedPreferences = context.getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
			role = sharedPreferences.getString("role", "UNKNOWN");

		}
		else{
			role=User.getUserInstance().getRole();
		};
		
		int code = Integer.parseInt(intent.getStringExtra("id"));
		
		if((code==100)||(code==101)){
			//It's an accident! OMG!
			if(role.equals("ROLE_RESCU")){
				processAccident(context, intent);
			};
		}
		else{
			if(role.equals("ROLE_RESCU")||(role.equals("ROLE_SKIER"))){
				procesAlert(context, intent);
			};
			
		};

	};
	
	private void processAccident(Context context, Intent intent){
		int code = Integer.parseInt(intent.getStringExtra("id"));
		
		Intent notifyAppIntent = new Intent(RescuerActivity.GCM_ACCIDENT_INTENT_ACTION);
		notifyAppIntent.putExtra("title", intent.getStringExtra("title"));
		notifyAppIntent.putExtra("body", intent.getStringExtra("body"));
		
		if(code==100){
			notifyAppIntent.putExtra("action","add");
		}
		else if(code==101){
			notifyAppIntent.putExtra("action", "remove");
		};
		
		
		context.sendOrderedBroadcast(notifyAppIntent, null,	
			new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					
					if (getResultCode() != Activity.RESULT_OK) {
						PendingIntent pendingIntent=null;
						
						Intent displayIntent = new Intent(context, RescuerActivity.class);
						displayIntent.putExtras(intent.getExtras());
						pendingIntent = PendingIntent.getActivity(context, 0, displayIntent, 0);
						NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
							.setSmallIcon(R.drawable.ic_accident)
							.setContentIntent(pendingIntent)
							.setAutoCancel(true)
							.setVibrate(mVibratePattern);
		
					
							//TODO set sound alert
							//.setSound(soundURI)

						// Pass the Notification to the NotificationManager:
						NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);											
						mNotificationManager.notify(ACCIDENT_NOTIFICATION_ID, notificationBuilder.build());
					}
				}
			}, null, 0, null, null);
	};
	
	private void procesAlert(Context context, Intent intent){
		final int code = Integer.parseInt(intent.getStringExtra("id"));
		Intent notifyAppIntent = new Intent(SkierActivity.GCM_ALERT_INTENT_ACTION);
		notifyAppIntent.putExtra("title", intent.getStringExtra("title"));
		notifyAppIntent.putExtra("body", intent.getStringExtra("body"));
		notifyAppIntent.putExtra("id",code);
		
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
							
							Uri soundUri = null;
							switch(code){
							case 1:
								soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.storm);
								break;
							case 2:
								soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.avalanche);
								break;
							case 3:
								soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.clown);
								break;
							};
							
							
							NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
								
								.setSmallIcon(R.drawable.ic_notif)
								.setContentTitle(intent.getStringExtra("title"))
								.setContentText(intent.getStringExtra("body"))
								.setContentIntent(pendingIntent)
								.setAutoCancel(true)
								.setVibrate(mVibratePattern);
							if(soundUri != null){
								notificationBuilder.setSound(soundUri);
							}

							// Pass the Notification to the NotificationManager:
							NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);											
							mNotificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build());	
							
						}
					}
				}, null, 0, null, null);
	};
		
		

}
