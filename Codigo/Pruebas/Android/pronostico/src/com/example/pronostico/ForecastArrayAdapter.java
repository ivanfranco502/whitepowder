package com.example.pronostico;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ForecastArrayAdapter extends ArrayAdapter<Forecast> {
	
	int layoutResourceId;
	MainActivity mContext;
	Forecast[] pronostico;

	public ForecastArrayAdapter(MainActivity context, int listview, Forecast[] pron) {
		super(context,listview,pron);
		layoutResourceId = listview;
		mContext = context;
		pronostico = pron;
	}
	
    public View getView(int i, View convertView, ViewGroup parent) {
    	
        View row = convertView;
        
        if(row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
        }
        
        TextView fecha =  (TextView) row.findViewById(R.id.fecha);
        fecha.setText(pronostico[i].getFecha());
        
        TextView tMin =  (TextView) row.findViewById(R.id.tMin);
        tMin.setText("Temperatura mínima "+pronostico[i].getTemperaturaMin().toString());
        
        TextView tMax =  (TextView) row.findViewById(R.id.tMax);
        tMax.setText("Temperatura máxima "+pronostico[i].getTemperaturaMax().toString());
  
        
        return row;
    }

}
