package com.whitepowder.slope.recognizer;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SlopeSpinnerAdapter extends ArrayAdapter<SimplifiedSlope> implements SpinnerAdapter {

	int layoutResourceId;
	SlopeRecognizerActivity mContext;
	ArrayList<SimplifiedSlope> slopes;
	
	public SlopeSpinnerAdapter(SlopeRecognizerActivity context, ArrayList<SimplifiedSlope> slps,int resource) {
		super(context, resource,slps);
		mContext = context;
		layoutResourceId = resource;
		slopes = slps;
	}
	
	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		return getCustomView(i, convertView, parent);
	}
	
	public View getDropDownView(int i, View convertView, ViewGroup parent) {
	    return getCustomView(i, convertView, parent);
	}
	
	public View getCustomView(int position, View convertView, ViewGroup parent){
      
		
		View row = convertView;
        
        if(row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
        }

        TextView slopeDesc =  (TextView) row.findViewById(com.example.whitepowder.R.id.slope_recognition_item_text);
        slopeDesc.setText(slopes.get(position).slope_description);
        
        
		return row;
		
	}

}
