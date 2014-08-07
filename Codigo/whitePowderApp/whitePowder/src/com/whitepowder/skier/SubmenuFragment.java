package com.whitepowder.skier;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.whitepowder.R;
import com.whitepowder.Logout;

public class SubmenuFragment extends Fragment {
	
	SkierActivity mSkierActivity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mSkierActivity = (SkierActivity) getActivity();
		View rootView = inflater.inflate(R.layout.skier_fragment_submenu, container,
				false);
	
	RelativeLayout butChangePassword = (RelativeLayout) rootView.findViewById(R.id.profile_pwd_change_container);
	Switch switchSkierMode = (Switch) rootView.findViewById(R.id.profile_skier_mode_switch);
	RelativeLayout butLogout = (RelativeLayout) rootView.findViewById(R.id.profile_logout_container);

	//CAMBIAR CONTRASEÑA
	butChangePassword.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			mSkierActivity.startActivityForResult(mSkierActivity.PWD_CHANGE_REQUEST_CODE);
		}
	});
	
	//MODO ESQUIADOR
	switchSkierMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        if (isChecked) {
	            // The switch is enabled
	        } else {
	            // The switch is disabled
	        }
	    }
	});
	
	//CERRAR SESIÓN
	butLogout.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(mSkierActivity)
	        .setTitle(getString(R.string.alert_logout_title))
	        .setMessage(getString(R.string.alert_logout_message))
	        .setNegativeButton(getString(R.string.alert_no), null)
	        .setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	Logout.logout(mSkierActivity, false);
	            }
	        }).create().show();
		}
	});
	
	return rootView;
	}
	
}
