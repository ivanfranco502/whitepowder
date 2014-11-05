package com.whitepowder.skier.normsAndSigns;

import java.io.IOException;
import java.io.InputStream;

import com.example.whitepowder.R;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;
import com.whitepowder.utils.ApplicationError;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SignActivity extends Activity {

	static public SkierActivity skierActivity;
	//SeekBar emergency
	private boolean seekBarProgress;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skier_activity_nas_sign);
		
		Sign sign = (Sign) getIntent().getSerializableExtra("SELECTED_SIGN");

		setTitle(sign.getName());
		
		SquareImageView signPicture = (SquareImageView) findViewById(R.id.signPicture);
		try {
			InputStream logoBitmap = this.getAssets().open(sign.getImage());
			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			signPicture.setImageBitmap(bitmap);
			
		} catch (IOException e) {
			new ApplicationError(1000, "Error","No se encuentra la se�al de seguridad");
		}
		
		TextView signDescription = (TextView) findViewById(R.id.sign_description);
		signDescription.setText(sign.getDescription());
		
		setupEmergencyButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.sign, menu);
		return true;
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
			EmergencyPeripheral.handlePeripheralEvent();
	        return true;
		}
		else{
			return super.onKeyUp(keyCode, event);
		}
	}
	
	private void setupEmergencyButton(){
		SeekBar emergencySeekBar = (SeekBar) findViewById(R.id.emergency_seekBar);
		
		emergencySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      
			@Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
				if(seekBarProgress){
					if(seekBar.getProgress() >= 85 && seekBar.getProgress() <= 100){
						//llamar emergencia
						EmergencyThread et = new EmergencyThread(skierActivity.skierActivity, getApplicationContext());
						et.execute();
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
}