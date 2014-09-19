package com.whitepowder.skier.normsAndSigns;

import java.io.IOException;
import java.io.InputStream;

import com.example.whitepowder.R;
import com.whitepowder.utils.ApplicationError;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SignActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skier_activity_nas_sign);
		
		Sign sign = (Sign) getIntent().getSerializableExtra("SELECTED_SIGN");
		
		TextView signName = (TextView) findViewById(R.id.sign_name);
		signName.setText(sign.getName());
		
		SquareImageView signPicture = (SquareImageView) findViewById(R.id.signPicture);
		try {
			InputStream logoBitmap = this.getAssets().open(sign.getImage());
			Bitmap bitmap = BitmapFactory.decodeStream(logoBitmap);
			signPicture.setImageBitmap(bitmap);
			
		} catch (IOException e) {
			new ApplicationError(1000, "Error","No se encuentra la señal de seguridad");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
