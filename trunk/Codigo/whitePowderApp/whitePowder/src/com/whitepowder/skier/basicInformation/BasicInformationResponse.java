package com.whitepowder.skier.basicInformation;

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
			return getDay() + ": " + getStartHour() + " h - " + getEndHour() + " h.";
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