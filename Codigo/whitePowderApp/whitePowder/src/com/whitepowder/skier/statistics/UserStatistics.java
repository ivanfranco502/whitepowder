package com.whitepowder.skier.statistics;

import java.util.Date;

import android.location.Location;

public class UserStatistics {
	float maxSpeed=0;
	Date maxSpeedDate=null;
	double maxAltitude=0;
	Date maxAltitudeDate=null;
	float averageSpeed=0;
	int speedMeditions=0;
	Location lastKnownLocation=null;
	float totalDistance=0;

public float getMaxSpeed() {
	return maxSpeed;
}

public void setMaxSpeed(float maxSpeed) {
	this.maxSpeed = maxSpeed;
}

public Date getMaxSpeedDate() {
	return maxSpeedDate;
}

public void setMaxSpeedDate(Date maxSpeedDate) {
	this.maxSpeedDate = maxSpeedDate;
}

public float getAverageSpeed() {
	return averageSpeed;
}

public void setAverageSpeed(float averageSpeed) {
	this.averageSpeed = averageSpeed;
}

public double getMaxAltitude() {
	return maxAltitude;
}

public void setMaxAltitude(double maxAltitude) {
	this.maxAltitude = maxAltitude;
}

public Date getMaxAltitudeDate() {
	return maxAltitudeDate;
}

public void setMaxAltitudeDate(Date maxAltitudeDate) {
	this.maxAltitudeDate = maxAltitudeDate;
}

public float getTotalDistance() {
	return totalDistance;
}

public void setTotalDistance(float totalDistance) {
	this.totalDistance = totalDistance;
}

public int getSpeedMeditions() {
	return speedMeditions;
}

public void setSpeedMeditions(int speedMeditions) {
	this.speedMeditions = speedMeditions;
}

public Location getLastKnownLocation() {
	return lastKnownLocation;
}

public void setLastKnownLocation(Location lastKnownLocation) {
	this.lastKnownLocation = lastKnownLocation;
};

}
