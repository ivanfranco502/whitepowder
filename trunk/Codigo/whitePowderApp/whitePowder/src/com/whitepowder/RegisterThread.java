package com.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.whitepowder.R;
import android.os.AsyncTask;
import android.util.Log;
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
		
		if(validateInput(username,password,repeatedPassword,email)==0){		
			try {		
				//Generates request
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
			    	Log.i("Error","EC:100 Error reaching server");
			    	mError = new ApplicationError(100,"Error reaching Server");
			    };
			}
		    
			catch (MalformedURLException e) {
				Log.i("Error","EC:201 Malformed URL in register module");
				mError = new ApplicationError(201,"Malformed URL in register module");
			}
			catch (IOException e) {
				Log.i("Error","EC: 202 IO exception in register module");
				mError = new ApplicationError(202,"IO exception in register module");
			};
		}
			
		return null;
	}
	
	private int validateInput(String username, String password, String repeatedPassword, String email) {
		
		if(!(username.length()>0&&password.length()>0&&repeatedPassword.length()>0&&email.length()>0)){
			mError = new ApplicationError(203, "EC 203 Incomplete data in register module");
			Log.i("Warning", mError.getErrorDescription());
			return 1;
		}
		else {		
			if(!password.equals(repeatedPassword)){
				mError = new ApplicationError(204, "EC 204 Password and repeated password must match");
				Log.i("Warning", mError.getErrorDescription());
				return 2;
			}
			else{
				if(!email.contains("@") || !email.contains(".")){
					mError = new ApplicationError(205, "EC 205 Invalid email address");
					Log.i("Warning", mError.getErrorDescription());
					return 3;
				}
				else{
					return 0;
				}
			}
		}		
	}
	
	private void parseRegisterResponse(String response){
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        String status  = jsonObject.getString("status");
	        int code = jsonObject.getInt("code");
	        
	        if(status.equals("SUCCESS")){
	        	mContext.setResult(0);
	        	mContext.finish();
	        }
	        else {
	        	if(code==101){
					Log.i("Error","EC: 207 Username is already registered");
					mError = new ApplicationError(207,"Username or password is already registered");
	        	}
	        	else{
		        	if(code==102){
						Log.i("Error","EC: 208 Email is already registered");
						mError = new ApplicationError(207,"Username or password is already registered");
		        	}
		        	else{
						Log.i("Error","EC: 206 Unexpected response in register module");
						mError = new ApplicationError(206,"Unexpected response in register module");
		        	};
	        	};
	        };
			
		} 
		
		catch (JSONException e) {
			Log.i("Error","EC: 206 Unexpected response in register module");
			mError = new ApplicationError(204,"Unexpected response in register module");
		}
		
	}
	
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
    		//TODO display UI
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
	    	
    			//100,201 y 202 van al default
    		
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
		    	//TODO descardcodear	
	    		case 207:
	    			Toast.makeText(mContext,"Usuario ya registrados anteriormente",Toast.LENGTH_SHORT).show();
		    		break;
		    		
		    	//TODO descardcodear
	    		case 208:
	    			Toast.makeText(mContext,"Email ya registrados anteriormente",Toast.LENGTH_SHORT).show();
		    		break;	
		    	default:
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    	};

    }
}
