package com.example.pronostico;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

//import java.util.Date;

@SuppressLint("UseSparseArrays")
public class Forecast {
	int id;
	String fecha;
	Double temperaturaMax;
	Double temperaturaMin;
	double presion;
	Double humedad;
	
	int weatherId;
	String weatherMain;
	String weatherIcon;
	
	Map<Integer, String> descriptionMap;
	
	public Forecast(){
		descriptionMap = new HashMap<Integer, String>();
		descriptionMap.put(200,"Tormenta eléctrica");
		descriptionMap.put(201,"Tormenta");
		descriptionMap.put(202,"Tormenta");
		descriptionMap.put(210,"Tormenta eléctrica");
		descriptionMap.put(211,"Tormenta");
		descriptionMap.put(212,"Tormenta");
		descriptionMap.put(221,"Tormenta");
		descriptionMap.put(230,"Tormenta");
		descriptionMap.put(231,"Tormenta");
		descriptionMap.put(232,"Tormenta");
		descriptionMap.put(300,"Llovizna");
		descriptionMap.put(301,"Llovizna");
		descriptionMap.put(302,"Llovizna");
		descriptionMap.put(310,"Llovizna");
		descriptionMap.put(311,"Llovizna");
		descriptionMap.put(312,"Llovizna");
		descriptionMap.put(313,"Llovizna");
		descriptionMap.put(314,"Llovizna");
		descriptionMap.put(321,"Llovizna");
		descriptionMap.put(500,"Lluvia");
		descriptionMap.put(501,"Lluvia");
		descriptionMap.put(502,"Lluvia");
		descriptionMap.put(503,"Lluvia");
		descriptionMap.put(504,"Lluvia");
		descriptionMap.put(511,"Lluvia");
		descriptionMap.put(520,"Lluvia");
		descriptionMap.put(521,"Lluvia");
		descriptionMap.put(522,"Lluvia");
		descriptionMap.put(531,"Lluvia");
		descriptionMap.put(600,"Nieve");
		descriptionMap.put(601,"Nieve");
		descriptionMap.put(602,"Nieve");
		descriptionMap.put(611,"Nieve");
		descriptionMap.put(612,"Nieve");
		descriptionMap.put(615,"Nieve");
		descriptionMap.put(616,"Nieve");
		descriptionMap.put(620,"Nieve");
		descriptionMap.put(621,"Nieve");
		descriptionMap.put(622,"Nieve");
		descriptionMap.put(701,"Neblina");
		descriptionMap.put(711,"Humo");
		descriptionMap.put(721,"Niebla");
		descriptionMap.put(731,"Tormenta de arena");
		descriptionMap.put(741,"Niebla");
		descriptionMap.put(751,"Arena");
		descriptionMap.put(761,"Polvo");
		descriptionMap.put(762,"Cenizas volcánicas");
		descriptionMap.put(771,"Ráfagas");
		descriptionMap.put(781,"Tornado");
		descriptionMap.put(800,"Despejado");
		descriptionMap.put(801,"Parcialmente nublado");
		descriptionMap.put(802,"Levemente nublado");
		descriptionMap.put(803,"Nublado");
		descriptionMap.put(804,"Nublado");
		descriptionMap.put(900,"Tornado");
		descriptionMap.put(901,"Tormenta tropical");
		descriptionMap.put(902,"Huracán");
		descriptionMap.put(903,"Frío");
		descriptionMap.put(904,"Caluroso");
		descriptionMap.put(905,"Ventoso");
		descriptionMap.put(906,"Granizo");
		descriptionMap.put(950,"Inestable");
		descriptionMap.put(951,"Agradable");
		descriptionMap.put(952,"Briza");
		descriptionMap.put(953,"Briza");
		descriptionMap.put(954,"Briza");
		descriptionMap.put(955,"Briza");
		descriptionMap.put(956,"Briza");
		descriptionMap.put(957,"Ventoso");
		descriptionMap.put(958,"Temporal");
		descriptionMap.put(959,"Temporal");
		descriptionMap.put(960,"Tormenta");
		descriptionMap.put(961,"Tormenta");
		descriptionMap.put(962,"Huracán");

	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String feha) {
		this.fecha = feha;
	}
	public long getTemperaturaMax() {
		return Math.round(temperaturaMax);
	}
	public void setTemperaturaMax(Double temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}
	public long getTemperaturaMin() {
		return Math.round(temperaturaMin);
	}
	public void setTemperaturaMin(Double temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}
	public double getPresion() {
		return presion;
	}
	public void setPresion(double presion) {
		this.presion = presion;
	}
	public Double getHumedad() {
		return humedad;
	}
	public void setHumedad(Double humedad) {
		this.humedad = humedad;
	}
	public int getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(int weatherId) {
		this.weatherId = weatherId;
	}
	public String getWeatherMain() {
		return descriptionMap.get(this.getWeatherId());
	}
	public void setWeatherMain(String weatherMain) {
		this.weatherMain = weatherMain;
	}
	public String getWeatherIcon() {
		return weatherIcon;
	}
	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}
}
