package com.whitepowder.userManagement;

import com.example.whitepowder.R;
import com.whitepowder.skier.emergency.EmergencyPeripheral;
import com.whitepowder.utils.ApplicationError;
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

public class PasswordChangeActivity extends Activity {

	PasswordChangeActivity mContext;
	private ApplicationError mError = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_change);
		mContext = this;
		
		final RelativeLayout butSend = (RelativeLayout)findViewById(R.id.pwd_change_change_button);
		TextView inputRepeatedNewPassword = (TextView) findViewById(R.id.pwd_change_repeat_new_password_input);
		
		butSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView inputCurrentPassword = (TextView) findViewById(R.id.pwd_change_current_password_input);
				TextView inputNewPassword = (TextView) findViewById(R.id.pwd_change_new_password_input);
				TextView inputRepeatedNewPassword = (TextView) findViewById(R.id.pwd_change_repeat_new_password_input);
				if(!validateInput(inputCurrentPassword.getText().toString(), 
						inputNewPassword.getText().toString(), inputRepeatedNewPassword.getText().toString())){
					switch(mError.getErrorCode()){
					case 404:
		    			Toast.makeText(mContext,R.string.pwd_change_error_incomplete_data,Toast.LENGTH_SHORT).show();
			    		break;
					case 405:
		    			Toast.makeText(mContext,R.string.password_not_secure,Toast.LENGTH_SHORT).show();
			    		break;
	    			case 406:
		    			Toast.makeText(mContext,R.string.pwd_change_error_passwords_not_matching,Toast.LENGTH_SHORT).show();
			    		break;
				}
				}
				else{
					PasswordChangeThread rt = new PasswordChangeThread(mContext);
					rt.execute(inputCurrentPassword.getText().toString(), inputNewPassword.getText().toString());
				}
			}
		});
		
		
		inputRepeatedNewPassword.setOnKeyListener(new OnKeyListener()
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
	
	private boolean validateInput(String currentPassword, String newPassword, String repeatedNewPassword){
		
		if(!(currentPassword.length()>0&&newPassword.length()>0&&repeatedNewPassword.length()>0)){
			mError = new ApplicationError(404,"Warning", "Campos incompletos en el módulo de cambio contraseña");
			return false;
		}
		
		else if(!Security.isValidPassword(newPassword)){
			mError = new ApplicationError(405,"Warning", "Not valid password");
			return false;
		}
		
		else if(!newPassword.equals(repeatedNewPassword)){
			mError = new ApplicationError(406,"Warning", "Password y repetición de password no coinciden en módulo de cambio de contraseña");
			return false;
		};
		
		
		return true;
	}
	
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
			EmergencyPeripheral.handlePeripheralEvent();
	        return true;
		}
		else{
			return super.onKeyUp(keyCode, event);
		}
	}
}
