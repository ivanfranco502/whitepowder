package com.whitepowder.slope.recognizer;

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
import com.whitepowder.ApplicationError;
import com.whitepowder.user.management.User;
import com.example.whitepowder.R;
import com.google.gson.Gson;
import android.os.AsyncTask;
import android.widget.Toast;

public class SlopeDownloaderThread extends AsyncTask<Void, Void, Void> {

	private final String SlopeDownloadURL = "http://whitetavros.com/Sandbox/web/internalApi/slope/allNames";
	private ApplicationError mError = null;
	private SlopeRecognizerActivity mContext;
	private SlopeContainer mSlopes=null;
	
	public SlopeDownloaderThread(SlopeRecognizerActivity ctxt) {
		
		mContext = ctxt;	
	};

	@Override
	protected Void doInBackground(Void... params) {
		
		HttpURLConnection connection=null;
		
		try {		
			JSONObject request= new JSONObject();
			request.put("_token", User.getUserInstance().getToken());
			
			URL url = new URL(SlopeDownloadURL);
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
		catch (JSONException e) {
			mError = new ApplicationError(603,"Error","JSonException en módulo de reconocimiento de pista");
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

		if(mSlopes!=null){
			for(SimplifiedSlope ss : mSlopes.payload){
				mContext.getAdapter().add(ss);			
			};
		};
		
		if(mError!=null){
			switch(mError.getErrorCode()){
				default: //100,601,602 y 603
					Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
					break;
			};
		};
		
	};

	private void parseResponse(String response){
		Gson gson = new Gson();
		mSlopes = gson.fromJson(response,SlopeContainer.class);
	};

}