package com.whitepowder.storage;

import com.whitepowder.slope.recognizer.SlopeDownloaderThread;
import com.whitepowder.user.management.LoginActivity;

import android.os.AsyncTask;

public class SyncThread extends AsyncTask<LoginActivity, Void, Void> {

	LoginActivity mContext;
	
	@Override
	protected Void doInBackground(LoginActivity... params) {
		mContext = params[0];
		
		//Starts threads
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(mContext);
		sdt.start();
		
		try {
			sdt.join();
		} 
		catch (InterruptedException e) {
			//TODO handle error
		}
		
		mContext.onSyncFinished();
		
		return null;
	}
	

}
