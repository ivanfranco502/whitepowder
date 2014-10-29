package com.whitepowder.skier.emergency;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.skier.Coordinate;
import com.whitepowder.skier.MyPosition;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;

public class EmergencyThread extends AsyncTask<Void, Void, Void> {
	
	
	private ProgressDialog progressDialog;
	private Context mContext;
	private SkierActivity mSkierActivity;
	private final String EmergencyURL = BaseTavrosURI.getBaseURI()+"alert/send";
	private final Gson gson = new Gson();
	private ApplicationError mError = null;
	private LocationManager mLocationManager;
	private boolean sendTried = false;
	
	public EmergencyThread(SkierActivity skierActivity, Context ctxt) {		
		mContext = ctxt;
		mSkierActivity = skierActivity;
	}
	
	@Override
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.   
		if (null == (mLocationManager = (LocationManager) mSkierActivity.getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "Su dispositivo no posee GPS", Toast.LENGTH_SHORT ).show();
			this.cancel(true);
		};		
		
		if (null == (mLocationManager = (LocationManager) mSkierActivity.getSystemService(Context.LOCATION_SERVICE))){
			Toast.makeText(mContext, "No es posible obtener su posición.", Toast.LENGTH_SHORT ).show();
			this.cancel(true);
		};
		
		progressDialog = new ProgressDialog(mSkierActivity);
		progressDialog.setMessage(mContext.getString(R.string.process_dialog_emergency));
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... unused){
		// Acquire reference to the LocationManager
		
		Location mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(mCurrentLocation != null){
			MyPosition pos = new MyPosition(User.getUserInstance().getToken(), new Coordinate(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
			String emergency = gson.toJson(pos);					
			sendEmergency(emergency);
			sendTried =  true;
		}
		
		return null;
	}
	
	
	private void sendEmergency(String emergency){
		HttpURLConnection connection = null;
		boolean success = false;
		
		try {
			URL url = new URL(EmergencyURL);
			connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    
		    connection.setUseCaches (false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    
		    while(!success){
		    
			    //Send request
			    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			    wr.writeBytes(emergency);
			    wr.flush ();
			    wr.close ();
			    
			    if(connection.getResponseCode()==200){
			    	//Get Response
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
					String response = reader.readLine();
					try {
						JSONObject jsonObject = new JSONObject(response);
						
						//Gets data from json
				        int code = jsonObject.getInt("code");	               
				        
				        if(code==200){
				        	//Emergencia recibida exitosamente
					        success = true;	        	
				        }
				        else {							
							if(code==110){
								mError = new ApplicationError(1200,"Warning","Token inválido en alerta de emergencia");
							}
							else{
								mError = new ApplicationError(1201,"Error","Respuesta inesperada en response en alerta de emergencia");
							};			
				        };
					}
					catch(JSONException e){};
			    }
			    else{
			    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
			    };
		    }
		}
		catch( MalformedURLException e){
			mError = new ApplicationError(1202,"Error","MalformedURLException en alerta de emergencia");
		}
		catch(IOException e){
			mError = new ApplicationError(1203,"Error","IOException en alerta de emergencia");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};
		}
	}
	
	
	
	@Override
    protected void onPostExecute(Void unused) {	
	
		progressDialog.dismiss();
		
    	if(mError==null){
    		if(sendTried){
    		Toast.makeText(mContext, mSkierActivity.getResources().getString(R.string.emergency_success_toast), Toast.LENGTH_SHORT).show();
    		}
    		else{
        		Toast.makeText(mContext, "No es posible enviar la alerta de emergencia en este momento.", Toast.LENGTH_SHORT).show();
    		}
    	}
    	//Error handling
    	else{
    		switch(mError.getErrorCode()){
	    		  		
	    		case 1200: 
	    			Logout.logout(mContext, false);
		    		break;
	    		case 1201:
		    		Toast.makeText(mContext,R.string.error_unexpected_response,Toast.LENGTH_SHORT).show();
		    		break;
		    	default:
		    		Toast.makeText(mContext,R.string.error_server_unreachable,Toast.LENGTH_SHORT).show();
		    		break;  			
    		}
    				
    	};
    }	
}