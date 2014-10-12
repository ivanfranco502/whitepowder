package com.whitepowder.skier;

public class MyPosition {
	
	String _token;
	Coordinate coordinate;
	
	public MyPosition(String tok, Coordinate cord) {
		_token = tok;
		coordinate = cord;
	};
	
}


class PositionUploadResponse{
	int code;
	String payload;
}
