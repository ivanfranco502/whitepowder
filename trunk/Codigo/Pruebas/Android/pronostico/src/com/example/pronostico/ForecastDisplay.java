package com.example.pronostico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ForecastDisplay extends AsyncTask<Double, Void, Void> {

	Forecast[] forecast = new Forecast[7];
	MainActivity mContext;
	
	public ForecastDisplay(MainActivity con){
		mContext = con;

	}
	
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.       
	}
	@Override
	protected Void doInBackground(Double...coordenadas) {
		
		URL url;
		try {
		url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat="+coordenadas[0].toString()+"&lon="+coordenadas[1].toString()+"&units=metric&cnt=7");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();		
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			InputStream is = urlConnection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
			String response = reader.readLine();
			parseResponse(response);	
			
		} catch (IOException e) {
			Log.i("Error","Failed receiving information from weather server.");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.i("Error","Failed parsing JSON.");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	void parseResponse(String json) throws JSONException{
	
		JSONObject jsonObject = new JSONObject(json);
		
        int mensaje  = jsonObject.getInt("cod");
        
        if(mensaje==200){
        	 JSONArray forecastArray = jsonObject.getJSONArray("list");
        	 
        	 for(int i=0;i<forecastArray.length();i++){
        		 Forecast pronostico = new Forecast();
        		 pronostico.setId(i);    		 
        		 JSONObject forec = (JSONObject) forecastArray.get(i);
        		 
        		 String dt =  forec.getString("dt");      		
        		 long dv = Long.valueOf(dt)*1000;
        		 Date df = new java.util.Date(dv);
        		 pronostico.setFecha(new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(df));     		 
        		 
        		 JSONObject temp = forec.getJSONObject("temp");
        		 pronostico.setTemperaturaMax(temp.getDouble("max"));
        		 pronostico.setTemperaturaMin(temp.getDouble("min"));
        		 
        		 pronostico.setPresion(forec.getDouble("pressure"));
        		 pronostico.setHumedad(forec.getDouble("humidity"));
        		 
        		 JSONArray weatherArray = forec.getJSONArray("weather");
        		 
        		 //Raro, xq es array?
        		 
        		 JSONObject weather = weatherArray.getJSONObject(0);
        		 pronostico.setWeatherId(weather.getInt("id"));      		 
        		 pronostico.setWeatherMain(weather.getString("main"));
        		 pronostico.setWeatherIcon(weather.getString("icon"));
        		 
        		 forecast[i]=pronostico;

        	 }
        }
        else{
        	Log.i("Error","Failed receiving information from weather server.");
        }
        
	}
	
    protected void onPostExecute(Void unused) {
    	
    	ListView listview = (ListView) mContext.findViewById(R.id.list);
    	ForecastArrayAdapter adapter = new ForecastArrayAdapter(mContext, R.layout.forecast_row, forecast);

    	listview.setAdapter(adapter);
    	

   }
}
