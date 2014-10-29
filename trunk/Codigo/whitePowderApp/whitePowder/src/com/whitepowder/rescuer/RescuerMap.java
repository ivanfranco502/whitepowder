package com.whitepowder.rescuer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.whitepowder.skier.map.DrawableSlope;
import com.whitepowder.skier.map.DrawableSlopeContainer;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ReadFile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

public class RescuerMap extends Activity {
	
	private GoogleMap mMap = null;
	Context mContext;
	ArrayList<Victim> accidents=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getApplicationContext();
		
		setContentView(R.layout.rescuer_activity);
		getActionBar().hide();
		
		accidents = parseVictims(getIntent().getExtras().getString("accidentes"));
		
		setupMap();
		drawAccidents();
		
	};
	

	private ArrayList<Victim> parseVictims(String mensaje){
		ArrayList<Victim> victims = new ArrayList<Victim>();
		try {
			JSONObject msg = new JSONObject(mensaje);
			JSONArray array = msg.getJSONArray("accidentes");
			
			for(int i=0;i<array.length();i++){
				JSONObject accidente = array.getJSONObject(i);
				victims.add(new Victim(accidente.getString("username"), accidente.getDouble("x"), accidente.getDouble("x")));
			}
		} 
		catch (JSONException e) {
			// TODO manage errors
		};
		
		
		return victims;
	};
	
	private void setupMap(){
		
		
		//Gets map
		mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.rescuer_map_fragment)).getMap();
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
	
	};
	
	private void drawSlopes(DrawableSlopeContainer dsc){
	
		if(dsc.getCode()==200){
			
			for(DrawableSlope ds: dsc.getPayload()){
				if(ds.getSlope_coordinates()!=null && ds.getSlope_coordinates().size() > 0){
					
					PolylineOptions plo = new PolylineOptions();
				    plo.width(6);
				    plo.color(Color.parseColor("#"+ds.getSlope_difficulty_color()));
			
					for(Coordinate c: ds.getSlope_coordinates()){
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
	
	private LatLng determineCenter(DrawableSlopeContainer dsc){
		double sumatoriaX=0;
		double sumatoriaY=0;
		int count=0;
		
		if(dsc.getCode()==200){
			for(DrawableSlope ds: dsc.getPayload()){
				if(ds.getSlope_coordinates()!=null && ds.getSlope_coordinates().size() > 0){					
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
	};
	
	private void drawAccidents(){
		
		for (Victim accident : accidents) {
			mMap.addMarker(new MarkerOptions()
	        .position(new LatLng(accident.getLocation().x,accident.getLocation().y))
	        .title(accident.getUsername())
	        	.icon(BitmapDescriptorFactory
			    .fromResource(R.drawable.marker_accident)));	
		};	
		
	};

}
