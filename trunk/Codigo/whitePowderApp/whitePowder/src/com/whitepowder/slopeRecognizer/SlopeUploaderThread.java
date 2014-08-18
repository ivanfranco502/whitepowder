package com.whitepowder.slopeRecognizer;

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
import com.google.gson.Gson;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class SlopeUploaderThread extends AsyncTask<Void, Void, Void> {

	private final String SlopeUploadURL = "http://whitetavros.com/Sandbox/web/internalApi/slope/recognition";
	ProgressDialog progressDialog = null;
	SlopeRecognizerActivity mContext;
	ApplicationError mError=null;
	
	public SlopeUploaderThread(SlopeRecognizerActivity ctx) {
		mContext = ctx;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setMessage("Transmitiendo pista");
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
	HttpURLConnection connection=null;
		
		try {

			Gson gson = new Gson();
			String request = gson.toJson(mContext.mRecognizedSlope);
			
			URL url = new URL(SlopeUploadURL);
			connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
			
		    connection.setUseCaches (false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
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
			mError = new ApplicationError(601,"Error","MalformedURLException en módulo de reconocimiento de pista");
		}
		catch (IOException e) {
			mError = new ApplicationError(602,"Error","IOException en módulo de reconocimiento de pista");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};

		};
		
		return null;
	};
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		progressDialog.dismiss();
		
		if(mError==null){
			mContext.onTransmitionFinished();
		}
		else{
    		switch(mError.getErrorCode()){
	  		
    		//TODO deshardcode texts
    		
    		case 104:
	    		Toast.makeText(mContext,R.string.error_unexpected_response,Toast.LENGTH_SHORT).show();
	    		break;
	    		
    		case 801:
	    		Toast.makeText(mContext,"Usuario no logueado",Toast.LENGTH_SHORT).show();
	    		Logout.logout(mContext, true);
	    		
	    	default: //100,601,602
	    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
	    		break;  			
    		}
		};
		
	}
	
	void parseResponse(String response){
			    	
		try {
			JSONObject jsonObject;
			jsonObject = new JSONObject(response);
			
	        int code = jsonObject.getInt("code");	               
	        
	        if(code!=200){
				if(code==110){
					mError = new ApplicationError(801,"Error","Usuario no logueado");
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
