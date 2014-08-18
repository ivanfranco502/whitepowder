package com.whitepowder.skier.basicInformation;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ApplicationError;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BasicInformationFragment extends Fragment{
	
	private TextView ski_center_name;
	private TextView ski_center_location;
	private TextView ski_center_amenities;
	private TextView ski_center_minimum_height;
	private TextView ski_center_maximum_height;
	private TextView ski_center_season;
	private ListView ski_center_schedule;
	private TextView ski_center_details;
	
	private TextView forecast_date;
	private TextView forecast_description;
	private TextView forecast_min_temp;
	private TextView forecast_max_temp;
	private ImageView forecast_icon;
	private TextView forecast_see_extended;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_basic_information, container,
				false);
		
		ski_center_name = (TextView) rootView.findViewById(R.id.ski_center_name);
		ski_center_location = (TextView) rootView.findViewById(R.id.ski_center_location);
		ski_center_amenities = (TextView) rootView.findViewById(R.id.ski_center_amenities);
		ski_center_minimum_height = (TextView) rootView.findViewById(R.id.ski_center_minimum_height);
		ski_center_maximum_height = (TextView) rootView.findViewById(R.id.ski_center_maximum_height);
		ski_center_season = (TextView) rootView.findViewById(R.id.ski_center_season);
		ski_center_schedule = (ListView) rootView.findViewById(R.id.ski_center_schedule);
		ski_center_details = (TextView) rootView.findViewById(R.id.ski_center_details);
		
		forecast_date = (TextView) rootView.findViewById(R.id.forecast_date);
		forecast_description = (TextView) rootView.findViewById(R.id.forecast_description);
		forecast_min_temp = (TextView) rootView.findViewById(R.id.forecast_min_temp);
		forecast_max_temp = (TextView) rootView.findViewById(R.id.forecast_max_temp);
		forecast_icon = (ImageView) rootView.findViewById(R.id.forecast_icon);
		forecast_see_extended = (TextView) rootView.findViewById(R.id.forecast_see_extended);		
		
		forecast_see_extended.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//launch intent with the extended forecast
			}
		});
		
		return rootView;
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		fillUIFields();
	}

	private void fillUIFields() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);;
		Gson gson = new Gson();
		String basicInformationValue = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);
		
		if(basicInformationValue==null){
			

    	}
    	else{
    		final BasicInformationResponse basicInformationResponse;
    		basicInformationResponse = gson.fromJson(basicInformationValue, BasicInformationResponse.class);
		
			String skiCenterName = basicInformationResponse.getBasicInformation().getCenterName();
			if(skiCenterName != null){
				ski_center_name.setText(skiCenterName);
			}
			
			String skiCenterLocation = basicInformationResponse.getBasicInformation().getLocation();
			if(skiCenterLocation != null){
				ski_center_location.setText(skiCenterLocation);
			}
			
			String skiCenterAmenities = basicInformationResponse.getBasicInformation().getAmenities();
			if(skiCenterAmenities != null){
				ski_center_amenities.setText(skiCenterAmenities);
			}
			
			String skiCenterMinimumHeight = basicInformationResponse.getBasicInformation().getMinimumHeight();
			if(skiCenterMinimumHeight != null){
				ski_center_minimum_height.setText(skiCenterMinimumHeight);
			}
			
			String skiCenterMaximumHeight = basicInformationResponse.getBasicInformation().getMaximumHeight();
			if(skiCenterMaximumHeight != null){
				ski_center_maximum_height.setText(skiCenterMaximumHeight);
			}
			
			String skiCenterSeason = basicInformationResponse.getBasicInformation().getSeason();
			if(skiCenterSeason != null){
				ski_center_season.setText(skiCenterSeason);
			}
			
			
			// Create The Adapter with passing ArrayList as 3rd parameter
			ArrayList<Day> days = basicInformationResponse.getBasicInformation().getDays();
			if(days != null){
				ArrayList<String> arraySchedules = new ArrayList<String>();
				for(int i = 0; i < days.size(); i++ ){
					arraySchedules.add(days.get(i).toString());
				}
				ArrayAdapter<String> arrayAdapter =      
				new ArrayAdapter<String>(this.getActivity(),R.layout.skier_fragment_basic_information_schedule_row, arraySchedules);
				// Set The Adapter
				ski_center_schedule.setAdapter(arrayAdapter); 
			}
			
			String skiCenterDetails = basicInformationResponse.getBasicInformation().getDetails();
			if(skiCenterDetails != null){
				ski_center_details.setText(skiCenterDetails);
				
			//chequear coor nulls y llamar al forecast.
				double coorX = basicInformationResponse.getBasicInformation().getX();
				double coorY = basicInformationResponse.getBasicInformation().getY();
				
				BasicInformationForecastThread bift = new BasicInformationForecastThread(getActivity(), coorX, coorY);
				bift.start();
				try{
					bift.join();
				}
				catch(InterruptedException e){
					
				}
				JSONObject jsonObject = null;
				try{
					jsonObject = new JSONObject(sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_SCHEDULE_KEY,null));
					if(jsonObject != null){
						JSONArray forecastArray = null;
						forecastArray = jsonObject.getJSONArray("list");
						
						if(forecastArray != null){       		 
			        		 JSONObject forec= null;
			        		 forec = (JSONObject) forecastArray.get(0);
			        		 
			        		 String dt =  forec.getString("dt");      		
			        		 long dv = Long.valueOf(dt)*1000;
			        		 Date df = new java.util.Date(dv);
			        		 forecast_date.setText((new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(df)).toString());     		 
			        		 
			        		 JSONObject temp = forec.getJSONObject("temp");
			        		 Double tempMin = temp.getDouble("min");
			        		 forecast_min_temp.setText(tempMin.toString());
			        		 Double tempMax = temp.getDouble("max");
			        		 forecast_max_temp.setText(tempMax.toString());
			        		 
			        		 
			        		 JSONArray weatherArray = forec.getJSONArray("weather");
			        		 
			        		 JSONObject weather = weatherArray.getJSONObject(0);
			        		 forecast_description.setText(weather.getString("main"));     		 
			        		 
			        		 try {
			        				InputStream logoBitmap = getActivity().getAssets().open("weatherIcons/"+weather.getString("icon")+".png");
			        				Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			        				forecast_icon.setImageBitmap(bitmap);
			        				
			        			} catch (IOException e) {
			        				new ApplicationError(602, "Error","No se encuentra el icono del pronostico");
			        			}
						}
					}
				}  
				catch(JSONException e){	}
				} 

				
				
			}
		}
	}