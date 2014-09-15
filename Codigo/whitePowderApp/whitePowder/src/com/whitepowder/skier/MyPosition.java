package com.whitepowder.skier;

public class MyPosition {
	
	String _token;
	Coordinate coordinate;
	
	public MyPosition(String tok, Coordinate cord) {
		_token = tok;
		coordinate = cord;
	};
	
}
class Coordinate{
	
	double x;
	double y;
	
	public Coordinate(double cx, double cy) {
		x = cx;
		y = cy;
	};
}

class PositionUploadResponse{
	int code;
	String payload;
}
