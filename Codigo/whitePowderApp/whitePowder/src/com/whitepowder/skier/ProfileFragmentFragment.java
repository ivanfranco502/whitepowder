package com.whitepowder.skier;

import com.example.whitepowder.R;
import com.whitepowder.slope.recognizer.SlopeRecognizerActivity;
import com.whitepowder.user.management.LoginActivity;
import com.whitepowder.user.management.LoginThread;
import com.whitepowder.user.management.PasswordChangeActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragmentFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_fragment_profile, container,
				false);
		RelativeLayout butStats = (RelativeLayout) rootView.findViewById(R.id.profile_view_stats_container);
		RelativeLayout butChangePassword = (RelativeLayout) rootView.findViewById(R.id.profile_pwd_change_container);
		Switch switchSkierMode = (Switch) rootView.findViewById(R.id.profile_skier_mode_switch);
		
		butStats.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO ver estadísticas
					
			}
		});
		
		butChangePassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PasswordChangeActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		switchSkierMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The switch is enabled
		        } else {
		            // The switch is disabled
		        }
		    }
		});
		
		return rootView;
	}

}
