package com.whitepowder.skier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
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
	
	public BasicInformationForecast[] basicInformationForecast;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skier_activity_main);
		mContext = this;

		final ImageButton butSubmenu = (ImageButton) findViewById(R.id.bt_submenu); 
		ImageButton butSkiermode = (ImageButton) findViewById(R.id.bt_skiermode);
		ImageButton butInfoBasic = (ImageButton) findViewById(R.id.bt_basicinfo);
		ImageButton butClima = (ImageButton) findViewById(R.id.bt_clima);
		ImageButton butSecurity = (ImageButton) findViewById(R.id.bt_security);
		ImageButton butStatistics = (ImageButton) findViewById(R.id.bt_statistics);
		ImageButton butMap = (ImageButton) findViewById(R.id.bt_map);		
         
        
        butSubmenu.setOnClickListener(new OnClickListener() {  
         @Override  
         public void onClick(View v) {  
          //Creating the instance of PopupMenu  
          PopupMenu popup = new PopupMenu(SkierActivity.this, butSubmenu);  
          //Inflating the Popup using xml file  
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

          popup.show();//showing popup menu  
         }  
        });//closing the setOnClickListener method */
        
        //Set Skiermode Button
        butSkiermode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				v.setSelected(!v.isSelected());
			}

		});
		     
		
		butInfoBasic.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BasicInformationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
		
		butMap.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MapActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
			}
		});
		
		
		

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
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
	}
	

	
	public void startActivityForResult(int requestCode){
		Intent intent = new Intent(this, PasswordChangeActivity.class);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == PWD_CHANGE_REQUEST_CODE){
			if(resultCode==1){
				Toast.makeText(this, R.string.pwd_change_successful, Toast.LENGTH_SHORT).show();
			};
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
