package com.whitepowder.skier.basicInformation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.storage.StorageConstants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BasicInformationForecastActivity extends Activity {

	ListView listViewForecast;
	BasicInformationForecastAdapter forecastListAdapter;
	double coorX;
	double coorY;
	private BasicInformationForecast[] basicInformationForecast;
	
	//SeekBar emergency
	private boolean seekBarProgress;
	BasicInformationForecastActivity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skier_fragment_basic_information_forecast);
		
		mActivity = this;
		
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
		
		setupEmergencyButton();
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
						EmergencyThread et = new EmergencyThread(mActivity, getApplicationContext());
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
