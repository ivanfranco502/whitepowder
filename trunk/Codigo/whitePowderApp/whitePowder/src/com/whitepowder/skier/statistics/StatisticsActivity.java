package com.whitepowder.skier.statistics;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.whitepowder.R;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class StatisticsActivity extends Activity {

	Context mContext =null;
	StatisticsManager userStats=null;
	Boolean gotStats=false;
	
	static public SkierActivity skierActivity;
	//SeekBar emergency
	private boolean seekBarProgress;
	
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
			maxSpeed.setText(String.format("%.2f", userStats.stats.getMaxSpeed())+" km/h");
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
			maxAltitude.setText(String.format("%.0f", userStats.stats.getMaxAltitude())+" mts");
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
			averageSpeed.setText(String.format("%.3f", userStats.stats.getAverageSpeed())+" km/h");
			averageSpeed.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		//Loads total distance
		
		if(userStats.stats.getTotalDistance()!=0){
			TextView totalDist = (TextView) findViewById(R.id.skier_statistics_total_distance_value);
			totalDist.setText(String.format("%.3f", userStats.stats.getTotalDistance())+" km");
			totalDist.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		if(!gotStats){
			Toast.makeText(mContext, R.string.skier_statistics_toast_no_stats, Toast.LENGTH_LONG).show();
		};
		
		setupEmergencyButton();
	
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
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
			EmergencyPeripheral.handlePeripheralEvent();
	        return true;
		}
		else{
			return super.onKeyUp(keyCode, event);
		}
	}
	
	private void setupEmergencyButton(){
		SeekBar emergencySeekBar = (SeekBar) findViewById(R.id.emergency_seekBar);
		
		emergencySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      
			@Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
				if(seekBarProgress){
					if(seekBar.getProgress() >= 85 && seekBar.getProgress() <= 100){
						//llamar emergencia
						EmergencyThread et = new EmergencyThread(skierActivity.skierActivity, getApplicationContext());
						et.execute();
					}
				}
				seekBar.setProgress(0);
		    }
		      
		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	if(seekBar.getProgress() >= 0 && seekBar.getProgress() <= 15){
		    		seekBarProgress = true;
				}else{
					seekBarProgress = false;
				}
		    }
		      
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
			});
    
	}	
	
}
