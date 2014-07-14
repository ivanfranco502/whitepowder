package com.whitepowder;

import com.example.whitepowder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PasswordResetActivity extends Activity  {
	
	PasswordResetActivity mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_reset);
		mContext = this;
		
		RelativeLayout butSend = (RelativeLayout)findViewById(R.id.pwd_reset_send_button);
		
		butSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputEmail = (TextView) findViewById(R.id.pwd_reset_email_input);
	
				PasswordResetThread rt = new PasswordResetThread(mContext);
				rt.execute(inputEmail.getText().toString());
			}
		});
	}

}
