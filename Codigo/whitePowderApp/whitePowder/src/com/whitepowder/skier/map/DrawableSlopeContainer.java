package com.whitepowder.skier.map;

import java.util.ArrayList;

public class DrawableSlopeContainer {
	int code;
	ArrayList<DrawableSlope> payload;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public ArrayList<DrawableSlope> getPayload() {
		return payload;
	}
	public void setPayload(ArrayList<DrawableSlope> payload) {
		this.payload = payload;
	}
}
