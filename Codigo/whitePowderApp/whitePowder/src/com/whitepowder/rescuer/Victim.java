package com.whitepowder.rescuer;

import java.util.Calendar;
import java.util.Date;

import com.whitepowder.skier.Coordinate;

public class Victim {
	private String username;
	private Coordinate location;
	private Date accidentTime;
	
	public Victim(String user,double x, double y){
		username=user;
		location = new Coordinate(x,  y);
		accidentTime = Calendar.getInstance().getTime();
	}

	public Coordinate getLocation() {
		return location;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
