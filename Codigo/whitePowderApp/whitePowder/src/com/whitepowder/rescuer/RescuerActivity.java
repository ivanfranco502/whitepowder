package com.whitepowder.rescuer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.whitepowder.R;
import com.whitepowder.gcmModule.AlertDisplayActivity;
import com.whitepowder.gcmModule.GCM;
import com.whitepowder.skier.SkierModeService;
import com.whitepowder.skier.SkierModeStopperThread;
import com.whitepowder.storage.SyncThread;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.utils.Logout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
public class RescuerActivity extends Activity {

	private Context mContext;
	private LocationManager mLocationManager;
	private ServiceConnection mConnection;
	private AlertDialog alertNoGPS = null;
	private BroadcastReceiver serviceBroadcastReciever=null;
	private SkierModeService mBoundService;
	private ImageButton butSubmenu;
	
	public static String GCM_ALERT_INTENT_ACTION = "GCM_ALERT_INTENT_ACTION";
	public static String GCM_ACCIDENT_INTENT_ACTION = "GCM_ACCIDENT_INTENT_ACTION";
	private BroadcastReceiver mAlertReceiver = null;
	private BroadcastReceiver mAccidentReceiver = null;
	
	private ArrayList<Victim> accidents;
	private InboxAdapter adapter;
	
