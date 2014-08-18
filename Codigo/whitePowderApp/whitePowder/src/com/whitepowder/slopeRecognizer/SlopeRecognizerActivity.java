package com.whitepowder.slopeRecognizer;

import java.util.ArrayList;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.storage.SPStorage;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SlopeRecognizerActivity extends Activity{
	
	private final int REQUEST_SHOW_MAP = 2;
	private final float DISTANCE_BETWEEN_POINTS = 1;
	private int ACCURATE_RATE =21;
	
	private TextView pointsView;
	private int pointsAmmount =0;
	public RecognizedSlope mRecognizedSlope;
	public SlopeSpinnerAdapter adapter;
	private Spinner spinner;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private boolean activeFlag = false;
	private boolean accurateFlag = false;
	private ProgressDialog progressDialog;
	private SlopeRecognizerActivity mContext = this; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SlopeContainer mSlopes=null;
		
		//Sets general
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slope_recognition);
		
		ActionBar ab = getActionBar();
		ab.setTitle("White Powder");
		ab.setSubtitle("Reconocedor");
		
		//Generates hint option in slope spinner
		spinner = (Spinner) findViewById(R.id.slope_recognition_spinner);
		adapter = new SlopeSpinnerAdapter(this, generateDummySlope(), R.layout.slope_recognition_spinner_item);
		spinner.setAdapter(adapter);
		
		//Gets and show slopes
		
		SharedPreferences prefs = mContext.getSharedPreferences(SPStorage.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_PRIVATE);
		String slopesText = prefs.getString(SPStorage.SIMPLIFIED_SLOPES, null);
		
		Gson gson = new Gson();
		mSlopes = gson.fromJson(slopesText,SlopeContainer.class);
		
		if(mSlopes!=null){
			for(SimplifiedSlope ss : mSlopes.payload){
				mContext.getAdapter().add(ss);			
			};
		};	
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE))){
			finish();
		};
	
		enableLocationListener();
				
		//Register for location changes	
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, DISTANCE_BETWEEN_POINTS,mLocationListener);
		
		if (!mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
			AlertNoGps();			
		};
		
		RelativeLayout btnStart = (RelativeLayout) findViewById(R.id.slope_recognition_start_button_container);
		RelativeLayout btnStop = (RelativeLayout) findViewById(R.id.slope_recognition_stop_button_container);
		pointsView = (TextView) findViewById(R.id.slope_recognition_points);
		
		//Setup buttons
		
		setupStartButton(btnStart,btnStop);
		setupStopButton(btnStop,btnStart);
		
		//Setups already recognized dialog
		
		spinner.setOnItemSelectedListener(
				new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {
						if(((SimplifiedSlope)spinner.getSelectedItem()).slope_recognized==1){
							final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
							builder.setMessage("La pista seleccionada ya se encuenta reconocida, ¿Desea sobrescribirla?");
							builder.setCancelable(true);
							
					        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					        	public void onClick(final DialogInterface dialog, final int id) {
					        		dialog.cancel();        		
					        	}});
					        
					        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					        	public void onClick(final DialogInterface dialog, final int id) {
					        		spinner.setSelection(0);
					        		dialog.cancel();
					        	}});
					        
							AlertDialog alert = builder.create();
						    alert.show();
						}
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}	
				}	
		);
	
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mLocationListener);
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
						startRecognition();						
					};
					
					if(activeFlag){
						mRecognizedSlope.add(new Coordinate(location.getLatitude(), location.getLongitude()));
						pointsAmmount++;
						pointsView.setText("Puntos usados: "+Integer.toString(pointsAmmount));
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
					
					//TODO deshardcode text
					Toast.makeText(mContext, "Se ha desactivado el GPS", Toast.LENGTH_SHORT).show();
				
					stopRecognition();
				}
			}

			@Override
			public void onProviderEnabled(String arg0) {
				return;
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				return;
			};
		};
	}
	
	private void setupStartButton(final RelativeLayout btnStart, final RelativeLayout btnStop){
		btnStart.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
			
				//TODO deshardcode text
				
				if(((SimplifiedSlope) spinner.getSelectedItem()).getSlope_id()==0){
					Toast.makeText(mContext, "Por favor seleccione una pista a reconocer", Toast.LENGTH_SHORT).show();
				}
				else{				
					if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
						Toast.makeText(mContext, "Por favor active el GPS para continuar", Toast.LENGTH_SHORT).show();
					}
					else{
											
						if(!accurateFlag){
							progressDialog = new ProgressDialog(mContext);
							progressDialog.setMessage("Calibrando su GPS, por favor espere");
							progressDialog.setCancelable(true);
							progressDialog.setIndeterminate(true);
							progressDialog.show();
						}
						else{		
							startRecognition();
						};	
					};
				};
			};
		});
	};
	
	private void startRecognition(){
		
		//Sets flag
		
		activeFlag=true;
		
		//Changes UI
		RelativeLayout btnStart = (RelativeLayout) mContext.findViewById(R.id.slope_recognition_start_button_container); 
		btnStart.setClickable(false);
		btnStart.setVisibility(RelativeLayout.INVISIBLE);
		
		RelativeLayout btnStop = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_stop_button_container);
		btnStop.setClickable(true);
		btnStop.setVisibility(RelativeLayout.VISIBLE);
		
		//Locks spinner
		spinner.setEnabled(false);
		
		//Starts recognition
		
		SimplifiedSlope ss = (SimplifiedSlope)spinner.getSelectedItem();
		mRecognizedSlope = new RecognizedSlope(ss.getSlope_id());
		
		if(progressDialog!=null){	
			progressDialog.dismiss();
			progressDialog = null;
		}
	};
	
	private void stopRecognition(){
		
		//Sets flag
		activeFlag = false;
		
		//Changes UI
		
		RelativeLayout btnStart = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_start_button_container);
		btnStart.setClickable(true);
		btnStart.setVisibility(RelativeLayout.VISIBLE);
		
		RelativeLayout btnStop = (RelativeLayout)mContext.findViewById(R.id.slope_recognition_stop_button_container);
		btnStop.setClickable(false);
		btnStop.setVisibility(RelativeLayout.INVISIBLE);
		
		//Locks spinner
		spinner.setEnabled(true);
		
		mRecognizedSlope.clearAll();
		mRecognizedSlope = null;
		pointsAmmount=0;
		pointsView.setText("");
		spinner.setSelection(0);
	}
	
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
	
	public void onTransmitionFinished(){
		
		Toast.makeText(mContext, "Transmisión realizada con éxito", Toast.LENGTH_SHORT).show();
		
		stopRecognition();

		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
		if(requestCode == REQUEST_SHOW_MAP){
			if(resultCode==RESULT_CANCELED){
				stopRecognition();
			}
			else if(resultCode==RESULT_OK){
				SlopeUploaderThread slut = new SlopeUploaderThread(this);
				slut.execute();
				
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
	        	public void onClick(final DialogInterface dialog, final int id) {
	        		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	        		
	        	}
	        })
	        .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        	public void onClick(final DialogInterface dialog, final int id) {
	        		dialog.cancel();
	        	}	
	        });
		AlertDialog alert = builder.create();
	    alert.show();
	};
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.rescuer_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	    
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	  case R.id.slope_recon_menu_logout:
		  
			new AlertDialog.Builder(this)
	        .setTitle(getString(R.string.alert_exit_title))
	        .setMessage(getString(R.string.alert_exit_message))
	        .setNegativeButton(getString(R.string.alert_no), null)
	        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	
	      		  Logout.logout(mContext, false);		  
	            }
	        }).create().show();
			break;
		 
          
	  case R.id.slope_recon_menu_change_password:
		  Intent intent = new Intent(mContext, PasswordChangeActivity.class);
		  this.startActivity(intent);
		  break;
	  default:
	    break;
	  }
	  
	  return true;
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	        .setTitle(getString(R.string.alert_exit_title))
	        .setMessage(getString(R.string.alert_exit_message))
	        .setNegativeButton(getString(R.string.alert_no), null)
	        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	
	            	SlopeRecognizerActivity.this.finish();
	            }
	        }).create().show();
	}

}
