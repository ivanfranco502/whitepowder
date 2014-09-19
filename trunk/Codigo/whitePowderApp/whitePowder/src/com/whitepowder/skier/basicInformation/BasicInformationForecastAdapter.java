package com.whitepowder.skier.basicInformation;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whitepowder.R;
import com.whitepowder.skier.SkierActivity;
import com.whitepowder.utils.ApplicationError;

public class BasicInformationForecastAdapter extends BaseAdapter{
	private BasicInformationForecastActivity mContext;
	BasicInformationForecast[] basicInformationForecast;

	public BasicInformationForecastAdapter(BasicInformationForecastActivity context, BasicInformationForecast[] bif) {
		super();
		mContext = context;
		basicInformationForecast = bif;
	}
	
	
	 public int getCount() 
	    {
	        // return the number of records in cursor
	        //return basicInformationForecast.length;
		 	return 7;
	    }
	 
    public View getView(int position, View view, ViewGroup parent) {
    	
    	// inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.skier_fragment_basic_information_forecast_row, null);
    	
    	BasicInformationForecast basicForecast = basicInformationForecast[position];
        		
    	TextView forecast_date = (TextView) view.findViewById(R.id.forecast_date);
    	TextView forecast_description = (TextView) view.findViewById(R.id.forecast_description);
    	TextView forecast_min_temp = (TextView) view.findViewById(R.id.forecast_min_temp);
    	TextView forecast_max_temp = (TextView) view.findViewById(R.id.forecast_max_temp);
		ImageView forecast_icon = (ImageView) view.findViewById(R.id.forecast_icon);
    	
		forecast_date.setText(basicForecast.getFecha());
		forecast_description.setText(basicForecast.getWeatherMain());
		forecast_min_temp.setText(basicForecast.getTemperaturaMin());
		forecast_max_temp.setText(basicForecast.getTemperaturaMax());
		
		try {
			InputStream logoBitmap = mContext.getAssets().open("weatherIcons/"+basicForecast.getWeatherIcon()+".png");
			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			forecast_icon.setImageBitmap(bitmap);
			
		} catch (IOException e) {
			new ApplicationError(602, "Error","No se encuentra el icono del pronostico");
		}try {
			InputStream logoBitmap = mContext.getAssets().open("weatherIcons/"+basicForecast.getWeatherIcon()+".png");
			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			forecast_icon.setImageBitmap(bitmap);
			
		} catch (IOException e) {
			new ApplicationError(602, "Error","No se encuentra el icono del pronostico");
		}try {
			InputStream logoBitmap = mContext.getAssets().open("weatherIcons/"+basicForecast.getWeatherIcon()+".png");
			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			forecast_icon.setImageBitmap(bitmap);
			
		} catch (IOException e) {
			new ApplicationError(602, "Error","No se encuentra el icono del pronostico");
		}
  
        return view;
    }
    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
