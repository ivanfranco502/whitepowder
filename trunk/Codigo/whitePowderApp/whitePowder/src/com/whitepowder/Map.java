package com.whitepowder;

import com.example.whitepowder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Map extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_map, container,
				false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.map_label);
		textView.setText("Ac� va el mapa y la se�alizaci�n de pistas.");
		return rootView;
	}

}
