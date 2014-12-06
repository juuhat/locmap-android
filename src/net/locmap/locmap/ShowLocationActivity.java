package net.locmap.locmap;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import net.locmap.locmap.models.LocationModel;
import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowLocationActivity extends Activity {
	
	private LocationModel location;
	private LinearLayout imageLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_location);
		
		this.location = (LocationModel) getIntent().getExtras().getParcelable("location");
		updateTextViews();
		
		this.imageLayout = (LinearLayout) findViewById(R.id.layoutShowLocationImages);

		//check and download attached images
		if (this.location.getImages() != null) {
			for (String i : this.location.getImages()) {
				new DownloadImage().execute(Network.imagesUrl + i);
			}
		}
		
		LinearLayout buttons = (LinearLayout) findViewById(R.id.layoutShowLocationButtons);
		// show update/delete buttons if location owner opens activity
		ArrayList<String> ownerTemp = this.location.getOwners();
		String locIdTemp = UIFunctions.getId(this);
		buttons.setVisibility(LinearLayout.INVISIBLE);
		
		for (String ownerID : ownerTemp) {
			if (ownerID.equals(locIdTemp))
				buttons.setVisibility(LinearLayout.VISIBLE);
		}
	}
	
	private void updateTextViews() {
		TextView title = (TextView) findViewById(R.id.txtShowLocationTitle);
		TextView description = (TextView) findViewById(R.id.txtShowLocationDescription);
		
		title.setText(location.getTitle());
		description.setText(location.getDescription());
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data.hasExtra("location")) {
			this.location = (LocationModel) (data.getParcelableExtra("location"));
		}
		
		updateTextViews();
	}
	
	
	/**
	 * Click event for location edit
	 */
	public void btnShowLocationUpdate(View v) {
		Intent intent = new Intent(this, NewLocationActivity.class);
		intent.putExtra("location", this.location);
		startActivityForResult(intent, 0);
	}
	
	/**
	 * Click event for location delete
	 */
	public void btnShowLocationDelete(View v) {
		// TODO
	}
	
	private void addImage(Bitmap bitmap) {
		ImageView imgView = new ImageView(this);
		imgView.setImageBitmap(bitmap);
		imageLayout.addView(imgView);
	}
	
	private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap img = null;
			String url = params[0];
			
			try {
				InputStream in = new URL(url).openStream();
				img = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				//TODO Show error message to user
				Log.e("e", e.getMessage());
			}

			return img;
		}
		
		protected void onPostExecute(Bitmap result) {
			addImage(result);
		}
		
	}
	
}
