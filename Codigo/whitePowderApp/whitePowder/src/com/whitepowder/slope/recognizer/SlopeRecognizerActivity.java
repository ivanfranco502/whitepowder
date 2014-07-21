package com.whitepowder.slope.recognizer;

import android.app.Activity;
import android.os.Bundle;

public class SlopeRecognizerActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(this);
		sdt.execute();
	}

}
