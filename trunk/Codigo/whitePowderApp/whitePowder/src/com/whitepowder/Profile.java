package com.whitepowder;

import com.example.whitepowder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Profile extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_profile, container,
				false);
		TextView textView = (TextView) rootView
				.findViewById(R.id.profile_label);
		textView.setText("Ac� van las opciones del perfil: cambiar contrase�a y ver estad�sticas.");
		return rootView;
	}

}
