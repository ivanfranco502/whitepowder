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

import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;
import com.whitepowder.Security;

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
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(301,"Error","MalformedURLException en módulo de reseteo de contraseña");
		}
		catch (IOException e) {
			mError = new ApplicationError(302,"Error","IOException en módulo de reseteo de contraseña");
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
			mError = new ApplicationError(303,"Warning", "Dirección de email invalida en módulo de registro");
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
	        	switch (code) {
	        	case 107:
					mError = new ApplicationError(107,"Error","El email no se encuentra registrado.");
					break;
				case 108:
					mError = new ApplicationError(108,"Error","No se pudo cambiar la contraseña. Vuela a intentar.");
					break;
				case 109:
					mError = new ApplicationError(109,"Error","No se pudo enviar el mail con la nueva contraseña. Vuelva a intentar.");
					break;
				}
	        };		
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(104,"Error","Respuesta inesperada en response en módulo de login");
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
	    		case 104:
		    		Toast.makeText(mContext,R.string.error_unexpected_response,Toast.LENGTH_SHORT).show();
		    		break;	
	    		case 107:
					Toast.makeText(mContext,R.string.pwd_reset_error_107,Toast.LENGTH_SHORT).show();
		    		break;
	    		case 108:
					Toast.makeText(mContext,R.string.pwd_reset_error_108,Toast.LENGTH_SHORT).show();
		    		break;
	    		case 109:
					Toast.makeText(mContext,R.string.pwd_reset_error_109,Toast.LENGTH_SHORT).show();
		    		break;
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
