package com.whitepowder.userManagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.whitepowder.R;
import com.whitepowder.gcmModule.GCM;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.slopeRecognizer.SlopeRecognizerActivity;
import com.whitepowder.storage.SyncThread;

public class LoginActivity extends Activity {

	LoginActivity mContext;
	ProgressDialog progressDialogSync=null;
	final int REGISTER_REQUEST_CODE = 1;
	final int RESET_REQEST_CODE = 2;
	SyncThread sth;
	
	private BroadcastReceiver syncFinishedBroadcastReciever = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		checkSharedPreferences();
		setContentView(R.layout.login);
		
		new GCM(mContext);
		
		final RelativeLayout butLogin = (RelativeLayout)findViewById(R.id.login_login_button);	
		TextView butRegister = (TextView)findViewById(R.id.login_register_button);
		TextView butResetPassword = (TextView)findViewById(R.id.login_reset_button);
		TextView inputPassword = (TextView)findViewById(R.id.login_password_input);	
		
					
		butLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUser = (TextView)findViewById(R.id.login_username_input);
				TextView inputPassword = (TextView)findViewById(R.id.login_password_input);	
				if(isValidInput(inputUser.getText().toString(), inputPassword.getText().toString())){
					LoginThread lt = new LoginThread(LoginActivity.this, getApplicationContext());
					lt.execute(inputUser.getText().toString(),inputPassword.getText().toString());
				}
				else{
					Toast.makeText(mContext,R.string.login_error_incomplete_data,Toast.LENGTH_SHORT).show();
				};
					
			}
		});
		
		inputPassword.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	butLogin.performClick();
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});		
		
		butRegister.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,RegisterActivity.class);
				startActivityForResult(intent, REGISTER_REQUEST_CODE);
				
			}
		});
		
		butResetPassword.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PasswordResetActivity.class);
				startActivityForResult(intent, RESET_REQEST_CODE);
			}
		});
		
		
		
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(syncFinishedBroadcastReciever!=null){
			unregisterReceiver(syncFinishedBroadcastReciever);
		};

	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REGISTER_REQUEST_CODE){
			if(resultCode==1){
				Toast.makeText(this, R.string.login_register_successful, Toast.LENGTH_SHORT).show();
			};
		}
		else if(requestCode == RESET_REQEST_CODE){
			if(resultCode==1){
				Toast.makeText(this, R.string.login_reset_successful, Toast.LENGTH_SHORT).show();
			};
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private boolean isValidInput(String user, String pass){
		
		if(user.length()>0 && pass.length()>0){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public void loginAccordingToRole(boolean first_time){
		
		if(first_time){
		
			//Stats sync dialog
			progressDialogSync = new ProgressDialog(mContext);
			progressDialogSync.setMessage(getResources().getString(R.string.sync_dialog_message));
			progressDialogSync.setCancelable(false);
			progressDialogSync.setIndeterminate(true);
			progressDialogSync.show();
			
			//Starts sync task
			sth = new SyncThread();
			setupOnSyncFinishedListener();
			sth.execute(mContext);
		}
		
		else{
			launchCorrespondngActivity();
		};
	}
	
	public void setupOnSyncFinishedListener(){
		
		syncFinishedBroadcastReciever = new BroadcastReceiver() {

	        @Override
	        public void onReceive(Context context, Intent intent) {
				Boolean success = intent.getExtras().getBoolean("success");
				onSyncFinished(success);
	        };
	    };
		
		registerReceiver(syncFinishedBroadcastReciever, new IntentFilter(sth.getIntentOnSyncFinishedAction()));
	}
	
	public void onSyncFinished(boolean success){
		if (success){			
			//Stores user and password
			
			SharedPreferences sharedPreferences = getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString("_token", User.getUserInstance().getToken().toString());
			editor.putString("role", User.getUserInstance().getRole().toString());
			editor.apply();
			
			progressDialogSync.dismiss();			
			launchCorrespondngActivity();
		}
		else{
			
			progressDialogSync.dismiss();
			Toast.makeText(mContext, getResources().getString(R.string.sync_toast_error), Toast.LENGTH_SHORT).show();
			
		};
		
	}
	
	private void launchCorrespondngActivity(){
		Intent intent;
		
		if(User.getUserInstance().getRole().toString().equals("ROLE_SKIER")){
			//Rol esquiador
			intent = new Intent(mContext, SkierActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
		}
		else if (User.getUserInstance().getRole().toString().equals("ROLE_RECON")){
			//Rol reconocedor de pistas
			intent = new Intent(mContext, SlopeRecognizerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
		};
		
		this.finish();
	}
	
	private void checkSharedPreferences(){
		SharedPreferences sharedPreferences = getSharedPreferences("WP_USER_SHARED_PREFERENCES", Context.MODE_PRIVATE);
		String role = sharedPreferences.getString("role", "UNKNOWN");
		String token = sharedPreferences.getString("_token", "UNKNOWN");
		if(role != "UNKNOWN" && token != "UNKNOWN"){
			User.getUserInstance().setRole(role);
			User.getUserInstance().setToken(token);
			loginAccordingToRole(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