	//Sync
	private ProgressDialog progressDialogSync;
	private BroadcastReceiver syncFinishedBroadcastReciever=null;
	private SyncThread sth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rescuer_inbox);
		getActionBar().hide();
		butSubmenu = (ImageButton) findViewById(R.id.bt_submenu_rescu);
		
		mContext = getApplicationContext();
		
		accidents = new ArrayList<Victim>();
		adapter = new InboxAdapter(this, R.layout.rescuer_inbox_item, accidents);
		
		//Setups list view
		setupListView();
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "Su dispositivo no posee GPS", Toast.LENGTH_SHORT ).show();
		};		
		
		//Create service connection
        createServiceConnectionAndRegisterForBroadcast();
        
		//Setups rescuer mode
        setupRescuerMode();
		
        //setups popup menu
        
		setupPopupMenu();
		
		//Enables GCM
		new GCM(mContext);
		
		//Register for GCM alerts
        registerAlertBroadcastReceiver();
        registerAccidentBroadcastReceiver();
        
        
	}
	

	private void setupListView(){
		ListView la = (ListView) findViewById(R.id.rescuer_inbox_list);			
		View footer = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rescuer_inbox_footer,null, false);
		la.addFooterView(footer);
		la.setAdapter(adapter);
		
		//Setup Swipe adapter
		
		SwipeDismissListViewTouchListener touchListener =
	                new SwipeDismissListViewTouchListener(
	                        la,
	                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
	                            @Override
	                            public boolean canDismiss(int position) {
	                            	if(position==accidents.size()){
	                            		return false;
	                            	}
	                            	else{
	                            		return true;
	                            	}
	                                
	                            };

	                            @Override
	                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
	                                for (int position : reverseSortedPositions) {
	                                    adapter.remove(adapter.getItem(position));
	                                }
	                                adapter.notifyDataSetChanged();
	                            }
	                        });
		la.setOnTouchListener(touchListener);		
		la.setOnScrollListener(touchListener.makeScrollListener());	
		
		la.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,	long arg3) {
				JSONObject mensaje = new JSONObject();
				JSONArray array = new JSONArray();
				
				if(position<accidents.size()){
					JSONObject accidente = new JSONObject();
					
					Victim item = adapter.getItem(position);
				
					try {
						accidente.put("username", item.getUsername());
						accidente.put("x", item.getLocation().x);
						accidente.put("y", item.getLocation().y);
						array.put(accidente);
						mensaje.put("accidentes",array);
					} 
	
					catch (JSONException e) {
						//TODO handle error
					};
					
				}
				else{

					try {				
						for (Victim victim : accidents) {
	
							JSONObject accidente = new JSONObject();
							accidente.put("username", victim.getUsername());
							accidente.put("x", victim.getLocation().x);
							accidente.put("y", victim.getLocation().y);
							array.put(accidente);								
	
						};
						mensaje.put("accidentes", array);
					}
					catch(JSONException e){
						//TODO handle error
					};
				};
				
				if(array.length()!=0){
					Intent intent = new Intent(RescuerActivity.this, RescuerMap.class);
					intent.putExtra("accidentes",mensaje.toString());
					startActivity(intent);
				};
		
			}
		});
		
	};

	private void setupRescuerMode(){
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertNoGps();
		}
		else{
			//Binds to service
			//TODO estoy tocando aca
			bindService(new Intent(mContext, SkierModeService.class), mConnection, Context.BIND_AUTO_CREATE);			
		};	
	}
	
	private void alertNoGps() {
		if (alertNoGPS == null){
			final AlertDialog.Builder builder = new AlertDialog.Builder(RescuerActivity.this);
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
		else{
			alertNoGPS.show();
		};
	};
	
	@Override
	public void onResume(){
		super.onResume();
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertNoGps();
		}
		else{
			if(alertNoGPS!=null){
				alertNoGPS.dismiss();
				alertNoGPS=null;
			};
			bindService(new Intent(mContext, SkierModeService.class), mConnection, Context.BIND_AUTO_CREATE);	
		};
		
	}
	
	
	private void createServiceConnectionAndRegisterForBroadcast(){
		
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				mBoundService = ((SkierModeService.LocalBinder)service).getService();
				
				serviceBroadcastReciever = new BroadcastReceiver() {

			        @Override
			        public void onReceive(Context context, Intent intent) {
			        	stopRescuer();
			        	alertNoGps();			        
			        };
			        	        
			    };
			    registerReceiver(serviceBroadcastReciever, new IntentFilter(mBoundService.getIntentStopSkierModeAction()));
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
		
		if(syncFinishedBroadcastReciever!=null){
			unregisterReceiver(syncFinishedBroadcastReciever);
		};		
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
					
					//Add the victim to the accident list	
					String mensaje = intent.getExtras().getString("body");				
					
					try {
						Victim victima=null;
						JSONObject mje = new JSONObject(mensaje);
						victima = new Victim(mje.getString("username"), mje.getDouble("x"),mje.getDouble("y"));
						adapter.add(victima);
					} 
					catch (JSONException e) {
						//TODO raise error
					};				
					
				};
			}
		};
		
		registerReceiver(mAccidentReceiver, new IntentFilter(RescuerActivity.GCM_ACCIDENT_INTENT_ACTION));
	};
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	        .setTitle(getString(R.string.alert_exit_title))
	        .setMessage(getString(R.string.alert_exit_message))
	        .setNegativeButton(getString(R.string.alert_no), null)
	        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	
	            	RescuerActivity.this.finish();
	            }
	        }).create().show();
	};
	
	public void onSyncFinished(boolean success){
		if (success){			
			//Closes dialog			
			progressDialogSync.dismiss();			
		}
		else{	
			progressDialogSync.dismiss();
			Toast.makeText(mContext, getResources().getString(R.string.sync_toast_error), Toast.LENGTH_SHORT).show();
			
		};
		
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
	
	private void setupPopupMenu(){
		
		//Setups options menu
		butSubmenu.setOnClickListener(new OnClickListener() {  
			@Override 
			public void onClick(View v) {  
	         	 
				PopupMenu popup = new PopupMenu(RescuerActivity.this, butSubmenu);  
				popup.getMenuInflater().inflate(R.menu.popup_menu_submenu, popup.getMenu());  

				//registering popup with OnMenuItemClickListener  
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case R.id.submenu_logout:      			  
							new AlertDialog.Builder(RescuerActivity.this)
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
		         			
							//Starts sync dialog
							progressDialogSync = new ProgressDialog(RescuerActivity.this);
							progressDialogSync.setMessage(getResources().getString(R.string.sync_dialog_message));
							progressDialogSync.setCancelable(false);
							progressDialogSync.setIndeterminate(true);
							progressDialogSync.show();
							
							//Starts sync task
							sth = new SyncThread();
							setupOnSyncFinishedListener();
							sth.execute(mContext);
							
							break;
	         			 
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
	

}
