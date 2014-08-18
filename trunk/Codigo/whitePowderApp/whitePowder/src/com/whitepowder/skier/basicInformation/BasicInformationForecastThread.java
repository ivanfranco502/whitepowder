package com.whitepowder.skier.basicInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.whitepowder.storage.SPStorage;
import com.whitepowder.utils.ApplicationError;

public class BasicInformationForecastThread extends Thread {

	private String ForecastURL = null;
	private ApplicationError mError = null;
	private Context mContext;
	
	public BasicInformationForecastThread(Context context, double ... coor) {
		mContext = context;
		ForecastURL = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+coor[0]+"&lon="+coor[1]+"&units=metric&cnt=7&APPID=6a4c7323eb6848f9d7ac4224402ed312";
	}
	
		
	
	@Override
	public void run() {
		
		HttpURLConnection urlConnection = null;
		try {		
			
			URL url = new URL(ForecastURL);
			urlConnection = (HttpURLConnection) url.openConnection();		
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			InputStream is = urlConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
			String response = reader.readLine();
			JSONObject jsonObject = new JSONObject(response);
			
	        int mensaje  = jsonObject.getInt("cod");
	        
	        if(mensaje==200){
	        	SharedPreferences sp = mContext.getSharedPreferences(SPStorage.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(SPStorage.BASIC_INFORMATION_SCHEDULE, response);
				editor.commit();
	        }
			
		} catch (IOException e) {
			mError = new ApplicationError(600,"Error","IOException en descarga de pronóstico");
		} catch (JSONException e) {
			mError = new ApplicationError(601, "Error", "JSONException en descarga de pronóstico");
		}
		
		finally{
			if(urlConnection!=null){
				urlConnection.disconnect();
			};
		};
	};
}

