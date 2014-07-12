package com.whitepowder;

import com.example.whitepowder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PasswordChangeActivity extends Activity {

	PasswordChangeActivity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_change);
		mContext = this;
		
		RelativeLayout butSend = (RelativeLayout)findViewById(R.id.register_register_button);
		
		butSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputUsername = (TextView) findViewById(R.id.pwd_change_username_input);
				TextView inputCurrentPassword = (TextView) findViewById(R.id.pwd_change_current_password_input);
				TextView inputNewPassword = (TextView) findViewById(R.id.pwd_change_new_password_input);
				TextView inputRepeatedNewPassword = (TextView) findViewById(R.id.pwd_change_repeat_new_password_input);
				
				PasswordChangeThread rt = new PasswordChangeThread(mContext);
				rt.execute(inputUsername.getText().toString(),inputCurrentPassword.getText().toString(),inputNewPassword.getText().toString(),inputRepeatedNewPassword.getText().toString());
			}
		});
	}
}
