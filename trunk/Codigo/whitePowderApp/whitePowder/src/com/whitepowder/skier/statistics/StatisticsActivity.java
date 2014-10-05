package com.whitepowder.skier.statistics;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.whitepowder.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends Activity {

	Context mContext =null;
	StatisticsManager userStats=null;
	Boolean gotStats=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.skier_statistics);
		mContext = getApplicationContext();
		
		//Setups buttons
		setupClearStatsButton();
		
		//Loads user stats
		
		userStats = StatisticsManager.getInstance(mContext);
		
		//Loads max speed
		
		if(userStats.stats.getMaxSpeed()!=0){
			TextView maxSpeed = (TextView) findViewById(R.id.skier_statistics_speed_record_value);
			maxSpeed.setTextColor(Color.BLACK);
			maxSpeed.setText(Float.toString(userStats.stats.getMaxSpeed())+" km/h");
			gotStats=true;
			
			if(userStats.stats.getMaxSpeedDate()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				
				TextView maxSpeedDate = (TextView) findViewById(R.id.skier_statistics_speed_record_date);
				maxSpeedDate.setText(sdf.format(userStats.stats.getMaxSpeedDate()));
				
			};
		};
		
		//Loads max altitude
		
		if(userStats.stats.getMaxAltitude()!=0){
			TextView maxAltitude = (TextView) findViewById(R.id.skier_statistics_height_record_value);
			maxAltitude.setText(Double.toString(userStats.stats.getMaxAltitude())+" mts");
			maxAltitude.setTextColor(Color.BLACK);
			gotStats=true;
			
			if(userStats.stats.getMaxAltitudeDate()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				
				TextView maxAltitudeDate = (TextView) findViewById(R.id.skier_statistics_height_record_date);
				maxAltitudeDate.setText(sdf.format(userStats.stats.getMaxAltitudeDate()));
				
			};
		};
		
		//Loads average speed
		
		if(userStats.stats.getAverageSpeed()!=0){
			TextView averageSpeed = (TextView) findViewById(R.id.skier_statistics_average_speed_value);
			averageSpeed.setText(Double.toString(userStats.stats.getAverageSpeed())+" km/h");
			averageSpeed.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		//Loads total distance
		
		if(userStats.stats.getTotalDistance()!=0){
			TextView totalDist = (TextView) findViewById(R.id.skier_statistics_total_distance_text);
			totalDist.setText(Double.toString(userStats.stats.getTotalDistance())+" km");
			totalDist.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		if(!gotStats){
			Toast.makeText(mContext, R.string.skier_statistics_toast_no_stats, Toast.LENGTH_LONG).show();
		};
	
	};
	
	private void setupClearStatsButton(){
		RelativeLayout butClearStats = (RelativeLayout) findViewById(R.id.skier_statistics_reset_button);
		
		butClearStats.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(StatisticsActivity.this)
		        .setTitle(R.string.skier_statistics_errase_stats_alert_title)
		        .setMessage(R.string.skier_statistics_errase_stats_alert_body)
		        .setNegativeButton(getString(R.string.alert_no), null)
		        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
		        	
		            public void onClick(DialogInterface arg0, int arg1) {
		            	//Clear stats
		            	userStats.clearStats();
		            	//Restarts activity
		            	StatisticsActivity.this.recreate();

		            };
		        }).create().show();
				
				
			}
		});
		
	}
	
}
