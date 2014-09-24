package com.whitepowder.skier.normsAndSigns;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.whitepowder.R;
import com.whitepowder.utils.ApplicationError;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SignsFragment extends Fragment {
	
	private List<Sign> signs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getAllSigns();
		
		GridView signsGrid = (GridView) inflater.inflate(R.layout.skier_activity_nas_sign_grid, container, false);
        signsGrid.setAdapter(new ImageAdapter(inflater));
        signsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView signPicture = (ImageView)view.findViewById(R.id.signPicture);
                rotateElement(signPicture);
            }
        });
        return signsGrid;
	}
	
	
	private void getAllSigns(){
		signs = new ArrayList();
		Sign sign;
		sign = new Sign(getString(R.string.sign1_name), getString(R.string.sign1_description), "signs/avalanche.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign2_name), getString(R.string.sign2_description), "signs/easiest.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign3_name), getString(R.string.sign3_description), "signs/more difficult.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign4_name), getString(R.string.sign4_description), "signs/most difficult.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign5_name), getString(R.string.sign5_description), "signs/expert only.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign6_name), getString(R.string.sign6_description), "signs/insane difficult.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign7_name), getString(R.string.sign7_description), "signs/ski patrol.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign8_name), getString(R.string.sign8_description), "signs/terrain park.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign9_name), getString(R.string.sign9_description), "signs/zona resbaladiza.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign10_name), getString(R.string.sign10_description), "signs/snow19.png");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign11_name), getString(R.string.sign11_description), "signs/ski slope.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign12_name), getString(R.string.sign12_description), "signs/ice skiing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign13_name), getString(R.string.sign13_description), "signs/skiing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign14_name), getString(R.string.sign14_description), "signs/snowboarding.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign15_name), getString(R.string.sign15_description), "signs/downhill skiing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign16_name), getString(R.string.sign16_description), "signs/ski jumping.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign17_name), getString(R.string.sign17_description), "signs/tobogganing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign18_name), getString(R.string.sign18_description), "signs/snowmobiling.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign19_name), getString(R.string.sign19_description), "signs/prohibido downhill skiing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign20_name), getString(R.string.sign20_description), "signs/prohibido ski jumping.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign21_name), getString(R.string.sign21_description), "signs/prohibido tobogganing.jpg");
		signs.add(sign);
		sign = new Sign(getString(R.string.sign22_name), getString(R.string.sign22_description), "signs/prohibido snowmobiling.jpg");
		signs.add(sign);

			
	}
	
	
	private void rotateElement(final ImageView im) {
        if (im != null) {
            ObjectAnimator oa = null;
            if(Math.random()>0.5)
                oa = ObjectAnimator.ofFloat(im, "rotationX", 360, 0);
            else
                oa = ObjectAnimator.ofFloat(im, "rotationY", 360, 0);
            oa.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {
                    final Intent i = new Intent(getActivity(), SignActivity.class);
                    i.putExtra("SELECTED_SIGN", (Sign)im.getTag());
                    startActivity(i);
                }

                @Override
                public void onAnimationCancel(Animator animator) {}

                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            oa.setDuration(500).start();
        }
    }
	
	
	
	public class ImageAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public ImageAdapter(LayoutInflater inflater){
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return signs.size();
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
            final Sign sign = signs.get(position);

            final ViewHolder holder = new ViewHolder();
            view = inflater.inflate(R.layout.skier_activity_nas_sign_element, parent, false);
            holder.imageView = (ImageView) view.findViewById(R.id.signPicture);
            holder.signName = (TextView) view.findViewById(R.id.signName);
            
            holder.imageView.setTag(sign);

            holder.signName.setText(sign.getName());
            try {
    			InputStream logoBitmap = getActivity().getAssets().open(sign.getImage());
    			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
    			holder.imageView.setImageBitmap(bitmap);
    			
    		} catch (IOException e) {
    			new ApplicationError(1000, "Error","No se encuentra la señal de seguridad");
    		}
            
            return view;
        }

        class ViewHolder {
            ImageView imageView;
            TextView signName;
        }
    }
}
