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
		textView.setText("Acá van las opciones del perfil: cambiar contraseña y ver estadísticas.");
		return rootView;
	}

}
