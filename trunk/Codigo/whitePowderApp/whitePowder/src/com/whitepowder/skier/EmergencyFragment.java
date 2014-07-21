package com.whitepowder.skier;

import com.example.whitepowder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmergencyFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_emergency, container,
				false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.emergency_label);
		textView.setText("Acá va el botón de emergencia.");
		return rootView;
	}

}
