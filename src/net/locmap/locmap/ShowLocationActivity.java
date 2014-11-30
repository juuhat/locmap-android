package net.locmap.locmap;

import java.io.InputStream;
import java.net.URL;

import net.locmap.locmap.models.Location;
import net.locmap.locmap.utils.Network;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowLocationActivity extends Activity {
	
	private Location location;
	private LinearLayout imageLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_location);
		
		this.location = (Location) getIntent().getExtras().getParcelable("location");
		updateTextViews();
		
		this.imageLayout = (LinearLayout) findViewById(R.id.layoutShowLocationImages);

		//check and download attached images
		if (this.location.getImages() != null) {
			for (String i : this.location.getImages()) {
				new DownloadImage().execute(Network.imagesUrl + i);
			}
		}

	}
	
	private void updateTextViews() {
		TextView title = (TextView) findViewById(R.id.txtShowLocationTitle);
		TextView description = (TextView) findViewById(R.id.txtShowLocationDescription);
		
		title.setText(location.getTitle());
		description.setText(location.getDescription());
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
