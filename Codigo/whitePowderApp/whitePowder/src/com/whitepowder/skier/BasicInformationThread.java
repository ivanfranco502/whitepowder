package com.whitepowder.skier;

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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.ApplicationError;
import com.whitepowder.Logout;
import com.whitepowder.SHA1Manager;
import com.whitepowder.user.management.PasswordChangeActivity;
import com.whitepowder.user.management.User;

public class BasicInformationThread extends AsyncTask<String, Void, Void> {

	private final String BasicInformationURL = "http://whitetavros.com/Sandbox/web/internalApi/user/info/basic";
	private ApplicationError mError = null;
	private SkierActivity mContext;
	private ProgressDialog progressDialog;
	private BasicInformationResponse basicInformationResponse;
	private BasicInformation basicInformation;
	
	public BasicInformationThread(SkierActivity context) {
		mContext = context;	
	}
	
	@Override
	protected void onPreExecute(){
		/*progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage(mContext.getString(R.string.process_dialog_pwd_change));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();*/
	}
	
	
	@Override
	protected Void doInBackground(String... registerInput) {
		HttpURLConnection connection = null;
			
		try {		
			String token = User.getUserInstance().getToken();
			
			//Generates request
						
			JSONObject request= new JSONObject();
			request.put("_token", token);
			
			URL url = new URL(BasicInformationURL);
			connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    
		    connection.setUseCaches(false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(request.toString());
		    wr.flush();
		    wr.close();
		        
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
			mError = new ApplicationError(501,"Error","MalformedURLException en descarga de información básica");
		}
		catch (IOException e) {
			mError = new ApplicationError(502,"Error","IOException en descarga de información básica");
		}
		catch (JSONException e){
			mError = new ApplicationError(503, "Error", "JSONException en descarga de información básica");
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
			//TODO llenar UI con la info del centro
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
    			case 504:
    				//TODO mandar a la UI que no hay info disponible
    				break;
	    		case 508: 
	    			Logout.logout(mContext, true);
		    		break;
		    	default: //100, 401, 402, 403, 410
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    		
    		if (progressDialog!=null) {
				progressDialog.dismiss();
    		}
    	};

    }

		
	private void parseResponse(String response){
	
		Gson gson = new Gson();
		basicInformationResponse = gson.fromJson(response, BasicInformationResponse.class);
		if(basicInformationResponse.getCode() != 200){
        	switch(basicInformationResponse.getCode()){
        		case 110:
        			mError = new ApplicationError(508,"Error","Token inválido en descarga de información básica");
        			break;
        		case 116:
        			mError = new ApplicationError(504, "Error", "No hay información básica en la base de datos");
        			break;
        	}
        }
        else{
        	basicInformation = basicInformationResponse.getBasicInformation();	
        }
	}

}

