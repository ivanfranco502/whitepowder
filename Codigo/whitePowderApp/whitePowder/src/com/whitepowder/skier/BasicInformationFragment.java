package com.whitepowder.skier;

import com.example.whitepowder.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BasicInformationFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_basic_information, container,
				false);
		
		TextView ski_center_details = (TextView) rootView.findViewById(R.id.ski_center_details);
		
		
		return rootView;
	}
}
