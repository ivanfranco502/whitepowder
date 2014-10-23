package com.whitepowder.rescuer;

import java.util.ArrayList;
import java.util.List;

import com.example.whitepowder.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.whitepowder.gcmModule.AlertDisplayActivity;
import com.whitepowder.gcmModule.GCM;
import com.whitepowder.skier.Coordinate;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.skier.SkierModeStopperThread;
import com.whitepowder.skier.map.DrawableSlope;
import com.whitepowder.skier.map.DrawableSlopeContainer;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.utils.ReadFile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class RescuerActivity extends Activity {

	private GoogleMap mMap = null;
	private RescuerActivity mContext;
	private LocationManager mLocationManager;
	private ServiceConnection mConnection;
	private AlertDialog alertNoGPS = null;
	private BroadcastReceiver serviceBroadcastReciever=null;
	private RescuerService mBoundService;
	
	public static String GCM_ALERT_INTENT_ACTION = "GCM_ALERT_INTENT_ACTION";
	public static String GCM_ACCIDENT_INTENT_ACTION = "GCM_ACCIDENT_INTENT_ACTION";
	private BroadcastReceiver mAlertReceiver = null;
	private BroadcastReceiver mAccidentReceiver = null;
	
	private List<Victim> accidents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rescuer_activity);
		mContext = this;
		accidents = new ArrayList();
		
		setupMap();
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "Su dispositivo no posee GPS", Toast.LENGTH_SHORT ).show();
		};		
		
		//Create service connection
        createServiceConnectionAndRegisterForBroadcast();
        
		setupRescuerMode();
		
		//Enables GCM
		new GCM(mContext);
		
		//Register for GCM alerts
        registerAlertBroadcastReceiver();
        registerAccidentBroadcastReceiver();
        
        
	}

	public void setupMap(){
		
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
	
	}
	
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
	}
			
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
	}
	
	private void setupRescuerMode(){
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertNoGps();
		}
		else{
			//Binds to service
			bindService(new Intent(mContext, RescuerService.class), mConnection, Context.BIND_AUTO_CREATE);			
		};	
	}
	
	private void alertNoGps() {
		if (alertNoGPS == null){
			final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage("El sistema GPS está desactivado, debe activarlo para continuar.")
				.setCancelable(false)
		        .setPositiveButton("Activar GPS", new DialogInterface.OnClickListener() {
		        	public void onClick(final DialogInterface dialog, final int id) {
		        		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		        		
		        	}
		        });
			alertNoGPS = builder.create();
			alertNoGPS.show();
		}
		else if(!alertNoGPS.isShowing()){
				final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("El sistema GPS está desactivado, debe activarlo para continuar.")
					.setCancelable(false)
			        .setPositiveButton("Activar GPS", new DialogInterface.OnClickListener() {
			        	public void onClick(final DialogInterface dialog, final int id) {
			        		startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			        		
			        	}
			        });
				alertNoGPS = builder.create();
				alertNoGPS.show();
			}
	};
	
	@Override
	public void onResume(){
		super.onResume();
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertNoGps();
		}
		else{
			bindService(new Intent(mContext, RescuerService.class), mConnection, Context.BIND_AUTO_CREATE);	
		};
		
		refreshMap();
	}
	
	
	private void createServiceConnectionAndRegisterForBroadcast(){
		
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				mBoundService = ((RescuerService.LocalBinder)service).getService();
				
				serviceBroadcastReciever = new BroadcastReceiver() {

			        @Override
			        public void onReceive(Context context, Intent intent) {
			        	stopRescuer();
			        	alertNoGps();			        };
			        	        
			    };
			    registerReceiver(serviceBroadcastReciever, new IntentFilter(mBoundService.getIntentStopRescuerAction()));
			};

			@Override
			public void onServiceDisconnected(ComponentName className) {
				stopRescuer();
				alertNoGps();
			};
			
		};   
	}
	
	private void stopRescuer(){

		if(mBoundService!=null){
			unbindService(mConnection);
		};
		
		mBoundService = null;
		
		if(serviceBroadcastReciever!=null){
			unregisterReceiver(serviceBroadcastReciever);
			serviceBroadcastReciever=null;
		};

		//Starts thread that notifies server
		new SkierModeStopperThread(mContext).start();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		stopRescuer();
		
		if(mAlertReceiver!=null){
			unregisterReceiver(mAlertReceiver);
		};
		if(mAccidentReceiver!=null){
			unregisterReceiver(mAccidentReceiver);
		};
	}
	
	private void registerAlertBroadcastReceiver(){
		mAlertReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (isOrderedBroadcast()) {
					setResultCode(RESULT_OK);
					
					//Starts Alert display activity		
					Intent alertDisplay = new Intent(mContext,AlertDisplayActivity.class);
					alertDisplay.putExtras(intent.getExtras());
					startActivity(alertDisplay);
				};
			}
		};
		
		registerReceiver(mAlertReceiver, new IntentFilter(RescuerActivity.GCM_ALERT_INTENT_ACTION));
	};
	
	private void registerAccidentBroadcastReceiver(){
		mAccidentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (isOrderedBroadcast()) {
					setResultCode(RESULT_OK);
					
					//Add the victim to the list and refresh the map	
					addAccident(intent);
					refreshMap();
				};
			}
		};
		
		registerReceiver(mAccidentReceiver, new IntentFilter(RescuerActivity.GCM_ACCIDENT_INTENT_ACTION));
	};
	
	public void addAccident(Intent intent){
		//Obtener datos del intent
		//Instanciar objeto Victim
		//Add victim a la lista accidents
	}
	
	private void refreshMap(){
		//Limpiar viejos de la lista de accidents
		//Remover viejos markers?
		//Mostrar los markers en el mapa
	}
}
