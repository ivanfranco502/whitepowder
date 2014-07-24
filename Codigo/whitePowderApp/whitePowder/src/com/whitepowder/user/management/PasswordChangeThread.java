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
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whitepowder.R;
import com.whitepowder.ApplicationError;
import com.whitepowder.SHA1Manager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class PasswordChangeThread extends AsyncTask<String, Void, Void> {

	private final String PasswordChangeURL = "";
	private ApplicationError mError = null;
	private PasswordChangeActivity mContext;
	private ProgressDialog progressDialog;
	
	public PasswordChangeThread(PasswordChangeActivity ctxt) {
		
		mContext = ctxt;	
	}
	
	@Override
	protected void onPreExecute(){
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(mContext.getString(R.string.process_dialog_pwd_change));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	
	@Override
	protected Void doInBackground(String... registerInput) {
		HttpURLConnection connection = null;
		String token = User.getUserInstance().getToken();
		String currentPassword = registerInput[0];
		String newPassword = registerInput[1];
			
		try {		
			//Generates request
			currentPassword = SHA1Manager.SHA1(currentPassword);
			newPassword = SHA1Manager.SHA1(newPassword);
			//String params = "token="+URLEncoder.encode(token, "utf-8")+"&current_password="+URLEncoder.encode(currentPassword, "utf-8")+"&new_password="+URLEncoder.encode(newPassword, "utf-8");
			
			JSONObject request= new JSONObject();
			request.put("_token", token);
			request.put("currentPassword", currentPassword);
			request.put("newPassword", newPassword);
			
			
			URL url = new URL(PasswordChangeURL);
			connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    
		    connection.setUseCaches (false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
		    wr.writeBytes(request.toString());
		    wr.flush ();
		    wr.close ();
		        
		    if(connection.getResponseCode()==200){
			    
		    	//Get Response
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
				String response = reader.readLine();
				parseResponse(response);
		    }
		    
		    else{
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(401,"Error","MalformedURLException en módulo de cambio de contraseña");
		}
		catch (IOException e) {
			mError = new ApplicationError(402,"Error","IOException en módulo de cambio de contraseña");
		}
		catch (NoSuchAlgorithmException e) {
			mError = new ApplicationError(403,"Error","NoSuchAlgorithmException en módulo de cambio de contraseña");
		}
		catch (JSONException e){
			mError = new ApplicationError(410, "Error", "JSONException en módulo de cambio de contraseña");
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
    		mContext.finish();
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
    		
	    		case 408: 
		    		Toast.makeText(mContext,R.string.pwd_change_invalid_token,Toast.LENGTH_SHORT).show();
		    		break;
	    		case 409: 
		    		Toast.makeText(mContext,R.string.pwd_change_invalid_current_password,Toast.LENGTH_SHORT).show();
		    		break;
		    	default: //100, 401, 402, 403
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    		
    		if (progressDialog!=null) {
				progressDialog.dismiss();
    		}
    	};

    }

	
	
	

	
	private void parseResponse(String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets code from json response
	        int code = jsonObject.getInt("code");

	        if(code!=200){
	        	/*switch(code){
	        		case xxx:
	        			mError = new ApplicationError(408,"Error","Token inválido en módulo de cambio de contraseña");
	        		case yyy:
	        			mError = new ApplicationError(409,"Error","Contraseña actual inválida en módulo de cambio de contraseña");
	        	}*/
	        };		
		} 
		
		catch (JSONException e) {
			mError = new ApplicationError(407,"Error","Respuesta inesperada en response en módulo de cambio de contraseña");
		};
	}

}
