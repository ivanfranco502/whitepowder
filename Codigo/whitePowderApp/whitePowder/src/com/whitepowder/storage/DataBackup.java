package com.whitepowder.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.Logout;
import com.whitepowder.utils.ReadFile;

import android.content.Context;
import android.content.SharedPreferences;

public class DataBackup {
	
	Context mContext;
	
	SharedPreferences sharedPrefs;
	
	private String simplifiedSlopeContainerText;
	private String basicInformationText;
	private String drawableSlopeContainerText;
	
	public DataBackup(Context ctx) {
		mContext = ctx;
		
		sharedPrefs = mContext.getSharedPreferences(StorageConstants.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);		
		SharedPreferences.Editor editor = sharedPrefs.edit();
		
		//Gets current values
		
		simplifiedSlopeContainerText = sharedPrefs.getString(StorageConstants.SIMPLIFIED_SLOPES_KEY,null);
		basicInformationText = sharedPrefs.getString(StorageConstants.BASIC_INFORMATION_KEY,null);
		drawableSlopeContainerText = ReadFile.read_file(mContext.getApplicationContext(), StorageConstants.DRAWABLE_SLOPES_FILE);
		
		
		//Puts null values
		
		editor.putString(StorageConstants.SIMPLIFIED_SLOPES_KEY, null);
		editor.putString(StorageConstants.BASIC_INFORMATION_KEY, null);
		
		editor.commit();
		
		try{
			File file = new File(mContext.getFilesDir().getPath().toString() + "/"+StorageConstants.DRAWABLE_SLOPES_FILE);
			if(!file.exists()){
				file.createNewFile();
			};
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();	
		}
		catch(IOException e){
			restore();
		};

	};
	
	public void restore(){
		SharedPreferences.Editor editor = sharedPrefs.edit();
		
		editor.putString(StorageConstants.SIMPLIFIED_SLOPES_KEY, simplifiedSlopeContainerText);
		editor.putString(StorageConstants.BASIC_INFORMATION_KEY, basicInformationText);
		
		editor.commit();
		
		try{
			File file = new File(mContext.getFilesDir().getPath().toString() + "/"+StorageConstants.DRAWABLE_SLOPES_FILE);
			if(!file.exists()){
				file.createNewFile();
			};
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(drawableSlopeContainerText);
			bw.close();	
		}
		catch(IOException e){
			new ApplicationError(800,"Error","Error en la sincronización");
			Logout.logout(mContext, false);
		};
		
	}

}
