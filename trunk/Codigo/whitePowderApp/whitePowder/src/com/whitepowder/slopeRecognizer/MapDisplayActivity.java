package com.whitepowder.slopeRecognizer;

import com.example.whitepowder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MapDisplayActivity extends Activity {

	MapDisplayActivity mContext = this;
	RecognizedSlope mSlope = null;
	GoogleMap mMap = null;
	RelativeLayout btnOk = null;
	RelativeLayout btnCancel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSlope = (RecognizedSlope)getIntent().getSerializableExtra("slope");
		
		//If there are no points skip everything
		
		if(mSlope.coordinates.size()==0){
			this.finish();
		}
		else{
		
			this.setContentView(R.layout.slope_recognition_map);
			this.setResult(10);
			
			btnOk = (RelativeLayout)findViewById(R.id.slope_recognition_map_ok);
			btnCancel = (RelativeLayout)findViewById(R.id.slope_recognition_map_cancel);
	
			setupCancelButton(btnCancel);
			setupOkButton(btnOk);
			
			//Gets map
			
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.slope_recognition_map)).getMap();
			
			//Setups map
	        
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(mSlope.coordinates.get(0).x,mSlope.coordinates.get(0).y),14);
	        mMap.moveCamera(cam);
	      
			//Draw slope
			PolylineOptions plo = new PolylineOptions();
		    plo.width(6);
		    plo.color(Color.RED);
	
			for(Coordinate c: mSlope.coordinates){
				plo.add(new LatLng(c.x, c.y));
			}
			 
			 mMap.addPolyline(plo);
		};
	}
	
	private void setupCancelButton(final RelativeLayout btnCancel){
		btnCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				//TODO deshadcode text
				
				new AlertDialog.Builder(mContext)
		        .setTitle("Descartar")
		        .setMessage("¿Esta seguro que desea descartar la pista?")
		        .setNegativeButton(getString(R.string.alert_no), null)
		        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
		        	
		            public void onClick(DialogInterface arg0, int arg1) {
		            	
		            	setResult(RESULT_CANCELED);
		            	mContext.finish();

		            }
		        }).create().show();
				
				
			}
		});
	}
	
	private void setupOkButton(final RelativeLayout btnOk){
		btnOk.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	};
	
}
	
