package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

import com.example.whitepowder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class SlopeRecognizerActivity extends Activity{
	
	public SlopeSpinnerAdapter adapter;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private boolean activeFlag = false;
	private boolean accurateFlag = false;
	private ProgressDialog progressDialog;
	private SlopeRecognizerActivity mContext = this; 
	
	private int ACCURATE_RATE =20;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slope_recognition);
		
		//Generates hint option in slope spinner
		final Spinner spinner = (Spinner) findViewById(R.id.slope_recognition_spinner);
		adapter = new SlopeSpinnerAdapter(this, generateDummySlope(), R.layout.slope_recognition_spinner_item);
		spinner.setAdapter(adapter);
		
		//Downloads and displays slopes
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(this);
		sdt.execute();
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE))){
			finish();
			//TODO handle error
		};

		if (!mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			AlertNoGps();
		}
			
		mLocationListener = new LocationListener() {

			// Called back when location changes

			@Override
			public void onLocationChanged(Location location) {
				if(location.getAccuracy()<ACCURATE_RATE){
					accurateFlag = true;
					
					if(progressDialog!=null){
						progressDialog.dismiss();
						progressDialog = null;
					};
					
					if(activeFlag){
						//TODO track
					};
				}
				else{
					accurateFlag = false;
				};
			
			};
			
			@Override
			public void onProviderDisabled(String arg0) {
				//TODO handle error
			};

			@Override
			public void onProviderEnabled(String arg0) {
				//TODO handle error
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				//TODO handle error
			};
		};
		
		//Register for location changes	
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mLocationListener);
		
		final RelativeLayout btnStart = (RelativeLayout) findViewById(R.id.slope_recognition_start_button);
		
		btnStart.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				activeFlag=true;
				
				if(!accurateFlag){
					progressDialog = new ProgressDialog(mContext);
					progressDialog.setMessage("Calibrando su GPS, por favor espere");
					progressDialog.setCancelable(true);
					progressDialog.setIndeterminate(true);
					progressDialog.show();
				};
				
				btnStart.setBackgroundColor(Color.RED);
				//TODO change UI
				
			}
		});
		
	};
			
	
	ArrayList<SimplifiedSlope> generateDummySlope(){
		ArrayList<SimplifiedSlope> array = new ArrayList<SimplifiedSlope>();
		SimplifiedSlope pista = new SimplifiedSlope();
		pista.setSlop_id(0);
		pista.setSlop_description("Seleccione una pista");
		array.add(pista);
		return array;
		
	}

	public SlopeSpinnerAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SlopeSpinnerAdapter adapter) {
		this.adapter = adapter;
	}
	
	  private void AlertNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
	           .setCancelable(false)
	           .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    AlertDialog alert = builder.create();
	    alert.show();
	  }


}
