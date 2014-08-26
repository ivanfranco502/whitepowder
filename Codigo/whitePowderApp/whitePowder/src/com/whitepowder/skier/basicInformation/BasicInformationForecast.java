package com.whitepowder.skier.basicInformation;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.SparseArray;

public class BasicInformationForecast {
	int id;
	String fecha;
	Double temperaturaMax;
	Double temperaturaMin;
	
	int weatherId;
	String weatherMain;
	String weatherIcon;
	
	SparseArray<String> descriptionMap;
	SparseArray<String> daysOfTheWeek;
	SparseArray<String> months;
	
	public BasicInformationForecast(){
		descriptionMap = new SparseArray<String>();
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
		
		daysOfTheWeek = new SparseArray<String>();
		daysOfTheWeek.put(1, "Domingo");
		daysOfTheWeek.put(2, "Lunes");
		daysOfTheWeek.put(3, "Martes");
		daysOfTheWeek.put(4, "Miércoles");
		daysOfTheWeek.put(5, "Jueves");
		daysOfTheWeek.put(6, "Viernes");
		daysOfTheWeek.put(7, "Sábado");
		
		months = new SparseArray<String>();
		months.put(0, "enero");
		months.put(1, "febrero");
		months.put(2, "marzo");
		months.put(3, "abril");
		months.put(4, "mayo");
		months.put(5, "junio");
		months.put(6, "julio");
		months.put(7, "agosto");
		months.put(8, "septiembre");
		months.put(9, "octubre");
		months.put(10, "noviembre");
		months.put(11, "diciembre");
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@SuppressLint("SimpleDateFormat")
	public String getFecha() {
		Date fechaDate = null;
		try{
		fechaDate = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
		}catch(ParseException e){}
		
		if (fechaDate != null){
			Calendar c = Calendar.getInstance();
			c.setTime(fechaDate);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			int dayOFMonth = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH);
			return daysOfTheWeek.get(dayOfWeek) + " " + dayOFMonth + " de " + months.get(month);
		}
		else{
			return fecha;
		}
		
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getTemperaturaMax() {
		return "Temperatura máxima: " + Math.round(temperaturaMax) + " °C";
	}
	public void setTemperaturaMax(Double temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}
	public String getTemperaturaMin() {
		return "Temperatura mínima: " + Math.round(temperaturaMin) + " °C";
	}
	public void setTemperaturaMin(Double temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
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
