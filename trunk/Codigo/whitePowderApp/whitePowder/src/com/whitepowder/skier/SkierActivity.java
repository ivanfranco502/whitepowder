package com.whitepowder.skier;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.whitepowder.skier.MediaButtonIntentReceiver;
import com.example.whitepowder.R;
import com.whitepowder.gcmModule.AlertDisplayActivity;
import com.whitepowder.gcmModule.GCM;
import com.whitepowder.skier.basicInformation.BasicInformationActivity;
import com.whitepowder.skier.basicInformation.BasicInformationForecastActivity;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.skier.map.MapActivity;
import com.whitepowder.skier.normsAndSigns.NASActivity;
import com.whitepowder.skier.statistics.StatisticsActivity;
import com.whitepowder.storage.SyncThread;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.utils.Logout;

public class SkierActivity extends Activity {

	SkierActivity mContext;
	
	final int REGISTER_REQUEST_CODE = 1;
	public final int PWD_CHANGE_REQUEST_CODE = 1;
	public static String GCM_ALERT_INTENT_ACTION = "GCM_ALERT_INTENT_ACTION";
	
	//Location and communication with thread
	private LocationManager mLocationManager;
	private BroadcastReceiver serviceBroadcastReciever=null;
	private SkierModeService mBoundService;
	private ServiceConnection mConnection;
	
	//GCM Alerts
	BroadcastReceiver mAlertReceiver=null;
	
	//Sync
	private ProgressDialog progressDialogSync;
	private BroadcastReceiver syncFinishedBroadcastReciever=null;
	private SyncThread sth;
	
	//Buttons
	private ImageButton butSubmenu;
	private ImageButton butInfoBasic;
	private ImageButton butClima;
	private ImageButton butSecurity;
	private ImageButton butStatistics;
	private ImageButton butMap;		
	private ImageButton butSkiermode;
	
	//SeekBar emergency
	private boolean seekBarProgress;
	
