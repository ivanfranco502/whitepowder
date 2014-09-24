package com.whitepowder.skier.normsAndSigns;

public class Norm {
	private String name;
	private String description;
	private String image;
	
	public Norm(String n, String d, String i){
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
