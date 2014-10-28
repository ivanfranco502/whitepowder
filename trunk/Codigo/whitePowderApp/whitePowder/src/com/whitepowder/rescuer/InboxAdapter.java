package com.whitepowder.rescuer;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InboxAdapter extends ArrayAdapter<Victim>{

	RescuerActivity mContext;
	int layoutResourceId;
	ArrayList<Victim> victimas;
	
	public InboxAdapter(RescuerActivity context, int resource,ArrayList<Victim> objects) {
		
		super(context, resource, objects);
		mContext = context;
		layoutResourceId = resource;
		victimas = objects;

	}
	
	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		return getCustomView(i, convertView, parent);
	}
		
	public View getCustomView(int position, View convertView, ViewGroup parent){
      
		
		View row = convertView;
        
        if(row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
        }

        TextView slopeDesc =  (TextView) row.findViewById(com.example.whitepowder.R.id.rescuer_inbox_item_title);
        slopeDesc.setText(victimas.get(position).getUsername());     
        
		return row;
		
	};
	
	public Victim getItem(int position){
		return victimas.get(position);
	};

}
