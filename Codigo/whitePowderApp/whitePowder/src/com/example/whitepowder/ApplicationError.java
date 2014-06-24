package com.example.whitepowder;

public class ApplicationError{
	
	int errorCode =-1;
	String errorDescription = "Undefined Error";
	
	public ApplicationError(int eCode , String desc) {
		
		errorCode = eCode;
		errorDescription = desc;
		
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
