package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

public class SlopeContainer {
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
	int slop_id;
	String slop_description;
	int slop_recognized;
	
	public int getSlop_id() {
		return slop_id;
	}
	public void setSlop_id(int slop_id) {
		this.slop_id = slop_id;
	}
	public String getSlop_description() {
		return slop_description;
	}
	public void setSlop_description(String slop_description) {
		this.slop_description = slop_description;
	}
	public int getSlop_recognized() {
		return slop_recognized;
	}
	public void setSlop_recognized(int slop_recognized) {
		this.slop_recognized = slop_recognized;
	}

}


