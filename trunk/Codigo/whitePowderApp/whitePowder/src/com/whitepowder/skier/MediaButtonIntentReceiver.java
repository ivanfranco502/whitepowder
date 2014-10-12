package com.whitepowder.skier;

import com.whitepowder.skier.emergency.EmergencyPeripheral;
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
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
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
	    	EmergencyPeripheral.handlePeripheralEvent();
	    }
	    
	    abortBroadcast();
	}
}
