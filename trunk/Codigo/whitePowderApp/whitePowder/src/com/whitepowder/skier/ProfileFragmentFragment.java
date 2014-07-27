package com.whitepowder.skier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.whitepowder.R;
import com.whitepowder.Logout;
import com.whitepowder.user.management.PasswordChangeActivity;

public class ProfileFragmentFragment extends Fragment {
	Activity mSkierActivity;
	private static final int PWD_CHANGE_REQUEST_CODE = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSkierActivity = getActivity();
		View rootView = inflater.inflate(R.layout.skier_fragment_profile, container,
				false);
		RelativeLayout butStats = (RelativeLayout) rootView.findViewById(R.id.profile_view_stats_container);
		RelativeLayout butChangePassword = (RelativeLayout) rootView.findViewById(R.id.profile_pwd_change_container);
		Switch switchSkierMode = (Switch) rootView.findViewById(R.id.profile_skier_mode_switch);
		RelativeLayout butLogout = (RelativeLayout) rootView.findViewById(R.id.profile_logout_container);
		
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
				mSkierActivity.startActivityForResult(intent, PWD_CHANGE_REQUEST_CODE);
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
		
		butLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mSkierActivity)
		        .setTitle(getString(R.string.alert_logout_title))
		        .setMessage(getString(R.string.alert_logout_message))
		        .setNegativeButton(getString(R.string.alert_no), null)
		        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface arg0, int arg1) {
		            	Logout.logout(mSkierActivity);
		            }
		        }).create().show();
			}
		});
		
		return rootView;
	}

}
