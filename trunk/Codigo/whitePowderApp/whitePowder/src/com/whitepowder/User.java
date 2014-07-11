package com.whitepowder;

import java.util.Date;

//Singleton pattern

public class User {

	private static User instance = null;
	
	String username;
	String password;
	String token;
	Date logedAt;
	String role;

	private User() {};
	
	public static User getUserInstance(){
		if(instance==null){
			instance = new User();
		};
		return instance;
	};
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getLogedAt() {
		return logedAt;
	}
	public void setLogedAt(Date logedAt) {
		this.logedAt = logedAt;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
