package com.whitepowder.skier;

import com.whitepowder.skier.emergency.EmergencyThread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonIntentReceiver extends BroadcastReceiver{
	public MediaButtonIntentReceiver() {
	    super();
	}
	
	private Context mContext = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    mContext = context; 
		String intentAction = intent.getAction();
	    if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
	        return;
	    }
	    KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
	    if (event == null) {
	        return;
	    }
	    int action = event.getAction();
	    if (action == KeyEvent.ACTION_DOWN || action == KeyEvent.KEYCODE_HEADSETHOOK || action == KeyEvent.KEYCODE_BREAK) {
	    // do something
	    	SkierActivity.firstPressTime = SkierActivity.secondPressTime;
	    	SkierActivity.secondPressTime = SkierActivity.thirdPressTime;
	    	SkierActivity.thirdPressTime = System.currentTimeMillis();
	        long delta1 = SkierActivity.thirdPressTime - SkierActivity.secondPressTime;
	        long delta2 = SkierActivity.secondPressTime - SkierActivity.firstPressTime;

	        // Case for triple click
	        if(delta1 < SkierActivity.TRIPLE_CLICK_DELAY && delta2 < SkierActivity.TRIPLE_CLICK_DELAY){
	            // Do something for triple click 
	        	Toast.makeText(mContext, "¡Triple click!", Toast.LENGTH_LONG).show();
	        	EmergencyThread et = new EmergencyThread(SkierActivity.skierActivity, SkierActivity.skierActivity.getApplicationContext());
				et.execute();
	        }
	    }
	    
	    abortBroadcast();
	}
}
