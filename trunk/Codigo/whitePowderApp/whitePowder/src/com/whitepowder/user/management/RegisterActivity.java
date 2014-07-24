package com.whitepowder.user.management;

import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;
import com.whitepowder.Security;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	RegisterActivity mContext;	
	private ApplicationError mError = null;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
				if(!isValidInput(inputUsername.getText().toString(), inputPassword.getText().toString(), 
									inputRepeatedPassword.getText().toString(), inputEmail.getText().toString())){
					switch(mError.getErrorCode()){
		    			case 203: 
				    		Toast.makeText(mContext,R.string.register_error_incomplete_data,Toast.LENGTH_SHORT).show();
				    		break;
			    		case 204:
			    			Toast.makeText(mContext,R.string.register_error_passwords_not_matching,Toast.LENGTH_SHORT).show();
				    		break;
			    		case 205:
			    			Toast.makeText(mContext,R.string.register_error_email_not_valid,Toast.LENGTH_SHORT).show();
				    		break;
			    		case 210:
			    			Toast.makeText(mContext,R.string.password_not_secure,Toast.LENGTH_SHORT).show();
				    		break;
					}
				}
				else{
					RegisterThread rt = new RegisterThread(mContext);
					rt.execute(inputUsername.getText().toString(),inputPassword.getText().toString(),inputRepeatedPassword.getText().toString(),inputEmail.getText().toString());	
				}
			}
		});

	}
	
	private boolean isValidInput(String username, String password, String repeatedPassword, String email) {

		if(!(username.length()>0&&password.length()>0&&repeatedPassword.length()>0&&email.length()>0)){
			mError = new ApplicationError(203,"Warning", "Campos incompletos en el módulo de registro");
			return false;
		}
		else if(!Security.isValidPassword(password)){
			mError = new ApplicationError(210,"Warning", "Not valid password");
			return false;
		}
		else if(!password.equals(repeatedPassword)){
			mError = new ApplicationError(204,"Warning", "Password y repetición de password no coinciden en módulo de registro");
			return false;
		}
		else if(!Security.isValidEmail(email)){
			mError = new ApplicationError(205,"Warning", "Dirección de email invalida en módulo de registro");
			return false;
		};
		
		return true;
	
	}
}
