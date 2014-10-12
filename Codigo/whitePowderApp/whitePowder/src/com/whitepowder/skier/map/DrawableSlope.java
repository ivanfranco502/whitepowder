package com.whitepowder.skier.map;

import java.util.ArrayList;

import com.whitepowder.skier.Coordinate;

public class DrawableSlope {
	String slope_description =null;
	int slope_length=0;
	int slope_id=0;
	String slope_difficulty_color=null;
	String slope_difficulty_description=null;
	ArrayList<Coordinate> slope_coordinates;
	
	public String getSlope_name() {
		return slope_description;
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
	public ArrayList<Coordinate> getSlope_coordinates() {
		return slope_coordinates;
	}
	public void setSlope_coordinates(ArrayList<Coordinate> slope_coordinates) {
		this.slope_coordinates = slope_coordinates;
	}
	public int getSlope_length() {
		return slope_length;
	}
	public void setSlope_length(int slope_length) {
		this.slope_length = slope_length;
	}
	public String getSlope_difficulty_description() {
		return slope_difficulty_description;
	}
	public void setSlope_difficulty_description(String slope_difficulty_description) {
		this.slope_difficulty_description = slope_difficulty_description;
	}
	public String getSlope_description() {
		return slope_description;
	}
}