package com.whitepowder.slopeRecognizer;

import java.io.Serializable;
import java.util.ArrayList;

import com.whitepowder.userManagement.User;

public class RecognizedSlope implements Serializable{

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused") private String _token = User.getUserInstance().getToken();
	int slope_id;
	
	ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

	RecognizedSlope(int id){
		slope_id = id;
	}
	
	public void add(Coordinate p){
		coordinates.add(p);
	};
	
	public void clearAll(){
		slope_id=0;
		coordinates.clear();
	};
	
	public int getSlop_code() {
		return slope_id;
	}

	public void setSlop_code(int slop_code) {
		this.slope_id = slop_code;
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