package com.whitepowder.user.management;

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
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;
import com.whitepowder.SHA1Manager;

public class LoginThread extends AsyncTask<String, Void, Void> {
	
	private final String LoginURL = "http://whitetavros.com/Sandbox/web/internalApi/user/login";
	private ApplicationError mError = null;
	private Context mContext;
	private LoginActivity mLoginActivity;
	private ProgressDialog progressDialog;
		
	public LoginThread(LoginActivity loginActivity, Context ctxt) {		
		mContext = ctxt;
		mLoginActivity = loginActivity;
	}
	
	@Override
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.   
		progressDialog = new ProgressDialog(mLoginActivity);
		progressDialog.setMessage(mContext.getString(R.string.process_dialog_login));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(String... loginInput) {
		
		HttpURLConnection connection = null;
		
		User user = User.getUserInstance();
		
		try {
			user.setUsername(loginInput[0]);
			user.setPassword(SHA1Manager.SHA1(loginInput[1]));
			String params = "username="+URLEncoder.encode(user.getUsername(),"utf-8")+"&password="+URLEncoder.encode(user.getPassword(),"utf-8");
			URL url = new URL(LoginURL);
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
				parseLoginResponse(response);
		    }
		    
		    else{
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(101,"Error","MalformedURLException en módulo de login");
		} 
		catch (IOException e) {
			mError = new ApplicationError(102,"Error","IOException en módulo de login");
		}
		catch (NoSuchAlgorithmException e) {
			mError = new ApplicationError(106,"Error","NoSuchAlgorithmException en módulo de login");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};				
		};
		
		return null;
	}
	
	@Override
    protected void onPostExecute(Void unused) {	
	
    	if(mError==null){
    		mLoginActivity.loginAccordingToRole(true);
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
		    	default: //100,101,102,106
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    		
    		progressDialog.dismiss();
    		
    	};
    }
	
	
	private void parseLoginResponse(String response){
		
		User user = User.getUserInstance();
    	
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        int code = jsonObject.getInt("code");	               
	        
	        if(code==200){
	        	
		        JSONObject payload = jsonObject.getJSONObject("payload");
		        String token = payload.getString("_token");
		        String role = payload.getString("role");
	        	
	        	user.setToken(token);
	        	user.setRole(role);
	        	user.setLogedAt(new Date(System.currentTimeMillis()));	        	
	        }
	        else {
				user.setUsername(null);
				user.setPassword(null);
				
				if((code==104)||(code==105)){
					mError = new ApplicationError(105,"Warning","Usuario o contraseña incorrecto en módulo de login");
				}
				else{
					mError = new ApplicationError(104,"Error","Respuesta inesperada en response en módulo de login");
				};			
	        };
			
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(104,"Error","Respuesta inesperada en response en módulo de login");
		};	
		
	}

}
