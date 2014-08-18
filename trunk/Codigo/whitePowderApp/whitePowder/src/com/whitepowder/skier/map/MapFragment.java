package com.whitepowder.skier.map;

import com.example.whitepowder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_map, container,false);
		
		return rootView;		
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
	
	}

}
