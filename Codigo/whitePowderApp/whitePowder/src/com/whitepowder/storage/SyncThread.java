package com.whitepowder.storage;

import com.google.gson.Gson;
import com.whitepowder.skier.basicInformation.BasicInformationResponse;
import com.whitepowder.skier.basicInformation.BasicInformationThread;
import com.whitepowder.skier.map.SlopeDownloaderThread;
import com.whitepowder.slopeRecognizer.SimplifiedSlopeDownloaderThread;
import com.whitepowder.slopeRecognizer.SimplifiedSlopeContainer;
import com.whitepowder.userManagement.LoginActivity;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class SyncThread extends AsyncTask<LoginActivity, Void, Void> {

	LoginActivity mContext;
	ApplicationError mError = null;
	Gson gson;
	SharedPreferences sharedPrefs=null;
	public SharedPreferences.Editor editor=null;
	
	@Override
	protected Void doInBackground(LoginActivity... params) {
		mContext = params[0];
		
		//Gets Shared prefs file
		sharedPrefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
		editor = sharedPrefs.edit();
		
		//Loads Gson
		gson = new Gson();
		
		//Starts threads
		SimplifiedSlopeDownloaderThread ssdt = new SimplifiedSlopeDownloaderThread(mContext);
		ssdt.start();
		
		BasicInformationThread bit = new BasicInformationThread(mContext);
		bit.start();
		
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(mContext);
		sdt.start();
		
		//Join threads and check errors
		try {
			sdt.join();
			checkSimplifiedSlopeErrors();
			
			bit.join();
			checkBasicInformationErrors();	
			
			sdt.join();
			//TODO checkerrors
		} 
		catch (InterruptedException e) {
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		boolean status = false;
		if(mError==null){
			status=true;
		};

		mContext.onSyncFinished(status);

	}
	
	private void checkSimplifiedSlopeErrors(){
		SimplifiedSlopeContainer slopeContainer = null;
		
		String slopeContainerText = sharedPrefs.getString(StorageConstants.SIMPLIFIED_SLOPES_KEY,null);
		
		if(slopeContainerText==null){
			mError = new ApplicationError(800,"Error","Error en la sincronización");		
		}
		else{
			slopeContainer = gson.fromJson(slopeContainerText, SimplifiedSlopeContainer.class);
			if(slopeContainer.getCode()!=200){
				if(slopeContainer.getCode()==110){
					mError = new ApplicationError(801,"Error","Usuario no logueado");
					Logout.logout(mContext, true);
				}
				else{
					mError = new ApplicationError(800,"Error","Error en la sincronización");
				};
			};
		};
	}
	
	private void checkBasicInformationErrors() {
		String basicInformationValue = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);
		
		if(basicInformationValue==null){
			mError = new ApplicationError(800,"Error","Error en la sincronización");

    	}
    	//Error handling
    	else{
    		final BasicInformationResponse basicInformationResponse;
    		basicInformationResponse = gson.fromJson(basicInformationValue, BasicInformationResponse.class);
			if(basicInformationResponse.getCode() != 200){
	        	switch(basicInformationResponse.getCode()){
	        		case 110:
	        			mError = new ApplicationError(508,"Error","Token inválido en descarga de información básica");
	        			Logout.logout(mContext, true);
	        			break;
	        		case 116:
	        			mError = new ApplicationError(504, "Error", "No hay información básica en la base de datos");
	        			break;
	        	}
	        }
		}
	}
	

}
