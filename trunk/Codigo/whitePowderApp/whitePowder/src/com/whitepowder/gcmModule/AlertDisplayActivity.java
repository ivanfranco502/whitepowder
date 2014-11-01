package com.whitepowder.gcmModule;

import com.example.whitepowder.R;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.utils.ApplicationError;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
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
		
		playSound(getIntent().getExtras().getInt("id"));
		
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
	
	private void playSound(int id) {
		final AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		    public void onAudioFocusChange(int focusChange) {
		        
		    }
		};

		//Request audio focus for playback
		int result = am.requestAudioFocus(afChangeListener,
		                                 // Use the music stream.
		                                 AudioManager.STREAM_MUSIC,
		                                 // Request permanent focus.
		                                 AudioManager.AUDIOFOCUS_GAIN);
		   
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
		    // Start playback.
			MediaPlayer mediaPlayer = null;
			switch(id){
			case 1:
				mediaPlayer = MediaPlayer.create(this, R.raw.storm);
				break;
			case 2:
				mediaPlayer = MediaPlayer.create(this, R.raw.avalanche);
				break;
			case 3:
				mediaPlayer = MediaPlayer.create(this, R.raw.clown);
				break;
			}
			if(mediaPlayer != null){
				mediaPlayer.start(); // no need to call prepare(); create() does that for you
				while(mediaPlayer.isPlaying());
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
		
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
