package com.whitepowder.storage;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.ApplicationError;
import com.whitepowder.Logout;
import com.whitepowder.skier.BasicInformationResponse;
import com.whitepowder.skier.BasicInformationThread;
import com.whitepowder.slopeRecognizer.SimplifiedSlopeDownloaderThread;
import com.whitepowder.slopeRecognizer.SlopeContainer;
import com.whitepowder.user.management.LoginActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

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
		sharedPrefs = mContext.getSharedPreferences(SPStorage.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
		editor = sharedPrefs.edit();
		
		//Loads Gson
		gson = new Gson();
		
		//Starts threads
		SimplifiedSlopeDownloaderThread sdt = new SimplifiedSlopeDownloaderThread(mContext);
		sdt.start();
		
		BasicInformationThread bit = new BasicInformationThread(mContext);
		bit.start();
		
		//Join threads and check errors
		try {
			sdt.join();
			checkSimplifiedSlopeErrors();
			
			bit.join();
			checkBasicInformationErrors();	
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
		SlopeContainer slopeContainer = null;
		
		String slopeContainerText = sharedPrefs.getString(SPStorage.SIMPLIFIED_SLOPES,null);
		
		if(slopeContainerText==null){
			mError = new ApplicationError(800,"Error","Error en la sincronización");		
		}
		else{
			slopeContainer = gson.fromJson(slopeContainerText, SlopeContainer.class);
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
		String basicInformationValue = sharedPrefs.getString(SPStorage.BASIC_INFORMATION,null);
		
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
