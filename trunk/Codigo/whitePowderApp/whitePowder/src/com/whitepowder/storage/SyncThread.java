package com.whitepowder.storage;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.whitepowder.skier.basicInformation.BasicInformationForecastThread;
import com.whitepowder.skier.basicInformation.BasicInformationResponse;
import com.whitepowder.skier.basicInformation.BasicInformationThread;
import com.whitepowder.skier.map.SlopeDownloaderThread;
import com.whitepowder.slopeRecognizer.SimplifiedSlopeDownloaderThread;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;
import com.whitepowder.utils.ReadFile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class SyncThread extends AsyncTask<Context, Void, Void> {

	private final String IntentOnSyncFinished = "SYNC_FINISHED";
	Context mContext;
	ApplicationError mError = null;
	SharedPreferences sharedPrefs=null;
	Gson gson;
	DataBackup backup;
	
	@Override
	protected Void doInBackground(Context... params) {
		mContext = params[0];
		
		//Creo el backup de lo que tengo levantado y borro todo
		backup = new DataBackup(mContext);
		
		//Gets Shared prefs file
		sharedPrefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
		
		//Loads Gson
		gson = new Gson();
		
		//Logica de reintentos
		for(int trys=0;trys<5;trys++){
			mError=null;
			
			//Starts threads
			SimplifiedSlopeDownloaderThread ssdt = new SimplifiedSlopeDownloaderThread(mContext);
			ssdt.start();
			
			BasicInformationThread bit = new BasicInformationThread(mContext);
			bit.start();
			
			SlopeDownloaderThread sdt = new SlopeDownloaderThread(mContext);
			sdt.start();
			
			//Join threads and check errors
			try {
				ssdt.join();
				checkSimplifiedSlopeErrors();
				
				bit.join();
				checkBasicInformationErrors();	
				
				sdt.join();
				checkSlopeErrors();
			} 
			catch (InterruptedException e) {
				mError = new ApplicationError(800,"Error","Error en la sincronización");
			}
			finally{
				if(mError==null){
					break;
				}
				else{
					try {
						//Si hubo error, espero medio segundo antes de reintentar.
						Thread.sleep(500);
					} 
					catch (InterruptedException e) {
						Log.i("Warning","Error al dormir thread de sincronización");
					};
				};
			};
		};
		return null;
	};
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		boolean status = false;
		if(mError==null){
			status=true;
		}
		else{
			if(mError.getErrorCode()==801){
				Logout.logout(mContext, true);
			}
			backup.restore();
		}
		
		sendOnSyncFinished(status);
	
	};
	
	private void checkSimplifiedSlopeErrors(){
		int code=0;
		String simplifiedSlopeContainerText = sharedPrefs.getString(StorageConstants.SIMPLIFIED_SLOPES_KEY,null);
		
		if(simplifiedSlopeContainerText==null){
			mError = new ApplicationError(800,"Error","Error en la sincronización");		
		}
		else{
			try {
				JSONObject slopeContainer = new JSONObject(simplifiedSlopeContainerText);
				code = slopeContainer.getInt("code");
			} 
			catch (JSONException e) {
				mError = new ApplicationError(800,"Error","Error en la sincronización");
			};
			
			if(code!=200){
				if(code==110){
					mError = new ApplicationError(801,"Error","Usuario no logueado");
				}
				else{
					mError = new ApplicationError(800,"Error","Error en la sincronización");
				};
			};
		};
	};

	
	private void checkBasicInformationErrors() {
		int code=0;
		String basicInformationValue = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);
		
		if(basicInformationValue==null){
			mError = new ApplicationError(800,"Error","Error en la sincronización");
    	}

    	else{
			try {
				JSONObject basicInformationResponse = new JSONObject(basicInformationValue);
				code = basicInformationResponse.getInt("code");
			} 
			catch (JSONException e) {
				mError = new ApplicationError(800,"Error","Error en la sincronización");
			};
    				
			if(code != 200){
	        	switch(code){
	        		case 110:
	        			mError = new ApplicationError(801,"Error","Usuario no logueado");
	        			break;
	        		case 116:
	        			mError = new ApplicationError(504, "Error", "No hay información básica en la base de datos");
	        			break;
        			default:
        				mError = new ApplicationError(800,"Error","Error en la sincronización");
        				break;
	        	};
	        }
			else{
	        	launchForecastThread(gson.fromJson(basicInformationValue, BasicInformationResponse.class));
	        };
		}
	};
	
	private void checkSlopeErrors(){
		int code =0;
		
		String drawableSlopeContainerText = ReadFile.read_file(mContext.getApplicationContext(), StorageConstants.DRAWABLE_SLOPES_FILE);	
		
		if((drawableSlopeContainerText=="")||(drawableSlopeContainerText==null)){
			mError = new ApplicationError(800,"Error","Error en la sincronización");		
		}
		else{
			
			try {
				JSONObject drawableSlopeContainer = new JSONObject(drawableSlopeContainerText);
				code = drawableSlopeContainer.getInt("code");
			} 
			catch (JSONException e) {
				mError = new ApplicationError(800,"Error","Error en la sincronización");
			};
			
			if(code!=200){
				if(code==110){
					mError = new ApplicationError(801,"Error","Usuario no logueado");
				}
				else{
					mError = new ApplicationError(800,"Error","Error en la sincronización");
				};
			};

		};
		
	}
	
	private void launchForecastThread(BasicInformationResponse basicInformationResponse){
		//chequear coor nulls y llamar al forecast.
		double coorX = basicInformationResponse.getBasicInformation().getX();
		double coorY = basicInformationResponse.getBasicInformation().getY();
		if(coorX != 0 || coorY != 0){	
			BasicInformationForecastThread bift = new BasicInformationForecastThread(mContext, coorX, coorY);
			bift.start();
			try{
				bift.join();
			}
			catch(InterruptedException e){
				
			}
		}
	};
	
	private void sendOnSyncFinished(Boolean status){
		//Envio un intent a quien este esperando para que de por finalizada la sync
		Intent intent = new Intent();
		intent.putExtra("success", status);
		intent.setAction(getIntentOnSyncFinishedAction());
		mContext.sendBroadcast(intent);
	};

	public String getIntentOnSyncFinishedAction(){
		return IntentOnSyncFinished;
	};
	

}
