package com.example.whitepowder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mContext = getApplicationContext();
		RelativeLayout butLogin = (RelativeLayout)findViewById(R.id.login_login_button);	
		TextView butRegister = (TextView)findViewById(R.id.login_register_button);
		TextView butResetPassword = (TextView)findViewById(R.id.login_reset_button);
		
		
		//On login pressed method				
		butLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUser = (TextView)findViewById(R.id.login_username_input);
				TextView inputPassword = (TextView)findViewById(R.id.login_password_input);				
				LoginThread lt = new LoginThread(getApplicationContext());
				lt.execute(inputUser.getText().toString(),inputPassword.getText().toString());	
			}
		});
		
		butRegister.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,RegisterActivity.class);
				startActivity(intent);
				
			}
		});
		
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
