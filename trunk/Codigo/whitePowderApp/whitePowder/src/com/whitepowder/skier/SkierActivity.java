package com.whitepowder.skier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.whitepowder.R;
import com.whitepowder.skier.basicInformation.BasicInformationActivity;
import com.whitepowder.skier.basicInformation.BasicInformationForecast;
import com.whitepowder.skier.map.MapActivity;
import com.whitepowder.userManagement.PasswordChangeActivity;
import com.whitepowder.utils.Logout;

public class SkierActivity extends Activity {

	SkierActivity mContext;
	
	final int REGISTER_REQUEST_CODE = 1;
	public final int PWD_CHANGE_REQUEST_CODE = 1;
	
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private int TIME_BETWEEN_POINTS = 4000;
	
	private ImageButton butSubmenu;
	private ImageButton butInfoBasic;
	private ImageButton butClima;
	private ImageButton butSecurity;
	private ImageButton butStatistics;
	private ImageButton butMap;		
	private ImageButton butSkiermode;
	
	public BasicInformationForecast[] basicInformationForecast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.skier_activity_main);
		mContext = this;
		loadButtons();
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "Su dispositivo no posee GPS", Toast.LENGTH_SHORT ).show();
		};
	
		//Setups buttons
		
        setupPopupMenu();	    
        setupSkierModeButton();       
        setupBasikcInformationButton();  
        setupMapButton();

		
	};

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
				
						//Enables location listener
						enableLocationListener();
						
						//Register for location changes	
						mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_BETWEEN_POINTS, 0,mLocationListener);
						
						v.setSelected(true);
					};
					
				}
				else{
					//Disenables location listener
					mLocationManager.removeUpdates(mLocationListener);
					v.setSelected(false);
				};
			}
		});
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
	
	private void setupMapButton(){
		butMap.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MapActivity.class);
				startActivity(intent);
				
			}
		});
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	};
	
	private void enableLocationListener(){
		mLocationListener = new LocationListener() {

			// Called back when location changes

			@Override
			public void onLocationChanged(Location arg0) {
				Log.i("pos","poas");
				
				Intent i = new Intent(mContext, SkierModeService.class);
				mContext.startService(i);
				
			}
			
			@Override
			public void onProviderDisabled(String arg0) {
				//TODO do something
			}

			@Override
			public void onProviderEnabled(String arg0) {
				return;
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			};
		};

	};
	
	private void alertNoGps() {
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
}
