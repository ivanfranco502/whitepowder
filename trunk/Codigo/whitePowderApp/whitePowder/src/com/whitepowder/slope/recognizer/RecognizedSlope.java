package com.whitepowder.slope.recognizer;

import java.io.Serializable;
import java.util.ArrayList;

import com.whitepowder.user.management.User;

public class RecognizedSlope implements Serializable{

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused") private String _token = User.getUserInstance().getToken();
	int slope_code;
	
	ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

	RecognizedSlope(int id){
		slope_code = id;
	}
	
	public void add(Coordinate p){
		coordinates.add(p);
	};
	
	public void clearAll(){
		slope_code=0;
		coordinates.clear();
	};
	
	public int getSlop_code() {
		return slope_code;
	}

	public void setSlop_code(int slop_code) {
		this.slope_code = slop_code;
	}

}

class Coordinate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	double x;
	double y;
	
	Coordinate (double px, double py){
		this.x = px;
		this.y = py;
	}
}