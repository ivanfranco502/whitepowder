package com.whitepowder.skier;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.whitepowder.R;

public class ProfileFragmentFragment extends Fragment {
	SkierActivity mSkierActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSkierActivity = (SkierActivity) getActivity();
		View rootView = inflater.inflate(R.layout.skier_fragment_profile, container,
				false);
		RelativeLayout butPersInfo = (RelativeLayout) rootView.findViewById(R.id.profile_view_personal_info_container);
		RelativeLayout butStats = (RelativeLayout) rootView.findViewById(R.id.profile_view_stats_container);
		
		//DATOS PERSONALES
		butPersInfo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO ver Datos personales
			}
		});
		
		//ESTADÍSTICAS
		butStats.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO ver estadísticas
					
			}
		});
		

		
		return rootView;
	}

}
