package com.whitepowder.skier.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.example.whitepowder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.whitepowder.skier.Coordinate;
import com.whitepowder.skier.basicInformation.BasicInformationResponse;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ReadFile;

public class MapActivity extends Activity {
	
	private GoogleMap mMap=null;
	private MapActivity mContext;

	//SeekBar emergency
	private boolean seekBarProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.skier_fragment_map);
			mContext = this;
			setupMap();
	        setupEmergencyButton();
			
	}
	
	public void setupMap(){
		
		//Gets map
		
		mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.skier_map_fragment)).getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		
		//Find points
		
		Gson gson = new Gson();
		String data = ReadFile.read_file(mContext.getApplicationContext(), StorageConstants.DRAWABLE_SLOPES_FILE);	
		
		if((data!=null)||(data!="")){
			DrawableSlopeContainer dsc = gson.fromJson(data, DrawableSlopeContainer.class);
			if(dsc!=null){
				drawSlopes(dsc);
			};
			
			//Setups zoom and center
	        
	        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(determineCenter(),13);
	        mMap.moveCamera(cam);
		};
	
	}
	
	private void drawSlopes(DrawableSlopeContainer dsc){
	
		if(dsc.code==200){
			
			for(DrawableSlope ds: dsc.payload){
				if(ds!=null){
					if(ds.getSlope_coordinates()!=null){
						if(ds.getSlope_coordinates().size() > 0){
				
							PolylineOptions plo = new PolylineOptions();
						    plo.width(6);
						    plo.color(Color.parseColor("#"+ds.getSlope_difficulty_color()));
						    
						    
							for(Coordinate c: ds.slope_coordinates){
								
								plo.add(new LatLng(c.x, c.y));
							}
							 
							mMap.addPolyline(plo);
							
							//Adds start marker
							
							mMap.addMarker(new MarkerOptions()
					        .position(new LatLng(ds.getSlope_coordinates().get(0).x, ds.getSlope_coordinates().get(0).y))
					        .title("Pista: "+ds.getSlope_description())
					        .snippet("Longitud: "+Integer.toString(ds.getSlope_length())+" metros")
					        .icon(BitmapDescriptorFactory
					            .fromResource(R.drawable.slope_start)));
							
							//Adds end marker
							
							mMap.addMarker(new MarkerOptions()
					        .position(new LatLng(ds.getSlope_coordinates().get(ds.getSlope_coordinates().size()-1).x, ds.getSlope_coordinates().get(ds.getSlope_coordinates().size()-1).y))
					        .title("Fin de "+ds.getSlope_description())
					        .icon(BitmapDescriptorFactory
					        	.fromResource(R.drawable.slope_end)));				
						};
					};
				};
			};
			
		};
		
		
	}
	
	private LatLng determineCenter(){
		Gson gson = new Gson();
		
		SharedPreferences sharedPrefs = this.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);;
		String basicInformationValue = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);

		BasicInformationResponse basicInfoRespose = gson.fromJson(basicInformationValue, BasicInformationResponse.class);
		return new LatLng (basicInfoRespose.getBasicInformation().getX(),basicInfoRespose.getBasicInformation().getY());
		
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


