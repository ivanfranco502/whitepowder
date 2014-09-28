package com.whitepowder.skier.basicInformation;

import java.util.ArrayList;

public class BasicInformation {
	private String gein_center_name;
	private String gein_amenities;
	private int gein_maximum_height;
	private int gein_minimum_height;
	private String gein_season_since;
	private String gein_season_till;
	private String gein_location;
	private String gein_details;
	private ArrayList<Day> _schedules;
	private double gein_x;
	private double gein_y;
	
	public String getCenterName(){
		return gein_center_name;
	}
	public String getAmenities(){
		return gein_amenities;
	}
	public String getMaximumHeight(){
		return "Altura m�xima: " + gein_maximum_height + " m.s.n.m.";
	}
	public String getMinimumHeight(){
		return "Altura m�nima: " + gein_minimum_height + " m.s.n.m.";
	}
	public String getSeasonSince(){
		return gein_season_since;
	}
	public String getSeasonTill(){
		return gein_season_till;
	}
	public String getLocation(){
		return gein_location;
	}
	public String getDetails(){
		return gein_details;
	}
	public ArrayList<Day> getDays(){
		return _schedules;
	}
	public double getX(){
		return gein_x;
	}
	public double getY(){
		return gein_y;
	}
	
	public String getSeason(){
		return "Temporada: desde " + getSeasonSince() + " hasta " + getSeasonTill() + ".";
	}
}