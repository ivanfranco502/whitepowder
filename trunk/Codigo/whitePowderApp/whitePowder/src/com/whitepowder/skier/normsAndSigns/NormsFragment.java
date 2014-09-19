package com.whitepowder.skier.normsAndSigns;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whitepowder.R;


public class NormsFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_activity_nas_norms, container,
				false);
		return rootView;
	}
	
}
