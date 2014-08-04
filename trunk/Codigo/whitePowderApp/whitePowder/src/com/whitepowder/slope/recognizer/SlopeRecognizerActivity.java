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
	
	private RecognizedSlope mRecognizedSlope;
	private Spinner spinner;
	public SlopeSpinnerAdapter adapter;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private boolean activeFlag = false;
	private boolean accurateFlag = false;
	private ProgressDialog progressDialog;
	private SlopeRecognizerActivity mContext = this; 
	private final int REQUEST_SHOW_MAP = 2;
	
	private int ACCURATE_RATE =20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slope_recognition);
		
		//Generates hint option in slope spinner
		spinner = (Spinner) findViewById(R.id.slope_recognition_spinner);
		adapter = new SlopeSpinnerAdapter(this, generateDummySlope(), R.layout.slope_recognition_spinner_item);
		spinner.setAdapter(adapter);
		
		//Downloads and displays slopes
	//	SlopeDownloaderThread sdt = new SlopeDownloaderThread();
	//	sdt.execute();
		
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
		
		setupStartButton(btnStart,btnStop);
		setupStopButton(btnStop,btnStart);
		
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
				new ApplicationError(604, "Warning", "GPS provider disenabled in slope recognition module");
				if(activeFlag){
					
					Toast.makeText(mContext, "Se ha desactivado el GPS", Toast.LENGTH_SHORT).show();
				
					activeFlag=false;
					mRecognizedSlope.clearAll();
					mRecognizedSlope = null;					
					
					//Changes UI
					RelativeLayout btnStart = (RelativeLayout) mContext.findViewById(R.id.slope_recognition_start_button_container); 
					btnStart.setClickable(true);
					btnStart.setVisibility(RelativeLayout.VISIBLE);
					
					RelativeLayout btnStop = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_stop_button_container);
					btnStop.setClickable(false);
					btnStop.setVisibility(RelativeLayout.INVISIBLE);
				}
			}

			@Override
			public void onProviderEnabled(String arg0) {
				//TODO handle errors
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				//TODO handle error
			};
		};
	}
	
	private void setupStartButton(final RelativeLayout btnStart, final RelativeLayout btnStop){
		btnStart.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				if(((SimplifiedSlope) spinner.getSelectedItem()).getSlope_id()==0){
					Toast.makeText(mContext, "Por favor seleccione una pista a reconocer", Toast.LENGTH_SHORT).show();
				}
				else{				
					if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
						Toast.makeText(mContext, "Por favor active el GPS para continuar", Toast.LENGTH_SHORT).show();
					}
					else{
						activeFlag=true;
						
						//Changes UI
						btnStart.setClickable(false);
						btnStart.setVisibility(RelativeLayout.INVISIBLE);
						
						btnStop.setClickable(true);
						btnStop.setVisibility(RelativeLayout.VISIBLE);
						
	
						if(!accurateFlag){
							progressDialog = new ProgressDialog(mContext);
							progressDialog.setMessage("Calibrando su GPS, por favor espere");
							progressDialog.setCancelable(false);
							progressDialog.setIndeterminate(true);
							progressDialog.show();
						};
						
						//TODO get id
						mRecognizedSlope = new RecognizedSlope(1);
						
					};
				};
			};
		});
	};
	
	private void setupStopButton(final RelativeLayout btnStop, final RelativeLayout btnStart){
		btnStop.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,MapDisplayActivity.class);
				intent.putExtra("slope", mRecognizedSlope);
				startActivityForResult(intent, REQUEST_SHOW_MAP);
			
			};
		});
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
		if(requestCode == REQUEST_SHOW_MAP){
			if(resultCode==RESULT_CANCELED){
				RelativeLayout btnStart = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_start_button_container);
				btnStart.setClickable(true);
				btnStart.setVisibility(RelativeLayout.VISIBLE);
				
				RelativeLayout btnStop = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_stop_button_container);
				btnStop.setClickable(false);
				btnStop.setVisibility(RelativeLayout.INVISIBLE);
				
				mRecognizedSlope.clearAll();
				mRecognizedSlope = null;
				activeFlag = false;
			}
			else if(resultCode==RESULT_OK){
				//TODO transmit
			};
		}

	}

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
