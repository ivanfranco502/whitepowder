package com.whitepowder.skier;

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
	private String amenities;
	private int maximum_height;
	private int minimum_height;
	private String season_since;
	private String season_till;
	private String location;
	private List<Day> days;
	
	public String getAmenities(){
		return amenities;
	}
	public int getMaximumHeight(){
		return maximum_height;
	}
	public int getMinimumHeight(){
		return minimum_height;
	}
	public String getSeasonSince(){
		return season_since;
	}
	public String getSeasonTill(){
		return season_till;
	}
	public String getLocation(){
		return location;
	}
	public List<Day> getDays(){
		return days;
	}
	
}

class Day {
	private String name;
	private String start_hour;
	private String end_hour;
	private boolean close;
	
	public String getName(){
		return name;
	}
	public String getStartHour(){
		return start_hour;
	}
	public String getEndHour(){
		return end_hour;
	}
	public boolean getClose(){
		return close;
	}
}