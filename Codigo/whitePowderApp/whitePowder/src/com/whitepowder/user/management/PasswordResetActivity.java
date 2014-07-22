package com.whitepowder.user.management;

import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;
import com.whitepowder.Security;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
				if(isValidInput(inputEmail.getText().toString())){
					PasswordResetThread rt = new PasswordResetThread(mContext);
					rt.execute(inputEmail.getText().toString());
				}
				else{
					Toast.makeText(mContext,R.string.reset_invalid_email,Toast.LENGTH_SHORT).show();
				}

				
			}
		});
	}
	
	private boolean isValidInput(String email){
		
		if(Security.isValidEmail(email)){
			return true;
		}
		else{
			return false;
		}
		
	}
}
