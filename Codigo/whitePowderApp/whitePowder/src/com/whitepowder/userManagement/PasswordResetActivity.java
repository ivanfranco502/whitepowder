package com.whitepowder.userManagement;

import com.example.whitepowder.R;
import com.whitepowder.utils.Security;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
		
		final RelativeLayout butSend = (RelativeLayout)findViewById(R.id.pwd_reset_send_button);
		TextView inputEmail = (TextView) findViewById(R.id.pwd_reset_email_input);
		
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
		
		inputEmail.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	butSend.performClick();
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
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
