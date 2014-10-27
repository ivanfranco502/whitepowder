package com.whitepowder.rescuer;

import com.example.whitepowder.R;
import com.whitepowder.gcmModule.AlertDisplayActivity;
import com.whitepowder.userManagement.User;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

public class RescuerGcmBroadcastReceiver extends BroadcastReceiver {

	private long[] mVibratePattern = { 0, 200, 200, 300 };
	private int ALERT_NOTIFICATION_ID = 1;
	private int ACCIDENT_NOTIFICATION_ID = 2;
	private Context mContext;
	private Intent mIntent;
	
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
		
		if(role.equals("ROLE_RESCU")){
		
			mContext =  context;
			mIntent = intent;
			
			if((Integer.parseInt(intent.getStringExtra("id")))==100){
				//It's an accident! OMG!
				processAccident();
			}
			else{
				//It's an annoying alert, fuck it!
				processAlert();
			}
		}
	}
	
	
	private void processAlert(){
		Intent notifyAppIntent = new Intent(RescuerActivity.GCM_ALERT_INTENT_ACTION);
		notifyAppIntent.putExtra("title", mIntent.getStringExtra("title"));
		notifyAppIntent.putExtra("body", mIntent.getStringExtra("body"));
		notifyAppIntent.putExtra("id", Integer.parseInt(mIntent.getStringExtra("id")));
		
		mContext.sendOrderedBroadcast(notifyAppIntent, null,	
			new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (getResultCode() != Activity.RESULT_OK) {
						PendingIntent pendingIntent = null;
						
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
						mNotificationManager.notify(ALERT_NOTIFICATION_ID, notificationBuilder.build());
					}
				}
			}, null, 0, null, null);
	}
	
	private void processAccident(){
		Intent notifyAppIntent = new Intent(RescuerActivity.GCM_ACCIDENT_INTENT_ACTION);
		/*notifyAppIntent.putExtra("title", mIntent.getStringExtra("title"));
		notifyAppIntent.putExtra("body", mIntent.getStringExtra("body"));
		notifyAppIntent.putExtra("id", mIntent.getIntExtra("id", -1));*/
		
		mContext.sendOrderedBroadcast(notifyAppIntent, null,	
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
							/*.setContentTitle(intent.getStringExtra("title"))
							.setContentText(intent.getStringExtra("body"))*/
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
	}
}
