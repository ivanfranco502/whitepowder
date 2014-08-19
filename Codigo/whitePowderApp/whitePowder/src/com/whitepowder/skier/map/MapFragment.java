package com.whitepowder.skier.map;

import com.example.whitepowder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	
	private static View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(rootView != null){
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null){
				parent.removeView(rootView);
			}
		}
		
		try{
			rootView = inflater.inflate(R.layout.skier_fragment_map, container,false);
		}
		catch (InflateException e){
			/* map is already there, just return view as it is */
		}
		return rootView;		
		
	}
	
	@Override
	public void onStart(){
		super.onStart();
	
	}

}
