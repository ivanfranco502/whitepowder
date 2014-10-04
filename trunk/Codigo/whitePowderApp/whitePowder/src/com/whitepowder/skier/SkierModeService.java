package com.whitepowder.skier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.userManagement.User;
import com.whitepowder.userManagement.UserStatistics;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class SkierModeService extends Service  {

	private final String PositionUploadURL = BaseTavrosURI.getBaseURI()+"skier/setPosition";
	private final String IntentStopSkierModeAction= "STOP_SKIER_MODE";
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    
    private final IBinder mBinder = new LocalBinder();
	private int TIME_BETWEEN_POINTS = 4000;
	
	private Gson gson = new Gson();
	private Context mContext;
	
	SharedPreferences sharedPrefs=null;
	SharedPreferences.Editor editor = null;
	UserStatistics userStats=null;
	int count=0;
    
    @Override
    public void onCreate() {
    	
    	mContext = getApplicationContext();
    	
		//Gets Shared prefs file
		sharedPrefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
		editor=sharedPrefs.edit();
		
		//Loads User Stadistics from file
		loadStatsFromFile();
    	
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			stopSelf();
		};
		
		//Enables location listener
		enableLocationListener();	
		
		//Register for location changes	
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_BETWEEN_POINTS, 0,mLocationListener);

    }
	
	@Override
	public IBinder onBind(Intent intent) {
	
		return mBinder;
	}
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
    
	
    public class LocalBinder extends Binder {
        SkierModeService getService() {
            return SkierModeService.this;
        }
    };
    
	private void enableLocationListener(){
		mLocationListener = new LocationListener() {

			// Called back when location changes

			@Override
			public void onLocationChanged(final Location loc) {
				
				new Thread(new Runnable() {					
					@Override
					public void run() {
						
						MyPosition pos = new MyPosition(User.getUserInstance().getToken(), new Coordinate(loc.getLatitude(), loc.getLongitude()));
						String myPosString = gson.toJson(pos);					
						sendPosition(myPosString);
						
						
					};
				}).start();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						refreshStats(loc);
						
					};
				}).start();				
			};
			
			@Override
			public void onProviderDisabled(String arg0) {
				
				//Envio un intent al SkierActivity para que detenga el skier mode.
				Intent intent = new Intent();
				intent.setAction(IntentStopSkierModeAction);
				sendBroadcast(intent);
				
			};

			@Override
			public void onProviderEnabled(String arg0) {
				//Nothing to be done
				return;
			};

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				//Nothing to be done				
			};
		};

	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		persistStatistics();
		mLocationManager.removeUpdates(mLocationListener);
	};
	
	public void sendPosition(String myPos){
	
		//Hago 5 intentos por posici�n, si no consigo un OK la desestimo.
		
		for(int trys=0; trys<5 ;trys++){		

			HttpURLConnection connection = null;
			Boolean success= false;
			
			try{
				
				URL url = new URL(PositionUploadURL);
				connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
				
			    connection.setUseCaches (false);
			    connection.setDoInput(true);
			    connection.setDoOutput(true);
			    
			    //Send request
			    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
			    wr.writeBytes(myPos);
			    wr.flush();
			    wr.close();
			        
			    if(connection.getResponseCode()==200){
				    
			    	//Get Response
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
					String response = reader.readLine();
					
					success = parseResponse(response);
									
			    };
			}
	    
			catch (MalformedURLException e) {
				Log.i("Warning","MalformeURL Exception en Servicio del Modo Esquiador");
			}
			catch (IOException e) {
				Log.i("Warning","IOException en Servicio del Modo Esquiador");
			}
			
			finally{
				if(connection!=null){
					connection.disconnect();
				};
		
			};
			
			if(!success){
				try {
					//Si no se envi� la OK la posici�n, espero 1 seg antes de volver a intentar.
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					Log.i("Warning","Error al dormir thread en Servicio del Modo Esquiador");
				};
			}
			else{
				break;
			};
		};
		
	};
	
	private Boolean parseResponse(String response){
		Boolean ret=false;
		
		PositionUploadResponse por = gson.fromJson(response, PositionUploadResponse.class);
		
		if(por.code==110){
    		Logout.logout(mContext, false);
    		ret=true;
		}
		else if(por.code==200){
			ret=true;
		}
		
		return ret;
	};
	
	private void loadStatsFromFile(){
		String statsText = sharedPrefs.getString(StorageConstants.USER_STATISTICS_KEY, null);
		if(statsText!=null){
			userStats = gson.fromJson(statsText, UserStatistics.class);
		}
		else{
			userStats = UserStatistics.getInstance();
		}
	};
	
	public void refreshStats(Location loc){
		if(userStats!=null){
			
			//Check and updates max speed
			if(loc.getSpeed() > userStats.getMaxSpeed()){
				userStats.setMaxSpeed(loc.getSpeed());
				userStats.setMaxSpeedDate(Calendar.getInstance().getTime());
			};
			
			//Check and updates max altitude
			if(loc.getAltitude() > userStats.getMaxAltitude()){
				userStats.setMaxAltitude(loc.getAltitude());
				userStats.setMaxAltitudeDate(Calendar.getInstance().getTime());				
			};
			
			//Updates average speed
			float speed = loc.getSpeed()/1000*3600;
			
			if(speed>=10){
				userStats.setAverageSpeed(((userStats.getAverageSpeed()*userStats.getSpeedMeditions())+speed)/userStats.getSpeedMeditions()+1);
				userStats.setSpeedMeditions(userStats.getSpeedMeditions()+1);
			};
			
			//Updates total distance
			if(userStats.getLastKnownLocation()!=null){
				userStats.setTotalDistance(userStats.getTotalDistance()+loc.distanceTo(userStats.getLastKnownLocation()));
			};
			
			//Sometimes persist statistics
			if((count%4)==0){
				persistStatistics();
				
			};
			count++;
			
		};
		
	};
	
	public void persistStatistics(){
		editor.putString(StorageConstants.USER_STATISTICS_KEY, gson.toJson(userStats));
		editor.commit();
	};
	
	public String getIntentStopSkierModeAction(){
		return IntentStopSkierModeAction;
	};

		
}




