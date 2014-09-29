package com.whitepowder.skier.basicInformation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;
import com.whitepowder.storage.StorageConstants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

public class BasicInformationForecastActivity extends Activity {

	ListView listViewForecast;
	BasicInformationForecastAdapter forecastListAdapter;
	double coorX;
	double coorY;
	private BasicInformationForecast[] basicInformationForecast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skier_fragment_basic_information_forecast);
		
		listViewForecast=(ListView)findViewById(R.id.listViewForecast);
		
		basicInformationForecast = new BasicInformationForecast[7];
		
		JSONObject jsonObject = null;
		SharedPreferences sharedPrefs = this.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);;
		try{
			jsonObject = new JSONObject(sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_SCHEDULE_KEY,null));
			if(jsonObject != null){
				JSONArray forecastArray = null;
				forecastArray = jsonObject.getJSONArray("list");
				
				for(int i = 0; i < forecastArray.length(); i++){       		 
					BasicInformationForecast pronostico = new BasicInformationForecast();
	        		pronostico.setId(i);    		 
	        		JSONObject forec = (JSONObject) forecastArray.get(i);
	        		
	        		String dt =  forec.getString("dt");      		
	        		long dv = Long.valueOf(dt)*1000;
	        		Date df = new java.util.Date(dv);
	        		pronostico.setFecha(new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(df));     		 
	        		
	        		JSONObject temp = forec.getJSONObject("temp");
	        		pronostico.setTemperaturaMax(temp.getDouble("max"));
	        		pronostico.setTemperaturaMin(temp.getDouble("min"));
	        	
	        		JSONArray weatherArray = forec.getJSONArray("weather");
	        		
	        		JSONObject weather = weatherArray.getJSONObject(0);
	        		pronostico.setWeatherId(weather.getInt("id"));      		 
	        		pronostico.setWeatherMain(weather.getString("main"));
	        		pronostico.setWeatherIcon(weather.getString("icon"));
	        		
	        		basicInformationForecast[i]=pronostico;
				}					
			}else{
				//TODO avisar que no hay forecast disponible
			}
		}  
		catch(JSONException e){	}
			 
		
		
		
		forecastListAdapter = new BasicInformationForecastAdapter(this, basicInformationForecast);
		
		listViewForecast.setAdapter(forecastListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.basic_information_forecast, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
