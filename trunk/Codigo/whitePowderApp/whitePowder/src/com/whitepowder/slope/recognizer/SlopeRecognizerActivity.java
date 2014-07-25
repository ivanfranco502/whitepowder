package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

import com.example.whitepowder.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

public class SlopeRecognizerActivity extends Activity{
	
	public SlopeSpinnerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slope_recognition);
		
		//Generates hint option in slope spinner
		Spinner spinner = (Spinner) findViewById(R.id.slope_recognition_spinner);
		adapter = new SlopeSpinnerAdapter(this, generateDummySlope(), R.layout.slope_recognition_spinner_item);
		spinner.setAdapter(adapter);
		
		SlopeDownloaderThread sdt = new SlopeDownloaderThread(this);
		sdt.execute();
	}
	
	ArrayList<SimplifiedSlope> generateDummySlope(){
		ArrayList<SimplifiedSlope> array = new ArrayList<SimplifiedSlope>();
		SimplifiedSlope pista = new SimplifiedSlope();
		pista.setSlop_id(0);
		pista.setSlop_description("Seleccione una pista");
		array.add(pista);
		return array;
		
	}

	public SlopeSpinnerAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SlopeSpinnerAdapter adapter) {
		this.adapter = adapter;
	}

}
