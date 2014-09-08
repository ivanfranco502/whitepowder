package com.whitepowder.skier.map;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.example.whitepowder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;

public class MapActivity extends Activity {
	
	private GoogleMap mMap=null;
	private MapActivity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.skier_fragment_map);
			mContext = this;
			setupMap();
			
	}
	
	public void setupMap(){
		
		//Gets map
		
		mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.skier_map_fragment)).getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		//Find points
		
		Gson gson = new Gson();
		String data = read_file(mContext.getApplicationContext(), StorageConstants.DRAWABLE_SLOPES_FILE);	
		DrawableSlopeContainer dsc = gson.fromJson(data, DrawableSlopeContainer.class);
		
		drawSlopes(dsc);	
		
		//Setups zoom and center
        
        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(determineCenter(dsc),13);
        mMap.moveCamera(cam);
        
		
	}

	@Override
	public void onStart(){
		super.onStart();
	
	}
	
	private void drawSlopes(DrawableSlopeContainer dsc){
	
		if(dsc.code==200){
			
			for(DrawableSlope ds: dsc.payload){
				if(ds.getSlope_coordinates()!=null){
					
					PolylineOptions plo = new PolylineOptions();
				    plo.width(6);
				    plo.color(Color.parseColor("#"+ds.getSlope_difficulty_color()));
			
					for(SimpleCoordinate c: ds.slope_coordinates){
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
	
	private String read_file(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

}

