package com.whitepowder.slopeRecognizer;
import java.util.ArrayList;

public class SimplifiedSlopeContainer{

	
	int code;
	ArrayList<SimplifiedSlope> payload;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public ArrayList<SimplifiedSlope> getPayload() {
		return payload;
	}
	public void setPayload(ArrayList<SimplifiedSlope> payload) {
		this.payload = payload;
	}
}

class SimplifiedSlope {
	int slope_id;
	String slope_description;
	int slope_recognized;
	
	public int getSlope_id() {
		return slope_id;
	}
	public void setSlope_id(int slope_id) {
		this.slope_id = slope_id;
	}
	public String getSlope_description() {
		return slope_description;
	}
	public void setSlope_description(String slope_description) {
		this.slope_description = slope_description;
	}
	public int getSlope_recognized() {
		return slope_recognized;
	}
	public void setSlope_recognized(int slope_recognized) {
		this.slope_recognized = slope_recognized;
	}

}


