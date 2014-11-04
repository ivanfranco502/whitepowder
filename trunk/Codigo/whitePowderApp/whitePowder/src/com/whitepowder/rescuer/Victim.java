package com.whitepowder.rescuer;

public class Victim {
	private String username=null;
	private int id=-1;
	Double x =null;
	Double y=null;
	

	public String getUsername() {
		return username;
	};

	public int getId() {
		return id;
	};

	public Double getX() {
		return x;
	};

	public Double getY() {
		return y;
	};
	
	public void setId(int acc_id){
		this.id = acc_id;
	};
	
	@Override
	public boolean equals(Object o) {
		Victim vic = (Victim)o;
		if(vic.getId()==this.getId()){
			return true;
		};
		
		return false;
	};


}