	//Peripheral
	private MediaButtonIntentReceiver r;
	static public SkierActivity skierActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.skier_activity_main);
		mContext = this;
		loadButtons();	
		
		//Enables GCM
		new GCM(mContext);
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "Su dispositivo no posee GPS", Toast.LENGTH_SHORT ).show();
		};

        //Create service connection
        createServiceConnectionAndRegisterForBroadcast();
		
        //Register for GCM alerts
        registerAlertBroadcastReciever();
        
		//Setups buttons
		
        setupPopupMenu();	    
        setupSkierModeButton();       
        setupBasikcInformationButton();  
        setupForecastButton();
        setupSecurityButton();
        setupMapButton();
        setupEmergencyButton();
        setupStatisticsButton();
        setupPeripheralIntegratorMasterManager();
 	
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(syncFinishedBroadcastReciever!=null){
			unregisterReceiver(syncFinishedBroadcastReciever);
		};
		
		if(mAlertReceiver!=null){
			unregisterReceiver(mAlertReceiver);
		};
		
		stopSkierMode();
		
		synchronized (this) {
	        if(r != null){
	            try{
	            	unregisterReceiver(r);
	            }
	            catch(IllegalArgumentException e){	            	
	            }
	        }
	    }
		
	};

	private void registerAlertBroadcastReciever(){

		mAlertReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if (isOrderedBroadcast()) {
					setResultCode(RESULT_OK);
					
					//Starts Alert display activity
					
					Intent alertDisplay = new Intent(mContext,AlertDisplayActivity.class);
					alertDisplay.putExtras(intent.getExtras());
					alertDisplay.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(alertDisplay);

				};
			}
		};
		
		registerReceiver(mAlertReceiver, new IntentFilter(SkierActivity.GCM_ALERT_INTENT_ACTION));
		
		
	};
	
	
	private void createServiceConnectionAndRegisterForBroadcast(){
		
		
		mConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				mBoundService = ((SkierModeService.LocalBinder)service).getService();
				
				serviceBroadcastReciever = new BroadcastReceiver() {

			        @Override
			        public void onReceive(Context context, Intent intent) {
			        	stopSkierMode();
						Toast.makeText(mContext, "Por favor active el GPS para utilizar el modo esquiador.", Toast.LENGTH_SHORT).show();
			        };
			        	        
			    };
			    registerReceiver(serviceBroadcastReciever, new IntentFilter(mBoundService.getIntentStopSkierModeAction()));
			};

			@Override
			public void onServiceDisconnected(ComponentName className) {
				
				stopSkierMode();
				Toast.makeText(mContext, "El modo esquiador ha sido desactivado.", Toast.LENGTH_SHORT).show();
			};
			
		};   
	}

	private void loadButtons(){
		butSubmenu = (ImageButton) findViewById(R.id.bt_submenu);
		butInfoBasic = (ImageButton) findViewById(R.id.bt_basicinfo);
		butClima = (ImageButton) findViewById(R.id.bt_clima);
		butSecurity = (ImageButton) findViewById(R.id.bt_security);
		butStatistics = (ImageButton) findViewById(R.id.bt_statistics);
		butMap = (ImageButton) findViewById(R.id.bt_map);		
		butSkiermode = (ImageButton) findViewById(R.id.bt_skiermode);
	}
	
	private void setupSkierModeButton(){
        
		//Setups skier mode button
        butSkiermode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(!v.isSelected()){
					if (!mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
						alertNoGps();
					}
					else{
						//Binds to service
						bindService(new Intent(mContext, SkierModeService.class), mConnection, Context.BIND_AUTO_CREATE);						

						v.setSelected(true);
					};			
					
				}
				else{
					stopSkierMode();				
				};
				
				
			}
		});
	}
	
	private void stopSkierMode(){

		if(mBoundService!=null){
			unbindService(mConnection);
		};
		
		mBoundService = null;
		
		if(serviceBroadcastReciever!=null){
			unregisterReceiver(serviceBroadcastReciever);
			serviceBroadcastReciever=null;
		};
		
		butSkiermode.setSelected(false);

		//Starts thread that notifies server
		new SkierModeStopperThread(mContext).start();
	}
	
	private void setupBasikcInformationButton(){
		butInfoBasic.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BasicInformationActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	private void setupForecastButton(){
		butClima.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BasicInformationForecastActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void setupMapButton(){
		butMap.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MapActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	private void setupSecurityButton(){
		butSecurity.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, NASActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	private void setupPeripheralIntegratorMasterManager(){
		skierActivity = this;
		IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON); //"android.intent.action.MEDIA_BUTTON"
	    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY + 1);
		r = new MediaButtonIntentReceiver();
	    registerReceiver(r, filter);
	    
	    ((AudioManager)getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(
                this, MediaButtonIntentReceiver.class));
	}
	
	
	private void setupStatisticsButton(){
		butStatistics.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, StatisticsActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	private void setupEmergencyButton(){
		SeekBar emergencySeekBar = (SeekBar) findViewById(R.id.emergency_seekBar);
        
		emergencySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      
			@Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
				if(seekBarProgress){
					if(seekBar.getProgress() >= 85 && seekBar.getProgress() <= 100){
						//llamar emergencia
						EmergencyThread et = new EmergencyThread(SkierActivity.this, getApplicationContext());
						et.execute();
					}
					else{
						ImageView emergencyImage = (ImageView) findViewById(R.id.emergencyAnimation);
						animateSkier(emergencyImage);
					}
				}
				seekBar.setProgress(0);
		    }
		      
		    

			@Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    	if(seekBar.getProgress() >= 0 && seekBar.getProgress() <= 15){
		    		seekBarProgress = true;
				}else{
					seekBarProgress = false;
				}
		    }
		      
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
			});
    
	}
	
	private void animateSkier(final ImageView emergencyImage) {
		TranslateAnimation moveLeftToRight = new TranslateAnimation(0, 200, 0, 0);
    	moveLeftToRight.setDuration(2000);
    	emergencyImage.setVisibility(View.VISIBLE);
    	emergencyImage.startAnimation(moveLeftToRight);
    	emergencyImage.setVisibility(View.INVISIBLE);
		
	}
	
	
	private void setupPopupMenu(){
		
		//Setups options menu
		butSubmenu.setOnClickListener(new OnClickListener() {  
			@Override 
			public void onClick(View v) {  
	         	 
				PopupMenu popup = new PopupMenu(SkierActivity.this, butSubmenu);  
				popup.getMenuInflater().inflate(R.menu.popup_menu_submenu, popup.getMenu());  

				//registering popup with OnMenuItemClickListener  
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case R.id.submenu_logout:      			  
							new AlertDialog.Builder(SkierActivity.this)
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
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	};
	
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	        .setTitle(getString(R.string.alert_exit_title))
	        .setMessage(getString(R.string.alert_exit_message))
	        .setNegativeButton(getString(R.string.alert_no), null)
	        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	            	
	            	SkierActivity.this.finish();
	            }
	        }).create().show();
	};
	

	
	public void startActivityForResult(int requestCode){
		Intent intent = new Intent(this, PasswordChangeActivity.class);
		startActivityForResult(intent, requestCode);
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == PWD_CHANGE_REQUEST_CODE){
			if(resultCode==1){
				Toast.makeText(this, R.string.pwd_change_successful, Toast.LENGTH_SHORT).show();
			};
		}
	};
	
	private void alertNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
		}
		else{	
			progressDialogSync.dismiss();
			Toast.makeText(mContext, getResources().getString(R.string.sync_toast_error), Toast.LENGTH_SHORT).show();
			
		};
		
	};

	@Override
	public void onPause(){
		super.onPause();
		synchronized (this) {
	        if(r != null){
	            try{
	            	unregisterReceiver(r);
	            }
	            catch(IllegalArgumentException e){	            	
	            }
	        }
	    }
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//setupPeripheralIntegratorMasterManager();
	}
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
			EmergencyPeripheral.handlePeripheralEvent();
	        return true;
		}
		else{
			return super.onKeyUp(keyCode, event);
		}
	}
}
