package com.whitepowder.skier.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.whitepowder.R;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.skier.emergency.EmergencyThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class StatisticsActivity extends Activity {

	Context mContext =null;
	StatisticsManager userStats=null;
	Boolean gotStats=false;
	StatisticsActivity mStatisticsActivity = null;
	
	//SeekBar emergency
	private boolean seekBarProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.skier_statistics);
		mContext = getApplicationContext();
		mStatisticsActivity = this;
		
		//Setups buttons
		setupClearStatsButton();
		setupShareStatsButton();
		
		//Loads user stats
		
		userStats = StatisticsManager.getInstance(mContext);
		
		//Loads max speed
		
		if(userStats.stats.getMaxSpeed()!=0){
			TextView maxSpeed = (TextView) findViewById(R.id.skier_statistics_speed_record_value);
			maxSpeed.setTextColor(Color.BLACK);
			maxSpeed.setText(String.format("%.2f", userStats.stats.getMaxSpeed())+" km/h");
			gotStats=true;
			
			if(userStats.stats.getMaxSpeedDate()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				
				TextView maxSpeedDate = (TextView) findViewById(R.id.skier_statistics_speed_record_date);
				maxSpeedDate.setText(sdf.format(userStats.stats.getMaxSpeedDate()));
				
			};
		};
		
		//Loads max altitude
		
		if(userStats.stats.getMaxAltitude()!=0){
			TextView maxAltitude = (TextView) findViewById(R.id.skier_statistics_height_record_value);
			maxAltitude.setText(String.format("%.0f", userStats.stats.getMaxAltitude())+" mts");
			maxAltitude.setTextColor(Color.BLACK);
			gotStats=true;
			
			if(userStats.stats.getMaxAltitudeDate()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.US);
				
				TextView maxAltitudeDate = (TextView) findViewById(R.id.skier_statistics_height_record_date);
				maxAltitudeDate.setText(sdf.format(userStats.stats.getMaxAltitudeDate()));
				
			};
		};
		
		//Loads average speed
		
		if(userStats.stats.getAverageSpeed()!=0){
			TextView averageSpeed = (TextView) findViewById(R.id.skier_statistics_average_speed_value);
			averageSpeed.setText(String.format("%.3f", userStats.stats.getAverageSpeed())+" km/h");
			averageSpeed.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		//Loads total distance
		
		if(userStats.stats.getTotalDistance()!=0){
			TextView totalDist = (TextView) findViewById(R.id.skier_statistics_total_distance_value);
			totalDist.setText(String.format("%.3f", userStats.stats.getTotalDistance())+" km");
			totalDist.setTextColor(Color.BLACK);
			gotStats=true;
		};
		
		if(!gotStats){
			Toast.makeText(mContext, R.string.skier_statistics_toast_no_stats, Toast.LENGTH_LONG).show();
		};
		
		setupEmergencyButton();
	
	};
	
	private void setupShareStatsButton() {
		RelativeLayout butCompartir = (RelativeLayout) findViewById(R.id.skier_statistics_share_button);
		
		butCompartir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ScrollView content = (ScrollView) findViewById(R.id.scroll_view);
				int totalHeight = content.getChildAt(0).getHeight();
			    int totalWidth = content.getChildAt(0).getWidth();

			    Bitmap bitmap = getBitmapFromView(content,totalHeight,totalWidth);
				
			    File file = null;
			    String file_path = null;
				if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {  
					file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ScreenStats";
					File dir = new File(file_path);
				         if(!dir.exists()){
				        	 dir.mkdirs();				        
				        	 } 
				    file = new File(dir, "screenStats" + ".png");
				}
				try{
					FileOutputStream ostream = new FileOutputStream(file);                                   
					bitmap.compress(CompressFormat.PNG, 10, ostream);
					ostream.close();	
				}
				catch(Exception e){
					
				}
				
				if(file != null){
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
				    shareIntent.setType("image/png");
				    shareIntent.putExtra(Intent.EXTRA_TEXT, "¡Mirá mis estadísticas esquiando a través de White Powder!");
				    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
				    startActivity(Intent.createChooser(shareIntent, "Compartir estadísticas"));
				}
				
			};
		});
	}

	private void setupClearStatsButton(){
		RelativeLayout butClearStats = (RelativeLayout) findViewById(R.id.skier_statistics_reset_button);
		
		butClearStats.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(StatisticsActivity.this)
		        .setTitle(R.string.skier_statistics_errase_stats_alert_title)
		        .setMessage(R.string.skier_statistics_errase_stats_alert_body)
		        .setNegativeButton(getString(R.string.alert_no), null)
		        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
		        	
		            public void onClick(DialogInterface arg0, int arg1) {
		            	//Clear stats
		            	userStats.clearStats();
		            	//Restarts activity
		            	StatisticsActivity.this.recreate();

		            };
		        }).create().show();
				
				
			}
		});
		
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
					if(seekBar.getProgress() >= 99 && seekBar.getProgress() <= 100){
						//llamar emergencia
						EmergencyThread et = new EmergencyThread(mStatisticsActivity, getApplicationContext());
						et.execute();
					}
					else{
						animateSkier();
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
	
	private void animateSkier() {
		final ImageView emergencyImage = (ImageView) findViewById(R.id.emergencyAnimation);
		final ImageView progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		final ImageView progressBar2 = (ImageView) findViewById(R.id.progressBar2);
		final TextView help = (TextView) findViewById(R.id.help);
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		TranslateAnimation moveLeftToRight = new TranslateAnimation(0, size.x, 0, 0);
    	moveLeftToRight.setDuration(2000);
    	moveLeftToRight.setRepeatCount(2);
    	moveLeftToRight.setAnimationListener(new AnimationListener(){
    	    public void onAnimationStart(Animation a){
    	    	emergencyImage.setVisibility(View.VISIBLE);
    	    	progressBar1.setVisibility(View.INVISIBLE);
    	    	progressBar2.setVisibility(View.INVISIBLE);
    	    	help.setVisibility(View.INVISIBLE);
    	    }
    	    public void onAnimationRepeat(Animation a){}
    	    public void onAnimationEnd(Animation a){
    	    	emergencyImage.setVisibility(View.INVISIBLE);
    	    	progressBar1.setVisibility(View.VISIBLE);
    	    	progressBar2.setVisibility(View.VISIBLE);
    	    	help.setVisibility(View.VISIBLE);
    	    }
    	});
    	
    
    	emergencyImage.startAnimation(moveLeftToRight);
		
	}	
	
	public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

	    Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(returnedBitmap);
	    Drawable bgDrawable = view.getBackground();
	    if (bgDrawable != null)
	        bgDrawable.draw(canvas);
	    else
	        canvas.drawColor(Color.WHITE);
	    view.draw(canvas);
	    return returnedBitmap;
	}
	
}
