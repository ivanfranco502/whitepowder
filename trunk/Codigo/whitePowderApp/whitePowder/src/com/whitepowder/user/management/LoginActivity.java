package com.whitepowder.user.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whitepowder.R;

public class LoginActivity extends Activity {

	Context mContext;
	final int REGISTER_REQUEST_CODE = 1;
	final int RESET_REQEST_CODE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		mContext = getApplicationContext();
		
		//Hide the action bar
	//	ActionBar bar = getActionBar();
		//bar.hide();
		
		
		RelativeLayout butLogin = (RelativeLayout)findViewById(R.id.login_login_button);	
		TextView butRegister = (TextView)findViewById(R.id.login_register_button);
		TextView butResetPassword = (TextView)findViewById(R.id.login_reset_button);
		
					
		butLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUser = (TextView)findViewById(R.id.login_username_input);
				TextView inputPassword = (TextView)findViewById(R.id.login_password_input);				
				LoginThread lt = new LoginThread(LoginActivity.this, getApplicationContext());
				lt.execute(inputUser.getText().toString(),inputPassword.getText().toString());	
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
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == REGISTER_REQUEST_CODE){
			if(resultCode==0){
				//TODO deshardcode
				Toast.makeText(this, "Registro OK!", Toast.LENGTH_SHORT).show();
			};
		};
		if(requestCode == RESET_REQEST_CODE){
			if(resultCode==0){
				Toast.makeText(this, "Reseteo OK!", Toast.LENGTH_SHORT).show();
			};
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
