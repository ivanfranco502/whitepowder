package com.whitepowder.skier.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.BaseTavrosURI;

public class SlopeDownloaderThread extends Thread {

    private DrawableSlopeContainer mDrawableSlopeContainer;
	private final String SlopeDownloadURL = BaseTavrosURI.getBaseURI()+"slope/allPath";
	private Context mContext;
	
	public SlopeDownloaderThread(Context ctx) {
		mContext = ctx;
	}
	
	@Override
    public void run() {
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
				
				Gson gson = new Gson();
				
				mDrawableSlopeContainer = gson.fromJson(response,DrawableSlopeContainer.class);
				
				if(mDrawableSlopeContainer!=null){						
						File file = new File(mContext.getFilesDir().getPath().toString() + "/"+StorageConstants.DRAWABLE_SLOPES_FILE);
						if(!file.exists()){
							file.createNewFile();
						};
						
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(response);
						bw.close();			
				};				
		    }
		    
		    else{
		    	new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			new ApplicationError(801,"Error","MalformedURLException en módulo de esquiador");
		}
		catch (IOException e) {
			new ApplicationError(802,"Error","IOException en módulo de esquiador");
		}
		catch (JSONException e) {
			new ApplicationError(803,"Error","JSonException en módulo de esquiador");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};

		};
		
	};
	
}
