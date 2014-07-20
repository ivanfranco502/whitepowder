package com.whitepowder;

import com.example.whitepowder.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BasicInformation extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_basic_information, container,
				false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.basic_information_label);
		textView.setText("Acá va la información básica del centro de esquí.");
		return rootView;
	}
}
