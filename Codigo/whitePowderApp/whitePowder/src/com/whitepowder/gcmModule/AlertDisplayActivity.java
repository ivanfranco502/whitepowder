package com.whitepowder.gcmModule;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.whitepowder.R;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.utils.ApplicationError;

public class AlertDisplayActivity extends Activity {
	
	Thread vibratorThread=null;
	Boolean isStopped=false;
	Vibrator vibrator=null;
	
	AlertDisplayActivity mContext;
	
	private TextView alert_title;
	private TextView alert_body;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.skier_alert_display);
		mContext = this;
		
		final RelativeLayout butSend = (RelativeLayout)findViewById(R.id.discardButton);
		alert_title = (TextView) findViewById(R.id.alertSign);
		alert_body = (TextView) findViewById(R.id.alertBody);
		
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
		
		alert_title.setText(getIntent().getExtras().getString("title"));
		alert_body.setText(getIntent().getExtras().getString("body"));		
		
		butSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isStopped=true;
				finish();
				
			}
		});

		//AlertDialog.Builder blder = new AlertDialog.Builder(this);
		//blder.setCancelable(false);
	    //blder.setTitle(getIntent().getExtras().getString("title"));
	    //blder.setMessage(getIntent().getExtras().getString("body"));
	    //blder.setPositiveButton("DESCARTAR", new OnClickListener() {
			
			//@Override
			//public void onClick(DialogInterface dialog, int which) {
				//isStopped=true;
				//finish();
				
			//}
		//});
		//blder.create().show();
	}
	
	private void playSound(final int id) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
				
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
						mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.storm);
						break;
					case 2:
						mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.avalanche);
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
		}).start();

		
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
