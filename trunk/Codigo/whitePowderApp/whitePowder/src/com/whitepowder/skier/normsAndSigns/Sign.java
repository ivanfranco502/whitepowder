package com.whitepowder.skier.normsAndSigns;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sign implements Serializable {
	private String name;
	private String image;
	
	
	public Sign(String n, String i){
		name = n;
		image = i;
	}
	
	public String getName(){
		return name;
	}
	public String getImage(){
		return image;
	}
}
