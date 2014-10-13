package com.whitepowder.rescuer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.whitepowder.skier.Coordinate;
import com.whitepowder.skier.MyPosition;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;

public class RescuerService extends Service  {

	private final String PositionUploadURL = BaseTavrosURI.getBaseURI()+"skier/setPosition";
	private final String IntentStopRescuerAction= "STOP_RESCUER";
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    
    private final IBinder mBinder = new LocalBinder();
	private int TIME_BETWEEN_POINTS = 10000;
	
	private Gson gson = new Gson();
	private Context mContext;
	    
    @Override
    public void onCreate() {
    	
    	mContext = getApplicationContext();
		    	
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			stopSelf();
		};
		
		//Enables location listener
		enableLocationListener();	
		
		//Register for location changes	
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_BETWEEN_POINTS, 0, mLocationListener);

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
        RescuerService getService() {
            return RescuerService.this;
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
							
			};
			
			@Override
			public void onProviderDisabled(String arg0) {
				
				//Envio un intent al RescuerActivity para que detenga el envío de info.
				Intent intent = new Intent();
				intent.setAction(IntentStopRescuerAction);
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
		mLocationManager.removeUpdates(mLocationListener);
	};
	
	public void sendPosition(String myPos){
	
		//Hago 5 intentos por posición, si no consigo un OK la desestimo.
		
		for(int trys=0; trys<5 ;trys++){		

			HttpURLConnection connection = null;
			boolean success = false;
			
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
				Log.i("Warning","MalformeURL Exception en Servicio del Rescatista");
			}
			catch (IOException e) {
				Log.i("Warning","IOException en Servicio del Rescatista");
			}
			
			finally{
				if(connection!=null){
					connection.disconnect();
				};
		
			};
			
			if(!success){
				try {
					//Si no se envió la OK la posición, espero 1 seg antes de volver a intentar.
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					Log.i("Warning","Error al dormir thread en Servicio del Rescatista");
				};
			}
			else{
				break;
			};
		};
		
	};
	
	private boolean parseResponse(String response){
		boolean ret = false;
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        int code = jsonObject.getInt("code");	 
			
			if(code==110){
	    		Logout.logout(mContext, false);
	    		ret = true;
			}
			else if(code==200){
				ret = true;
			}
		}
		catch (JSONException e) {
			Log.i("Warning","Respuesta inesperada en response en Servicio del Rescatista");
		};	
		
		return ret;
	};
	
	
	public String getIntentStopRescuerAction(){
		return IntentStopRescuerAction;
	};

		
}