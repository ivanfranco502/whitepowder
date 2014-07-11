package com.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.whitepowder.R;
import android.os.AsyncTask;
import android.widget.Toast;

public class RegisterThread extends AsyncTask<String, Void, Void> {

	private final String RegisterURL = "http://whitetavros.com/Sandbox/web/internalApi/user/register";
	private ApplicationError mError = null;
	private RegisterActivity mContext;
	
	public RegisterThread(RegisterActivity ctxt) {
		
		mContext = ctxt;	
	}
	
	@Override
	protected Void doInBackground(String... registerInput) {
		
		String username = registerInput[0];
		String password = registerInput[1];
		String repeatedPassword =  registerInput[2];
		String email = registerInput[3];

		String inactive = "0";
		String superAdmin = "0";
		String role = "ROLE_SKIER";				
		
		if(validateInput(username,password,repeatedPassword,email)){		
			try {		
				//Generates request
				password = SHA1Manager.SHA1(password);
				String params = "username="+username+"&password="+password+"&email="+email+"&inactive="+inactive+"&superadmin="+superAdmin+"&role="+role;
				URL url = new URL(RegisterURL);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
			    
			    connection.setUseCaches (false);
			    connection.setDoInput(true);
			    connection.setDoOutput(true);
			    
			    //Send request
			    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			    wr.writeBytes(params);
			    wr.flush ();
			    wr.close ();
			        
			    if(connection.getResponseCode()==200){
				    
			    	//Get Response
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
					String response = reader.readLine();
					parseRegisterResponse(response);
			    }
			    
			    else{
			    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
			    };
			}
		    
			catch (MalformedURLException e) {
				mError = new ApplicationError(201,"Error","MalformedURLException en módulo de registro");
			}
			catch (IOException e) {
				mError = new ApplicationError(202,"Error","IOException en módulo de registro");
			}
			catch (NoSuchAlgorithmException e) {
				mError = new ApplicationError(209,"Error","NoSuchAlgorithmException en módulo de registro");
			};

		}
			
		return null;
	}
	
	private boolean validateInput(String username, String password, String repeatedPassword, String email) {
		
		if(!(username.length()>0&&password.length()>0&&repeatedPassword.length()>0&&email.length()>0)){
			mError = new ApplicationError(203,"Warning", "Campos incompletos en el módulo de registro");
			return false;
		};
			
		if(!password.equals(repeatedPassword)){
			mError = new ApplicationError(204,"Warning", "Password y repetición de password no coinciden en módulo de registro");
			return false;
		};
		
		if(!email.contains("@") || !email.contains(".")){
			mError = new ApplicationError(205,"Warning", "Dirección de email invalida en módulo de registro");
			return false;
		};
		
		return true;
	
	}
	
	private void parseRegisterResponse(String response){
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        int code = jsonObject.getInt("code");
	        
	        if(code==200){
	        	mContext.setResult(0);
	        	mContext.finish();
	        }
	        else{
	        	if(code==101){
					mError = new ApplicationError(207,"Warning","Usuario ya existente en módulo de login");
	        	}
	        	if(code==102){
					mError = new ApplicationError(208,"Warning","Email ya registrado en módulo de login");
	        	}
	        };
			
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(204,"Error","Respuesta inesperada en response en módulo de registro");
		}
		
	}
	
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
    		//TODO display UI
    	}
    	//Error handling
    	else{
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
		    		
	    		case 206:
	    			Toast.makeText(mContext,R.string.error_unexpected_response,Toast.LENGTH_SHORT).show();
		    		break;
		    		
	    		case 207:
	    			Toast.makeText(mContext,R.string.register_error_existing_username,Toast.LENGTH_SHORT).show();
		    		break;
		    		
	    		case 208:
	    			Toast.makeText(mContext,R.string.register_error_existing_email,Toast.LENGTH_SHORT).show();
		    		break;	
		    	default:
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    	};

    }
    
    
    
    
}
