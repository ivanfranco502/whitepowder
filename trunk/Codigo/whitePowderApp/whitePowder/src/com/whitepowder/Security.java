package com.whitepowder;

public class Security {

	public static boolean hasInvalidCharacters(String... input){
		//TODO fix
		
		for(int i=0;i<input.length;i++){
			if(input[i].contains("\"")||(input[i].contains("&")||(input[i].contains("=")||input[i].contains("{")||input[i].contains("}")||input[i].contains("[")||input[i].contains("]")))){
				return true;
			};
		}
		
		return false;
	}
	
	public static boolean isValidPassword(String pwd){
		
		if(pwd.length()<6){
			return false;
		};
		
		return true;
	}
	
	public static boolean isValidEmail(String email){
		if(!email.contains("@") || !email.contains(".")|| email.length()<3){
			return false;
		};
		return true;
	}
	
}


