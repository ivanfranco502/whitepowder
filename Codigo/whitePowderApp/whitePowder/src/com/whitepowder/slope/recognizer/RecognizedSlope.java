package com.whitepowder.slope.recognizer;

import java.io.Serializable;
import java.util.ArrayList;

public class RecognizedSlope implements Serializable{

	private static final long serialVersionUID = 1L;

	int slop_code;
	
	ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

	RecognizedSlope(int id){
		slop_code = id;
	}
	
	public void add(Coordinate p){
		coordinates.add(p);
	};
	
	public void clearAll(){
		slop_code=0;
		coordinates.clear();
	};
	
	public int getSlop_code() {
		return slop_code;
	}

	public void setSlop_code(int slop_code) {
		this.slop_code = slop_code;
	}

}

class Coordinate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	double posX;
	double posY;
	
	Coordinate (double x, double y){
		posX = x;
		posY = y;
	}
}