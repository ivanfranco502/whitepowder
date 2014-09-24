package com.whitepowder.utils;

import com.example.whitepowder.R;
import com.whitepowder.userManagement.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class Logout {
	public static void logout(Activity mActivity, boolean tokenError){
		Context mContext = mActivity.getApplicationContext();
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_MULTI_PROCESS);
		Editor editor = sharedPreferences.edit();
		editor.clear(); 
		editor.commit();
		
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		mActivity.startActivity(intent);
				
		if(tokenError){
			Toast.makeText(mContext,R.string.error_invalid_token,Toast.LENGTH_SHORT).show();
		}
		
		return;
	}
	
	public static void logout(Context mContext, boolean tokenError){
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_MULTI_PROCESS);
		Editor editor = sharedPreferences.edit();
		editor.clear(); 
		editor.commit();
		
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
				
		if(tokenError){
			Toast.makeText(mContext,R.string.error_invalid_token,Toast.LENGTH_SHORT).show();
		}
		
		return;
	}
}
