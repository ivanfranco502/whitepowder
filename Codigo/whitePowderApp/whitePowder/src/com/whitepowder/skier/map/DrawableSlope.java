package com.whitepowder.skier.map;

import java.util.ArrayList;

public class DrawableSlope {
	String slope_name =null;
	int slope_id=0;
	String slope_difficulty_color=null;
	ArrayList<SimpleCoordinate> slope_coordinates;
	
	public String getSlope_name() {
		return slope_name;
	}
	public void setSlope_name(String slope_name) {
		this.slope_name = slope_name;
	}
	public int getSlope_id() {
		return slope_id;
	}
	public void setSlope_id(int slope_id) {
		this.slope_id = slope_id;
	}
	public String getSlope_difficulty_color() {
		return slope_difficulty_color;
	}
	public void setSlope_difficulty_color(String slope_difficulty_color) {
		this.slope_difficulty_color = slope_difficulty_color;
	}
	public ArrayList<SimpleCoordinate> getSlope_coordinates() {
		return slope_coordinates;
	}
	public void setSlope_coordinates(ArrayList<SimpleCoordinate> slope_coordinates) {
		this.slope_coordinates = slope_coordinates;
	}
}

class SimpleCoordinate{
	double x;
	double y;
}