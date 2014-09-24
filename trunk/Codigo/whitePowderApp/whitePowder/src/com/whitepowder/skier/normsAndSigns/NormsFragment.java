package com.whitepowder.skier.normsAndSigns;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whitepowder.R;
import com.whitepowder.skier.normsAndSigns.SignsFragment.ImageAdapter.ViewHolder;
import com.whitepowder.utils.ApplicationError;


public class NormsFragment extends Fragment {
	
	private List<Norm> norms;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.skier_activity_nas_norms, container,
				false);
		
		TextView title = (TextView) rootView.findViewById(R.id.title);
		title.setText(getString(R.string.norms_title));
		
		getAllNorms();
		
		ListView listViewNorms = (ListView) rootView.findViewById(R.id.listViewNorms);
		listViewNorms.setAdapter(new NormsListAdapter(inflater));
		
		
		return rootView;
	}
	
	private void getAllNorms(){
		norms = new ArrayList();
		Norm norm;
		norm = new Norm(getString(R.string.norm1_name), getString(R.string.norm1_description), "norms/1.respect.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm2_name), getString(R.string.norm2_description), "norms/2.speed.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm3_name), getString(R.string.norm3_description), "norms/3.slope choose.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm4_name), getString(R.string.norm4_description), "norms/4.overtake.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm5_name), getString(R.string.norm5_description), "norms/5.see.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm6_name), getString(R.string.norm6_description), "norms/6.stop.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm7_name), getString(R.string.norm7_description), "norms/7.foot prints.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm8_name), getString(R.string.norm8_description), "norms/8.signs.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm9_name), getString(R.string.norm9_description), "norms/9.help.png");
		norms.add(norm);
		norm = new Norm(getString(R.string.norm10_name), getString(R.string.norm10_description), "norms/10.identification.png");
		norms.add(norm);

	}
	
	
	public class NormsListAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public NormsListAdapter(LayoutInflater inflater){
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return norms.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Norm norm = norms.get(position);

            view = inflater.inflate(R.layout.skier_activity_nas_norms_row, parent, false);
            final ImageView image = (ImageView) view.findViewById(R.id.norm_icon);
            final TextView normName = (TextView) view.findViewById(R.id.norm_title);
            final TextView normDescription = (TextView) view.findViewById(R.id.norm_description);
            

            normName.setText(norm.getName());
            normDescription.setText(norm.getDescription());
            try {
    			InputStream logoBitmap = getActivity().getAssets().open(norm.getImage());
    			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
    			image.setImageBitmap(bitmap);
    			
    		} catch (IOException e) {
    			new ApplicationError(1001, "Error","No se encuentra la norma de seguridad");
    		}
            
            return view;
        }

    }
	
}
