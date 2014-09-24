package com.whitepowder.skier.normsAndSigns;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sign implements Serializable {
	private String name;
	private String description;
	private String image;
	
	
	public Sign(String n, String d, String i){
		name = n;
		description = d;
		image = i;
	}
	
	public String getName(){
		return name;
	}
	public String getImage(){
		return image;
	}
	public String getDescription() {
		return description;
	}
}
