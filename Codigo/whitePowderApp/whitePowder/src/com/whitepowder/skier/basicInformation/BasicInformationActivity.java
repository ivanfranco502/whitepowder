package com.whitepowder.skier.basicInformation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.storage.StorageConstants;

public class BasicInformationActivity extends Activity {
	

	private TextView ski_center_amenities;
	private TextView ski_center_minimum_height;
	private TextView ski_center_maximum_height;
	private TextView ski_center_season_since;
	private TextView ski_center_season_upto;
	private ListView ski_center_schedule;
	private TextView ski_center_details;
	
	/*private TextView forecast_date;
	private TextView forecast_description;
	private TextView forecast_min_temp;
	private TextView forecast_max_temp;
	private ImageView forecast_icon;
	private TextView forecast_see_extended;*/
	
	//SeekBar emergency
	private boolean seekBarProgress;
	
	
	BasicInformationActivity mContext;
	
	public BasicInformationForecast[] basicInformationForecast;
	
	double coorX;
	double coorY;
	
	View rootView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.skier_fragment_basic_information);
			mContext = this;
				
			ski_center_amenities = (TextView) findViewById(R.id.ski_center_amenities);
			ski_center_minimum_height = (TextView) findViewById(R.id.ski_center_minimum_height);
			ski_center_maximum_height = (TextView) findViewById(R.id.ski_center_maximum_height);
			ski_center_season_since = (TextView) findViewById(R.id.ski_center_season_since);
			ski_center_season_upto = (TextView) findViewById(R.id.ski_center_season_upto);
			ski_center_schedule = (ListView) findViewById(R.id.ski_center_schedule);
			ski_center_details = (TextView) findViewById(R.id.ski_center_details);
			
			/*forecast_date = (TextView) findViewById(R.id.forecast_date);
			forecast_description = (TextView) findViewById(R.id.forecast_description);
			forecast_min_temp = (TextView) findViewById(R.id.forecast_min_temp);
			forecast_max_temp = (TextView) findViewById(R.id.forecast_max_temp);
			forecast_icon = (ImageView) findViewById(R.id.forecast_icon);	*/
			
			
			mContext.basicInformationForecast = new BasicInformationForecast[7];
			
	        setupEmergencyButton();
			
			
			fillUIFields();
		
	}
	
	private void fillUIFields() {
		SharedPreferences sharedPrefs = this.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);;
		Gson gson = new Gson();
		String basicInformationValue = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);
		
		if(basicInformationValue==null){
			//TODO decir que no hay info cargada

    	}
    	else{
    		final BasicInformationResponse basicInformationResponse;
    		basicInformationResponse = gson.fromJson(basicInformationValue, BasicInformationResponse.class);
		
			
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
			
			String skiCenterSeasonSince = basicInformationResponse.getBasicInformation().getSeasonSince();
			if(skiCenterSeasonSince != null){
				ski_center_season_since.setText(skiCenterSeasonSince);
			}
			
			String skiCenterSeasonUpto = basicInformationResponse.getBasicInformation().getSeasonTill();
			if(skiCenterSeasonUpto != null){
				ski_center_season_upto.setText(skiCenterSeasonUpto);
			}			
			
			
			// Create The Adapter with passing ArrayList as 3rd parameter
			ArrayList<Day> days = basicInformationResponse.getBasicInformation().getDays();
			if(days != null){
				ArrayList<String> arraySchedules = new ArrayList<String>();
				for(int i = 0; i < days.size(); i++ ){
					arraySchedules.add(days.get(i).toString());
				}
				ArrayAdapter<String> arrayAdapter =      
				new ArrayAdapter<String>(mContext,R.layout.skier_fragment_basic_information_schedule_row, arraySchedules);
				// Set The Adapter
				ski_center_schedule.setAdapter(arrayAdapter); 
			}
			
			String skiCenterDetails = basicInformationResponse.getBasicInformation().getDetails();
			if(skiCenterDetails != null){
				ski_center_details.setText(skiCenterDetails);
				
			
			} 
		}
				
				
			
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
						EmergencyThread et = new EmergencyThread(mContext, getApplicationContext());
						et.execute();
					}
					else{
						animateSkier();
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
	
	private void animateSkier() {
		final ImageView emergencyImage = (ImageView) findViewById(R.id.emergencyAnimation);
		final ImageView progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		final ImageView progressBar2 = (ImageView) findViewById(R.id.progressBar2);
		final TextView help = (TextView) findViewById(R.id.help);
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		TranslateAnimation moveLeftToRight = new TranslateAnimation(0, size.x, 0, 0);
    	moveLeftToRight.setDuration(2000);
    	moveLeftToRight.setRepeatCount(2);
    	moveLeftToRight.setAnimationListener(new AnimationListener(){
    	    public void onAnimationStart(Animation a){
    	    	emergencyImage.setVisibility(View.VISIBLE);
    	    	progressBar1.setVisibility(View.INVISIBLE);
    	    	progressBar2.setVisibility(View.INVISIBLE);
    	    	help.setVisibility(View.INVISIBLE);
    	    }
    	    public void onAnimationRepeat(Animation a){}
    	    public void onAnimationEnd(Animation a){
    	    	emergencyImage.setVisibility(View.INVISIBLE);
    	    	progressBar1.setVisibility(View.VISIBLE);
    	    	progressBar2.setVisibility(View.VISIBLE);
    	    	help.setVisibility(View.VISIBLE);
    	    }
    	});
    	
    
    	emergencyImage.startAnimation(moveLeftToRight);
		
	}	
	
}
