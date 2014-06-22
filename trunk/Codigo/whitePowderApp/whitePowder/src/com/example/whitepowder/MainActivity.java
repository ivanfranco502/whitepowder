package com.example.whitepowder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		RelativeLayout butLogin = (RelativeLayout)findViewById(R.id.ingresarButton);	
		
		//Bot�n ingresar on click method				
		butLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUser = (TextView)findViewById(R.id.userInput);
				TextView inputPassword = (TextView)findViewById(R.id.passwordInput);
				
				//TODO validate input
				
				LoginThread lt = new LoginThread();
				lt.execute(inputUser.getText().toString(),inputPassword.getText().toString());
				
				
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
