package com.example.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginThread extends AsyncTask<String, Void, Void> {
	
	private final String LoginURL = "http://whitetavros.com/Symfony/web/internalApi/prueba";
	private ApplicationError mError = null;
	private Context mContext;
	
	public LoginThread(Context ctxt) {
		
		mContext = ctxt;
		
	}
	
	@Override
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.       
	}
	
	@Override
	protected Void doInBackground(String... loginInput) {
		
		if((loginInput[0].length()> 0)&& (loginInput[1].length()>0)){
		
			try {
				String params = "username="+loginInput[0]+"&password="+loginInput[1];
				URL url = new URL(LoginURL);
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
					//TODO parse response
			    }
			    
			    else{
			    	Log.i("Error","EC:100 Error reaching server");
			    	mError = new ApplicationError(100,"Error reaching Server");
			    };
			}
		    
			catch (MalformedURLException e) {
				Log.i("Error","EC:101 Malformed URL in login module");
				mError = new ApplicationError(101,"Malformed URL in login module");
			} 
			catch (IOException e) {
				Log.i("Error","EC 102 IO exception in login module");
				mError = new ApplicationError(102,"IO exception in login module");
			}
			
		}
		
		else{
			mError = new ApplicationError(103,"Username or password have not been completed");
		};
		
		return null;
	}
	
	@Override
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
    		//TODO display UI
    	}
    	
    	else{
    		switch(mError.getErrorCode()){
	    		
	    		case 103: 
		    		Toast.makeText(mContext,R.string.login_error_incomplete_data,Toast.LENGTH_SHORT).show();
		    		break;
		    	default:
		    		Toast.makeText(mContext,R.string.login_error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;
    			
    		}
    	}
   }

}
