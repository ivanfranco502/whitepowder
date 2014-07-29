package com.whitepowder.slope.recognizer;

import com.example.whitepowder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class MapDisplayActivity extends Activity {

	MapDisplayActivity mContexy = this;
	RecognizedSlope mSlope = null;
	GoogleMap mMap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.slope_recognition_map);
		
		mSlope = (RecognizedSlope)getIntent().getSerializableExtra("slope");
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.slope_recognition_map)).getMap();
		
		 PolylineOptions plo = new PolylineOptions();
	     plo.width(5);
	     plo.color(Color.RED);

		 for(Coordinate c: mSlope.coordinates){
			 plo.add(new LatLng(c.posX, c.posY));
		 }
		 
		 mMap.addPolyline(plo);

	}
}
