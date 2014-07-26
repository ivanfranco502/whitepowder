package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SlopeRecognizerActivity extends Activity{
	
	private ApplicationError mError = null;
	private RecognizedSlope mRecognizedSlope;
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
		};
	
		enableLocationListener();
				
		//Register for location changes	
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,mLocationListener);
		
		if (!mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
			AlertNoGps();			
		};
		
		RelativeLayout btnStart = (RelativeLayout) findViewById(R.id.slope_recognition_start_button_container);
		RelativeLayout btnStop = (RelativeLayout) findViewById(R.id.slope_recognition_stop_button_container);
		
		
		//Setup buttons
		
		setupStartButton(btnStart);
		setupStopButton(btnStop);
		//TODO setup show button
		
	};
			
	
	ArrayList<SimplifiedSlope> generateDummySlope(){
		ArrayList<SimplifiedSlope> array = new ArrayList<SimplifiedSlope>();
		SimplifiedSlope pista = new SimplifiedSlope();
		pista.setSlope_id(0);
		pista.setSlope_description("Seleccione una pista");
		array.add(pista);
		return array;
		
	}
	
	private void enableLocationListener(){
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
						mRecognizedSlope.add(new Coordinate(location.getLatitude(), location.getLongitude()));
					};
				}
				else{
					accurateFlag = false;
				};
			
			};
			
			@Override
			public void onProviderDisabled(String arg0) {
				mError = new ApplicationError(604, "Warning", "GPS provider disenabled in slope recognition module");
				if(activeFlag){
					activeFlag=false;
					Toast.makeText(mContext, "Se ha desactivado el GPS", Toast.LENGTH_SHORT).show();
					mRecognizedSlope = null;
					
					RelativeLayout stop = (RelativeLayout) findViewById(R.id.slope_recognition_stop_button_container);
					stop.setVisibility(RelativeLayout.INVISIBLE);
					stop.setClickable(false);
					
					RelativeLayout btnStart = (RelativeLayout) findViewById(R.id.slope_recognition_start_button_container);
					btnStart.setVisibility(RelativeLayout.VISIBLE);
					btnStart.setClickable(true);
					
					//TODO vuelta a 0 de la UI.
				}
			}

			@Override
			public void onProviderEnabled(String arg0) {
				//TODO handle error
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				//TODO handle error
			};
		};
	}
	
	private void setupStartButton(final RelativeLayout btnStart){
		btnStart.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
					Toast.makeText(mContext, "Por favor active el GPS para continuar", Toast.LENGTH_SHORT).show();
				}
				else{
					activeFlag=true;

					if(!accurateFlag){
						progressDialog = new ProgressDialog(mContext);
						progressDialog.setMessage("Calibrando su GPS, por favor espere");
						progressDialog.setCancelable(true);
						progressDialog.setIndeterminate(true);
						progressDialog.show();
					};
					
					//TODO get id
					mRecognizedSlope = new RecognizedSlope(1);
					
					//TODO change UI				
					btnStart.setVisibility(RelativeLayout.INVISIBLE);
					btnStart.setClickable(false);
					
					RelativeLayout stop = (RelativeLayout) findViewById(R.id.slope_recognition_stop_button_container);
					stop.setVisibility(RelativeLayout.VISIBLE);
					stop.setClickable(true);
				};		
			}
		});
	};
	
	private void setupStopButton(final RelativeLayout btnStop){
		btnStop.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
					
			}
		});
	};

	public SlopeSpinnerAdapter getAdapter() {
		return adapter;
	};

	public void setAdapter(SlopeSpinnerAdapter adapter) {
		this.adapter = adapter;	
	};
	
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
	};


}
