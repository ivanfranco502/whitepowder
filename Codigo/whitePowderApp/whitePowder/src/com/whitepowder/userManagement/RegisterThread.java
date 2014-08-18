package com.whitepowder.userManagement;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.whitepowder.R;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.SHA1Manager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class RegisterThread extends AsyncTask<String, Void, Void> {

	private final String RegisterURL = "http://whitetavros.com/Sandbox/web/internalApi/user/register";
	private ApplicationError mError = null;
	private RegisterActivity mContext;
	private ProgressDialog progressDialog;
	
	public RegisterThread(RegisterActivity ctxt) {
		
		mContext = ctxt;	
	}
	
	@Override
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.   
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(mContext.getString(R.string.process_dialog_register));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(String... registerInput) {
		
		HttpURLConnection connection=null;
		
		String username = registerInput[0];
		String password = registerInput[1];
//		String repeatedPassword =  registerInput[2];
		String email = registerInput[3];

		String inactive = "0";
		String superAdmin = "0";
		String role = "ROLE_SKIER";				
		
		try {		
			//Generates request
			password = SHA1Manager.SHA1(password);
			String params = "username="+URLEncoder.encode(username, "utf-8")+"&password="+URLEncoder.encode(password, "utf-8")+"&email="+URLEncoder.encode(email, "utf-8")+"&inactive="+inactive+"&superadmin="+superAdmin+"&role="+role;
			URL url = new URL(RegisterURL);
			connection = (HttpURLConnection)url.openConnection();
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
				connection.disconnect();
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
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};
		};

			
		return null;
	}
	
	
	
	private void parseRegisterResponse(String response){
		
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets code from json response
	        int code = jsonObject.getInt("code");

	        if(code!=200){ 
	        	if(code==101){
					mError = new ApplicationError(207,"Warning","Usuario ya existente en módulo de login");
	        	}
	        	else{
	        		if(code==102){
						mError = new ApplicationError(208,"Warning","Email ya registrado en módulo de login");
						return;
	        		}
	        		else{
	        			mError = new ApplicationError(206,"Error","Respuesta inesperada en response en módulo de registro");
	        		};
	        	};
	        };
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(206,"Error","Respuesta inesperada en response en módulo de registro");
		};
		
	}
	
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
    		mContext.setResult(1);
        	mContext.finish();
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
		    		
	    		case 210:
	    			Toast.makeText(mContext,R.string.password_not_secure,Toast.LENGTH_SHORT).show();
		    		break;	
		    		
		    	default: //100, 201, 202, 209
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    		
    		if (progressDialog!=null) {
				progressDialog.dismiss();
    		}
    	};

    }
     
}
