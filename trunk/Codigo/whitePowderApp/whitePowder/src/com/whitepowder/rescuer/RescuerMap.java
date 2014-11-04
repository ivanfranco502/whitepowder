package com.whitepowder.rescuer;

import java.util.ArrayList;

import com.example.whitepowder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitepowder.skier.Coordinate;
import com.whitepowder.skier.map.DrawableSlope;
import com.whitepowder.skier.map.DrawableSlopeContainer;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ReadFile;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class RescuerMap extends Activity {
	
	private GoogleMap mMap = null;
	Context mContext;
	ArrayList<Victim> accidents=null;
	Gson gson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gson = new Gson();
		mContext = getApplicationContext();
		getActionBar().hide();
		
		accidents = parseVictims(getIntent().getExtras().getString("accidentes"));
		
		setContentView(R.layout.rescuer_map);
			
		if(accidents.size()==1){
			//Si solo voy a mostrar un accidente muestro el botón de avisar atendido
			setupNursedButton();		
		};		
		
		setupMap();
		drawAccidents();	 
		
	};
	

	private ArrayList<Victim> parseVictims(String mensaje){
		ArrayList<Victim> victims = new ArrayList<Victim>();

		victims = gson.fromJson(mensaje,new TypeToken<ArrayList<Victim>>(){}.getType());
		
		
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
				
		if(accidents.size()==1){
			//Si estoy mostrando un solo accidente, el centro es el accidente.
			return new LatLng(accidents.get(0).getX(), accidents.get(0).getY());
		}
		else{
			//Si estoy mostrando varios accidentes me centro en el promedio del centro de esqui
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
		}
		

	};
	
	private void setupNursedButton(){
		RelativeLayout butNursed = (RelativeLayout) findViewById(R.id.rescuer_map_nursed_button);
		butNursed.setVisibility(RelativeLayout.VISIBLE);
		butNursed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				new AlertDialog.Builder(RescuerMap.this)
		        .setTitle(getString(R.string.rescuer_set_nursed_confirmation_title))
		        .setMessage(getString(R.string.rescuer_set_nursed_confirmation_body))
		        .setNegativeButton(getString(R.string.alert_no), null)
		        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

		            public void onClick(DialogInterface arg0, int arg1) {	            	
						new SetAccidentAsAttendedThread(accidents.get(0).getId(), mContext).start();
						Intent intent = new Intent();
						intent.putExtra("rescued_id", accidents.get(0).getId());
						RescuerMap.this.setResult(RESULT_OK, intent);
						finish();
		            }
		        }).create().show();

			}
		});
	}
	
	private void drawAccidents(){
		
		for (Victim accident : accidents) {
			mMap.addMarker(new MarkerOptions()
	        .position(new LatLng(accident.getX(),accident.getY()))
	        .title(accident.getUsername())
	        	.icon(BitmapDescriptorFactory
			    .fromResource(R.drawable.marker_accident)));	
		};	
		
	};

}
