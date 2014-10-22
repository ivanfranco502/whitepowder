package com.whitepowder.skier.map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.SeekBar;
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
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ReadFile;

public class MapActivity extends Activity {
	
	private GoogleMap mMap=null;
	private MapActivity mContext;
	
	static public SkierActivity skierActivity;
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
			drawSlopes(dsc);	
			
			//Setups zoom and center
	        
	        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(determineCenter(dsc),13);
	        mMap.moveCamera(cam);
		};
	
	}
	
	private void drawSlopes(DrawableSlopeContainer dsc){
	
		if(dsc.code==200){
			
			for(DrawableSlope ds: dsc.payload){
				if(ds.getSlope_coordinates()!=null){
					
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
		
		
	}
	
	private LatLng determineCenter(DrawableSlopeContainer dsc){
		double sumatoriaX=0;
		double sumatoriaY=0;
		int count=0;
		
		if(dsc.code==200){
			
			for(DrawableSlope ds: dsc.payload){
				if(ds.getSlope_coordinates()!=null){					
					sumatoriaX += ds.getSlope_coordinates().get(0).x;
					sumatoriaY += ds.getSlope_coordinates().get(0).y;
					count ++;
				};
			};
			
			return new LatLng(sumatoriaX/count, sumatoriaY/count);
			
		}
		else{
			return new LatLng(0, 0);
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


