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
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.whitepowder.R;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginThread extends AsyncTask<String, Void, Void> {
	
	private final String LoginURL = "http://whitetavros.com/Sandbox/web/internalApi/user/login";
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
			
        	User user = User.getUserInstance();
			
			try {
				user.setUsername(loginInput[0]);
				user.setPassword(SHA1Manager.SHA1(loginInput[1]));
				String params = "username="+user.getUsername()+"&password="+user.getPassword();
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
					parseLoginResponse(response);
			    }
			    
			    else{
			    	mError = new ApplicationError(100,"Error","Error en la conexi�n con el Servidor");
			    };
			}
		    
			catch (MalformedURLException e) {
				mError = new ApplicationError(101,"Error","MalformedURLException en m�dulo de login");
			} 
			catch (IOException e) {
				mError = new ApplicationError(102,"Error","IOException en m�dulo de login");
			}
			catch (NoSuchAlgorithmException e) {
				mError = new ApplicationError(106,"Error","NoSuchAlgorithmException en m�dulo de login");
			};
			
		}
		
		else{
			mError = new ApplicationError(103,"Warning","Usuario o contrase�a no completado en m�dulo de login");
		};
		
		return null;
	}
	
	@Override
    protected void onPostExecute(Void unused) {	

    	if(mError==null){
    		//TODO display UI
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
	    		
	    		case 103: 
		    		Toast.makeText(mContext,R.string.login_error_incomplete_data,Toast.LENGTH_SHORT).show();
		    		break;
	    		case 104:
		    		Toast.makeText(mContext,R.string.error_unexpected_response,Toast.LENGTH_SHORT).show();
		    		break;
	    		case 105:
	    			Toast.makeText(mContext,R.string.login_error_user_password_incorrect,Toast.LENGTH_SHORT).show();
		    		break;
		    	default:
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    	};
    }	
	
	private void parseLoginResponse(String response){
		
		User user = User.getUserInstance();
    	
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        int code = jsonObject.getInt("code");	        
	        JSONObject payload = jsonObject.getJSONObject("payload");
	        String token = payload.getString("token");
	        String role = payload.getString("role");
	        
	        
	        if(code==200){	        	
	        	user.setToken(token);
	        	user.setRole(role);
	        	user.setLogedAt(new Date(System.currentTimeMillis()));	        	
	        }
	        else {
				user.setUsername(null);
				user.setPassword(null);
				
				//TODO Handle errors
				
	        };
			
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(104,"Error","Respuesta inesperada en response en m�dulo de login");
		}	
	}

}
