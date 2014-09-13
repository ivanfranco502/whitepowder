package com.whitepowder.skier;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SkierModeService extends Service  {


	private Context mContext;

	@Override
	public IBinder onBind(Intent intent) {
		mContext = getApplicationContext();
		return null;
	}
		
}

	

