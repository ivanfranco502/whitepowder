package com.whitepowder.slopeRecognizer;

import java.util.ArrayList;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.storage.SyncThread;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
	
	//Buttons
	private ImageButton butSubmenuSlope;
	
	//Sync
	private ProgressDialog progressDialogSync;
	private BroadcastReceiver syncFinishedBroadcastReciever=null;
	private SyncThread sth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SimplifiedSlopeContainer mSlopes=null;
		
		//Sets general
		super.onCreate(savedInstanceState);
		
		//Si no tengo el perfil cargado, lo cargo
		
		if(User.getUserInstance().getToken()==null){			
			SharedPreferences sharedPreferences = getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
			String role = sharedPreferences.getString("role", "UNKNOWN");
			String token = sharedPreferences.getString("_token", "UNKNOWN");
			if(role != "UNKNOWN" && token != "UNKNOWN"){
				User.getUserInstance().setRole(role);
				User.getUserInstance().setToken(token);
				
				if(User.getUserInstance().getToken().equals("UNKNOWN")||(User.getUserInstance().getRole().equals("UNKNOWN"))){
					Logout.logout(this, false);
				};	
			};
		};
		
		setContentView(R.layout.slope_recognition);
		butSubmenuSlope = (ImageButton) findViewById(R.id.bt_submenu_slope);
		
		//Generates hint option in slope spinner
		spinner = (Spinner) findViewById(R.id.slope_recognition_spinner);
		adapter = new SlopeSpinnerAdapter(this, generateDummySlope(), R.layout.slope_recognition_spinner_item);
		spinner.setAdapter(adapter);
		
		//Gets and show slopes
		
		SharedPreferences prefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_PRIVATE);
		String slopesText = prefs.getString(StorageConstants.SIMPLIFIED_SLOPES_KEY, null);
		
		Gson gson = new Gson();
		mSlopes = gson.fromJson(slopesText,SimplifiedSlopeContainer.class);
		
		if(mSlopes!=null){
			for(SimplifiedSlope ss : mSlopes.payload){
				mContext.getAdapter().add(ss);			
			};
		};
		
		adapter.notifyDataSetChanged();
		
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
		
        setupPopupMenu();	
		setupStartButton(btnStart,btnStop);
		setupStopButton(btnStop,btnStart);
	
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mLocationListener);
		
		if(syncFinishedBroadcastReciever!=null){
			unregisterReceiver(syncFinishedBroadcastReciever);
		};
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
			
				//TODO deshardcode texts
				
				if(((SimplifiedSlope) spinner.getSelectedItem()).getSlope_id()==0){
					Toast.makeText(mContext, "Por favor seleccione una pista a reconocer", Toast.LENGTH_SHORT).show();
				}
				
				//Checks if slope is already recognized
				
				else if(((SimplifiedSlope)spinner.getSelectedItem()).slope_recognized==1){
						final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setMessage("La pista seleccionada ya se encuenta reconocida, ¿Desea sobrescribirla?");
						builder.setCancelable(true);
						
				        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				        	public void onClick(final DialogInterface dialog, final int id) {
				        		dialog.cancel();
				        		checkAndStart();
				        	}});
				        
				        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				        	public void onClick(final DialogInterface dialog, final int id) {
				        		spinner.setSelection(0);
				        		dialog.cancel();
				        	}});
				        
						AlertDialog alert = builder.create();
					    alert.show();
					}
				else{				
					checkAndStart();
				};
			};
		});
	};
	
	private void checkAndStart(){
		
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
		
	}
	
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
	
private void setupPopupMenu(){
		
		//Setups options menu
		butSubmenuSlope.setOnClickListener(new OnClickListener() {  
			@Override  
			public void onClick(View v) {  
	         	 
				PopupMenu popup = new PopupMenu(SlopeRecognizerActivity.this, butSubmenuSlope);  
				popup.getMenuInflater().inflate(R.menu.popup_menu_submenu, popup.getMenu());  

				//registering popup with OnMenuItemClickListener  
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case R.id.submenu_logout:      			  
							new AlertDialog.Builder(SlopeRecognizerActivity.this)
							.setTitle(getString(R.string.alert_exit_title))
	         			  	.setMessage(getString(R.string.alert_exit_message))
	         			  	.setNegativeButton(getString(R.string.alert_no), null)
	         			  	.setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

	         			  		public void onClick(DialogInterface arg0, int arg1) {
	         		            	
	         			  			Logout.logout(mContext, false);		  
	         			  		}
	         			  	}).create().show();
							break;
	         				
						case R.id.submenu_change_password:
	         			  
							Intent intent = new Intent(mContext, PasswordChangeActivity.class);
							startActivity(intent);
							break;
							
						case R.id.submenu_sync:
		         			
							//Can only sync when not recognizing
							if(activeFlag){
								Toast.makeText(mContext, getResources().getString(R.string.slope_recognition_cant_sync_msj), Toast.LENGTH_SHORT).show();
							}
							else{
								//Stats sync dialog
								progressDialogSync = new ProgressDialog(mContext);
								progressDialogSync.setMessage(getResources().getString(R.string.sync_dialog_message));
								progressDialogSync.setCancelable(false);
								progressDialogSync.setIndeterminate(true);
								progressDialogSync.show();
								
								//Starts sync task
								sth = new SyncThread();
								setupOnSyncFinishedListener();
								sth.execute(mContext);
								
								break;
							};

	         			 
						default:
							break;
						}
	         		  
						return true;  
					}  
				});  
				
				popup.show(); 
			}  
		});
	};
	
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
	};
	
	public void setupOnSyncFinishedListener(){
		
		syncFinishedBroadcastReciever = new BroadcastReceiver() {

	        @Override
	        public void onReceive(Context context, Intent intent) {
				Boolean success = intent.getExtras().getBoolean("success");
				onSyncFinished(success);
	        };
	    };
		
		registerReceiver(syncFinishedBroadcastReciever, new IntentFilter(sth.getIntentOnSyncFinishedAction()));
	};
	
	public void onSyncFinished(boolean success){
		if (success){			
			//Closes dialog			
			progressDialogSync.dismiss();
			if(adapter!=null){
				SimplifiedSlopeContainer mSlopes=null;
				
				//Clear all slopes in adapter
				adapter.clear();
				
				//Load new slopes in adapter
				SimplifiedSlope pista = new SimplifiedSlope();
				pista.setSlope_id(0);
				pista.setSlope_description("Seleccione una pista");
				
				adapter.add(pista);
				
				SharedPreferences prefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_PRIVATE);
				String slopesText = prefs.getString(StorageConstants.SIMPLIFIED_SLOPES_KEY, null);
				
				Gson gson = new Gson();
				mSlopes = gson.fromJson(slopesText,SimplifiedSlopeContainer.class);
				
				if(mSlopes!=null){
					for(SimplifiedSlope ss : mSlopes.payload){
						mContext.getAdapter().add(ss);			
					};
				};
				adapter.notifyDataSetChanged();
				
				
			};
		}
		else{	
			progressDialogSync.dismiss();
			Toast.makeText(mContext, getResources().getString(R.string.sync_toast_error), Toast.LENGTH_SHORT).show();
			
		};
		
	};


}
