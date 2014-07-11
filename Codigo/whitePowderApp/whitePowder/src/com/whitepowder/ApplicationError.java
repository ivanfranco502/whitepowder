package com.whitepowder;

import android.util.Log;

public class ApplicationError{
	
	int errorCode =-1;
	String errorDescription = "Undefined Error";
	
	public ApplicationError(int eCode ,String type, String desc) {
		
		errorCode = eCode;
		errorDescription = desc;		
		Log.i(type,"EC: "+Integer.toString(eCode)+" ,"+desc);
		
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
