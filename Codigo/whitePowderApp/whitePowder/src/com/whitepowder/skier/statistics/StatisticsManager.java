package com.whitepowder.skier.statistics;

import java.util.Calendar;
import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class StatisticsManager{
	
	private static StatisticsManager instance;
	Gson gson = new Gson();
	SharedPreferences sharedPrefs=null;
	SharedPreferences.Editor editor =null;
	static Context mContext;
	UserStatistics stats;

	int count=1;

	
	private StatisticsManager() {
		

		sharedPrefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
		editor=sharedPrefs.edit();
		
		String statsText = sharedPrefs.getString(StorageConstants.USER_STATISTICS_KEY, null);
		if(statsText!=null){
			stats = gson.fromJson(statsText, UserStatistics.class);

		}
		else{
			stats = new UserStatistics();
		};
	};
	
	public void persistStatistics(){

		editor.putString(StorageConstants.USER_STATISTICS_KEY, gson.toJson(stats));
		editor.commit();
	};
	
	
	public void clearStats(){
		stats = new UserStatistics();
		
		persistStatistics();
	};
	
	public void updateStatistics(Location loc){
		
		//Converts from m/s to km/h
		float speed = loc.getSpeed()/1000*3600;
	
		
		//Check and updates max speed
		if(speed > stats.getMaxSpeed()){
			stats.setMaxSpeed(speed);
			stats.setMaxSpeedDate(Calendar.getInstance().getTime());
		};
		
		//Check and updates max altitude
		if(loc.getAltitude() > stats.getMaxAltitude()){
			stats.setMaxAltitude(loc.getAltitude());
			stats.setMaxAltitudeDate(Calendar.getInstance().getTime());				
		};
		
		//Updates average speed
		
		if(speed>=10){
			stats.setAverageSpeed(((stats.getAverageSpeed()*stats.getSpeedMeditions())+speed)/(stats.getSpeedMeditions()+1));
			stats.setSpeedMeditions(stats.getSpeedMeditions()+1);
		};
		
		//Updates total distance
		if(stats.getLastKnownLocation()!=null){
			stats.setTotalDistance(stats.getTotalDistance()+(loc.distanceTo(stats.getLastKnownLocation())/1000));
		};
		
		//Sets last known location
		stats.setLastKnownLocation(loc);
		
		//Sometimes persist statistics
		if((count%4)==0){
			persistStatistics();
			count=1;
			
		};
		count++;

		
	};
	
	
	public static StatisticsManager getInstance(Context ctx){
		if(instance==null){
			mContext = ctx;
			instance = new StatisticsManager();
		};
		return instance;
	}


}
