package com.whitepowder.skier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BasicInformationResponse{
	private int code;
	private BasicInformation payload;
	
	public int getCode(){
		return code;
	}
	public BasicInformation getBasicInformation(){
		return payload;
	}
}

class BasicInformation {
	private String gein_center_name;
	private String gein_amenities;
	private int gein_maximum_height;
	private int gein_minimum_height;
	private String gein_season_since;
	private String gein_season_till;
	private String gein_location;
	private String gein_details;
	private ArrayList<Day> _schedules;
	private List<Slope> _slopes;
	
	public String getCenterName(){
		return gein_center_name;
	}
	public String getAmenities(){
		return gein_amenities;
	}
	public String getMaximumHeight(){
		return "Altura máxima: " + gein_maximum_height + " m.s.n.m.";
	}
	public String getMinimumHeight(){
		return "Altura mínima: " + gein_minimum_height + " m.s.n.m.";
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
	public List<Slope> getSlopes(){
		return _slopes;
	}
	
	public String getSeason(){
		return "Temporada: desde " + getSeasonSince() + " hasta " + getSeasonTill() + ".";
	}
	
}

class Day {
	private String hoda_day;
	private String hoda_start_hour;
	private String hoda_end_hour;
	private boolean hoda_close;
	
	public String getDay(){
		return hoda_day;
	}
	public String getStartHour(){
		return hoda_start_hour;
	}
	public String getEndHour(){
		return hoda_end_hour;
	}
	public boolean getClose(){
		return hoda_close;
	}
	
	public String toString(){
		if(getClose()){
			return  getDay() + ": cerrado.";
		}
		else{
			return getDay() + ": " + getStartHour() + " - " + getEndHour();
		}
		
	}
}

class Slope{
	private int slop_id;
	private String slop_description;
	private int slop_length;
	private Dificulty slop_dificulty;
	private List<Coordinates> _coordinates;
	
	public int getId(){
		return slop_id;
	}
	public String getDescription() {
		return slop_description;
	}
	public int getLength() {
		return slop_length;
	}
	public Dificulty getDificulty() {
		return slop_dificulty;
	}
}

class Dificulty{
	private String sldi_description;
	private String sldi_color;
	
	public String getDescription() {
		return sldi_description;
	}
	public String getColor() {
		return sldi_color;
	}	
}

class Coordinates{
	private Coordinate slco_coordinate;
}

class Coordinate{
	private double coor_x;
	private double coor_y;
}