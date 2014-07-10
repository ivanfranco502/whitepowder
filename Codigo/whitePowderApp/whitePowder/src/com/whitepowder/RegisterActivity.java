package com.whitepowder;

import com.example.whitepowder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	RegisterActivity mContext;	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		mContext = this;
		RelativeLayout butRegister = (RelativeLayout)findViewById(R.id.register_register_button);	
		
		butRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUsername = (TextView)findViewById(R.id.register_username_input);
				TextView inputPassword = (TextView)findViewById(R.id.register_password_input);		
				TextView inputEmail = (TextView) findViewById(R.id.register_email_input);
				TextView inputRepeatedPassword = (TextView) findViewById(R.id.register_repeat_password_input);
				
				RegisterThread rt = new RegisterThread(mContext);
				rt.execute(inputUsername.getText().toString(),inputPassword.getText().toString(),inputRepeatedPassword.getText().toString(),inputEmail.getText().toString());	
			}
		});

	}
}
