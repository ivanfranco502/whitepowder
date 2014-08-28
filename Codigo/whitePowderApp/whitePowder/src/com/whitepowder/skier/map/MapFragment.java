package com.whitepowder.skier.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.example.whitepowder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.whitepowder.storage.StorageConstants;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
	
	private static View rootView;
	private GoogleMap mMap=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(rootView == null){
	
			try{
				rootView = inflater.inflate(R.layout.skier_fragment_map, container,false);
			}
			catch (InflateException e){
				//TODO log 
			}
		};
		
		mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.skier_map_fragment)).getMap();
		mMap.setMyLocationEnabled(true);
		drawSlopes();		
		
		return rootView;	
		
	}
	
	@Override
	public void onStart(){
		super.onStart();

	
	}
	
	private void drawSlopes(){
		
		Gson gson = new Gson();
		String data = read_file(getActivity().getApplicationContext(), StorageConstants.DRAWABLE_SLOPES_FILE);	
		DrawableSlopeContainer dsc = gson.fromJson(data, DrawableSlopeContainer.class);
		
		if(dsc.code==200){
			
			for(DrawableSlope ds: dsc.payload){
				MarkerOptions mo = new MarkerOptions();
				mo.position(new LatLng(20,20));
				mMap.addMarker(mo);
				
				PolylineOptions plo = new PolylineOptions();
			    plo.width(6);
			    plo.color(Color.parseColor(ds.getSlope_difficulty_color()));
		
				for(SimpleCoordinate c: ds.slope_coordinates){
					plo.add(new LatLng(c.x, c.y));
				}
				 
				 mMap.addPolyline(plo);
			}						
		};
		
		
	}
	
	public String read_file(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

}
