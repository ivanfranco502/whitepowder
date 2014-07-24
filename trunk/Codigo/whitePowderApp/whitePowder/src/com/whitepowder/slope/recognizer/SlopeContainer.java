package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

public class SlopeContainer {
	int code;
	ArrayList<SimplifiedSlope> payload;
}

class SimplifiedSlope {
	int slop_id;
	String slop_description;
	int slop_recognized;
}
