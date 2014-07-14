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
		
		HttpURLConnection connection = null;
		
		if(!isValidInput(loginInput[0], loginInput[1])){
			return null;
		};
			
    	User user = User.getUserInstance();
		
		try {
			user.setUsername(loginInput[0]);
			user.setPassword(SHA1Manager.SHA1(loginInput[1]));
			String params = "username="+user.getUsername()+"&password="+user.getPassword();
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
				connection.disconnect();
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
    		//TODO display UI
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
	    		
    			case 90:
	    			Toast.makeText(mContext,R.string.error_invalid_characters,Toast.LENGTH_SHORT).show();
		    		break;
    		
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
    	};
    }
	
	private boolean isValidInput(String user, String pass){
		
		if(!(user.length()>0&&pass.length()>0)){
			mError = new ApplicationError(103,"Warning","Usuario o contraseña no completado en módulo de login");
			return false;
		};
		
		
		if(Security.hasInvalidCharacters(user,pass)){
			mError = new ApplicationError(90,"Warning","Los datos ingresados contienen caracteres inválidos");
			return false;
		};
		
		
		
		return true;
	}
	
	private void parseLoginResponse(String response){
		
		User user = User.getUserInstance();
    	
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets data from json
	        int code = jsonObject.getInt("code");	        
	        JSONObject payload = jsonObject.getJSONObject("payload");
	        String token = payload.getString("_token");
	        String role = payload.getString("role");
	        
	        
	        if(code==200){	        	
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
