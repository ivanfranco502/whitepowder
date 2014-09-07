package com.whitepowder.gcmModule;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCM {
	
	Context mContext;
	String SENDER_ID = "1025941805069";
	GoogleCloudMessaging gcm;

	
	public GCM(Context ctx) {
		mContext = ctx;
		gcm = GoogleCloudMessaging.getInstance(mContext);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						String registrationId = gcm.register(SENDER_ID);
						Log.i("ads","sdfsd");
					} 
					catch (IOException e) {
						//TODO set error
						Log.i("error", "sdasdada");
					}					
				}
			}).start();			
	} 
}
