package com.whitepowder;

import com.whitepowder.user.management.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Logout {
	public static void logout(Activity mActivity){
		Context mContext = mActivity.getApplicationContext();
		
		SharedPreferences sharedPreferences = mContext.getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear(); 
		editor.apply();
		
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mActivity.startActivity(intent);
		
		mActivity.finish();
		
		return;
	}
}
