package com.whitepowder.slope.recognizer;

import com.example.whitepowder.R;

import android.app.Activity;
import android.os.Bundle;

public class SlopeRecognizerActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slope_recognition);
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(this);
		sdt.execute();
	}

}
