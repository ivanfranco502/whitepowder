package com.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;

import android.os.AsyncTask;
import android.widget.Toast;

public class PasswordResetThread extends AsyncTask<String, Void, Void>  {

	private final String PasswordResetChangeURL = "http://whitetavros.com/Sandbox/web/internalApi/user/reset";
	private ApplicationError mError = null;
	private PasswordResetActivity mContext;
	
	public PasswordResetThread(PasswordResetActivity ctxt) {
		
		mContext = ctxt;	
	}
	
	@Override
	protected Void doInBackground(String... resetInput) {
		String email = resetInput[0];
		HttpURLConnection connection=null;
		if(!isValidInput(email)){
			return null;
		};
		
		try {		
			//Generates request
			String params = "email="+ URLEncoder.encode(email, "utf-8");
			URL url = new URL(PasswordResetChangeURL);
			connection = (HttpURLConnection)url.openConnection();

		    connection.setRequestMethod("POST");
			
		    connection.setUseCaches (false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(params);
		    wr.flush ();
		    wr.close ();
		        
		    if(connection.getResponseCode()==200){
			    
		    	//Get Response
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
				String response = reader.readLine();
				parseResetResponse(response);
		    }
		    
		    else{
		    	mError = new ApplicationError(100,"Error","Error en la conexi�n con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(301,"Error","MalformedURLException en m�dulo de reseteo de contrase�a");
		}
		catch (IOException e) {
			mError = new ApplicationError(302,"Error","IOException en m�dulo de reseteo de contrase�a");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};

		};
		
		
		return null;
	}
	
	private boolean isValidInput(String email){
		
		if(!Security.isValidEmail(email)){
			mError = new ApplicationError(303,"Warning", "Direcci�n de email invalida en m�dulo de registro");
			return false;
		};		
		
		return true;
	}
	
	private void parseResetResponse(String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets code from json response
	        int code = jsonObject.getInt("code");

	        if(code!=200){
	        	//TODO Check errors
	        };		
		} 
		
		catch (JSONException e) {
			//TODO Raiserror
		};
	}
	
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
        	mContext.setResult(0);
        	mContext.finish();
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
    				    		
				case 303:
	    			Toast.makeText(mContext,R.string.reset_invalid_email,Toast.LENGTH_SHORT).show();
		    		break;
		    		
		    	default: //100,301,302
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    	};
    }

}
