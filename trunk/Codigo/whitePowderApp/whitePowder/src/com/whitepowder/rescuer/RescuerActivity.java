package com.whitepowder.rescuer;

import java.util.ArrayList;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.gcmModule.GCM;
import com.whitepowder.skier.SkierModeService;
import com.whitepowder.skier.SkierModeStopperThread;
import com.whitepowder.storage.SyncThread;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.userManagement.User;
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
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
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

	
	public static String GCM_ALERT_INTENT_ACTION = "GCM_ALERT_INTENT_ACTION";
	public static String GCM_ACCIDENT_INTENT_ACTION = "GCM_ACCIDENT_INTENT_ACTION";
	private static int RESCUER_MAP_ACTIVITY_REQUEST_CODE = 1004;
	
	private Context mContext;
	private LocationManager mLocationManager;
	private ServiceConnection mConnection;
	private AlertDialog alertNoGPS = null;
	private BroadcastReceiver serviceBroadcastReciever=null;
	private SkierModeService mBoundService;
	private ImageButton butSubmenu;

	private BroadcastReceiver mAlertReceiver = null;
	private BroadcastReceiver mAccidentReceiver = null;
	
	private ArrayList<Victim> accidents;
	public InboxAdapter adapter;
	private Gson gson;
	
	//Sync
	private ProgressDialog progressDialogSync;
	private BroadcastReceiver syncFinishedBroadcastReciever=null;
	private SyncThread sth;
	
	//Vibrator
	Vibrator mVibrator=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		gson = new Gson();
		setContentView(R.layout.rescuer_inbox);
		getActionBar().hide();
		
		//Initializate inbox
		new AccidentDownloaderThread(this).execute();
		
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
		
		//Initializates vibrator
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		//Register for GCM alerts
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
	                                	new SetAccidentAsAttendedThread(adapter.getItem(position).getId(), getApplicationContext()).start();
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
				String mensaje=null;
				
				if(position<accidents.size()){
				
					Victim item = adapter.getItem(position);
					ArrayList<Victim> items = new ArrayList<Victim>();
					items.add(item);
					mensaje = gson.toJson(items);
					
				}
				else{

					mensaje = gson.toJson(accidents);
				};
				
				Intent intent = new Intent(RescuerActivity.this, RescuerMap.class);
				intent.putExtra("accidentes",mensaje.toString());
				startActivityForResult(intent, RESCUER_MAP_ACTIVITY_REQUEST_CODE);
		
			}
		});
		
	};
	
	
	private void setupRescuerMode(){
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			alertNoGps();
		}
		else{
			//Binds to service
			bindService(new Intent(mContext, SkierModeService.class), mConnection, Context.BIND_AUTO_CREATE);			
		};	
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if((requestCode == RESCUER_MAP_ACTIVITY_REQUEST_CODE)&&(resultCode==RESULT_OK)){
			int nursedAccidentId = data.getExtras().getInt("rescued_id");			
			Victim vict = new Victim();
			vict.setId(nursedAccidentId);
			removeFromList(vict);
			
		};
	}
	
	private void alertNoGps() {
		if (alertNoGPS == null){
			final AlertDialog.Builder builder = new AlertDialog.Builder(RescuerActivity.this);
			builder.setMessage(getString(R.string.rescuer_no_gps_alert_text))
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
		
	};
	
	public void removeFromList(Victim victim){
		adapter.remove(victim);
		adapter.notifyDataSetChanged();
	}
	
	public void addToList(Victim victim){
		if(!accidents.contains(victim)){
			adapter.add(victim);
			adapter.notifyDataSetChanged();
		};
	};
	
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
	
	private void registerAccidentBroadcastReceiver(){
		mAccidentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (isOrderedBroadcast()) {
					setResultCode(RESULT_OK);
					
					String action = intent.getExtras().getString("action");
					String mensaje = intent.getExtras().getString("body");
					
					if(action.equals("add")){
						//Add the victim to the accident list	
						
						Victim victima = gson.fromJson(mensaje, Victim.class);
						
						if((victima.getId()!=-1)&&(victima.getX()!=null)&&(victima.getY()!=null)&&(victima.getUsername()!=null)){
							addToList(victima);
						};
						
						//Vibrate and display toast
						
						Toast.makeText(RescuerActivity.this, "White Powder ha recibido un nuevo accidente", Toast.LENGTH_SHORT).show();
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								mVibrator.vibrate(500);
								
							}
						}).start();					
					}
					
					else if(action.equals("remove")){

						int id = Integer.parseInt(mensaje);
						
						for (Victim vict : accidents) {
							if(vict.getId()==id){
								removeFromList(vict);
								break;
							};
						};
						
					}			
					
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
				popup.getMenuInflater().inflate(R.menu.popup_menu_submenu_rescuer, popup.getMenu());  

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
							
						case R.id.submenu_update_inbox:
							  
							new AccidentDownloaderThread(RescuerActivity.this).execute();
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
