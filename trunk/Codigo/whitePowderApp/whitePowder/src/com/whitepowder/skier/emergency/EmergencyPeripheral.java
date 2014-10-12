package com.whitepowder.skier.emergency;

import com.whitepowder.skier.SkierActivity;

public abstract class EmergencyPeripheral {
	static final long TRIPLE_CLICK_DELAY = 1000;
	static long firstPressTime  = 0; // oldValue
	static long secondPressTime = 0; // oldValue
	static long thirdPressTime  = System.currentTimeMillis();
	
	public static void handlePeripheralEvent(){
		firstPressTime = secondPressTime;
    	secondPressTime = thirdPressTime;
    	thirdPressTime = System.currentTimeMillis();
        long delta1 = thirdPressTime - secondPressTime;
        long delta2 = secondPressTime - firstPressTime;

        // Case for triple click
        if(delta1 < TRIPLE_CLICK_DELAY && delta2 < TRIPLE_CLICK_DELAY){
            // Do something for triple click 
        	EmergencyThread et = new EmergencyThread(SkierActivity.skierActivity, SkierActivity.skierActivity.getApplicationContext());
			et.execute();
        }
	}
}
