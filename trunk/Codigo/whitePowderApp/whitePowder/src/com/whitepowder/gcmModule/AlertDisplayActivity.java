package com.whitepowder.gcmModule;

import com.example.whitepowder.R;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.utils.ApplicationError;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;

public class AlertDisplayActivity extends Activity {
	
	Thread vibratorThread=null;
	Boolean isStopped=false;
	Vibrator vibrator=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.skier_alert_display);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		vibratorThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!isStopped){
					vibrator.vibrate(500);
					try {
						Thread.sleep(750);
					} 
					catch (InterruptedException e) {
						new ApplicationError(1300, "Warning","Warning al dormir el thread vibrador al recibir una alerta");
					};
				}
				
			}
		});
		
		vibratorThread.start();

		AlertDialog.Builder blder = new AlertDialog.Builder(this);
		blder.setCancelable(false);
	    blder.setTitle(getIntent().getExtras().getString("title"));
	    blder.setMessage(getIntent().getExtras().getString("body"));
	    blder.setPositiveButton("DESCARTAR", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isStopped=true;
				finish();
				
			}
		});
		blder.create().show();
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
