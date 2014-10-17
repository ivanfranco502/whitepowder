package com.whitepowder.rescuer;

import java.util.Date;

import com.whitepowder.skier.Coordinate;

public class Victim {
	private Coordinate location;
	private Date accidentTime;
	
	public Victim(double x, double y){
		location = new Coordinate(x,  y);
		accidentTime = new Date();
	}

	public Coordinate getLocation() {
		return location;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}
}
