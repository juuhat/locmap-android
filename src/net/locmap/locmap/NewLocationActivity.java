package net.locmap.locmap;

import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Activity for creating new locations
 * @author Juuso Hatakka
 */

public class NewLocationActivity extends Activity implements
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {

    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi locationProvider;
    private LocationRequest locationRequest;
	private Location currentLocation;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newlocation);
		
		//build googleApiClient with locationServices
        googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
		
        googleApiClient.connect();
		
	}
	
	
	/**
	 * Click event for coordinates button
	 * Sets latitude and longitude fields to most recent values
	 * @param view
	 */
	public void btnNewLocationCoordinatesClicked(View view) {
		
		EditText latitude = (EditText) findViewById(R.id.editNewLocationLatitude);
		EditText longitude = (EditText) findViewById(R.id.editNewLocationLongitude);
		
		if (currentLocation != null) {
			latitude.setText(Double.toString(currentLocation.getLatitude()));
			longitude.setText(Double.toString(currentLocation.getLongitude()));
		}
		
	}
	
	
	/**
	 * Click event for camera. Sends intent to capture image
	 * @param view
	 */
	public void btnNewLocationCamera(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, 1);
		}
	}
	
	
	/**
	 * Click event for creating location.
	 * Gets parameters from EditText fields and creates request to API
	 * @param view
	 */
	public void btnNewLocationCreate(View view) {
		EditText title = (EditText) findViewById(R.id.editNewLocationTitle);
		EditText description = (EditText) findViewById(R.id.editNewLocationDescription);
		EditText latitude = (EditText) findViewById(R.id.editNewLocationLatitude);
		EditText longitude = (EditText) findViewById(R.id.editNewLocationLongitude);
		
		String[] params = {title.getText().toString(), description.getText().toString(),
							latitude.getText().toString(), longitude.getText().toString()};
		
		new CreateLocation().execute(params);
	}
	
	
	/**
	 * Called automatically when camera returns results
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1 && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        ImageView imgView = (ImageView) findViewById(R.id.imgNewLocationPreview);
	        imgView.setImageBitmap(imageBitmap);
	    }

	}
	
	
	/**
	 * Called by locationListener when new location is available
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			currentLocation = location;
		}
	}

	
	/**
	 * Called when googleApiClient is ready
	 */
	@Override
	public void onConnected(Bundle bundle) {
		 locationProvider = LocationServices.FusedLocationApi;
		 Location lastLocation = locationProvider.getLastLocation(googleApiClient);
		 
		 if (lastLocation != null) {
			 currentLocation = lastLocation;
		 }
		 
		//Set up continuous location updates
		 locationRequest = LocationRequest.create();
		 locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		 locationRequest.setInterval(1000); //milliseconds
		 locationProvider.requestLocationUpdates(googleApiClient, locationRequest, this);
		 
	}
	

	/**
	 * Called if googleApiClient connection fails
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
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
	
	
	/**
	 * Starts showLocation activity with given location
	 * @param location
	 */
	private void startShowLocationActivity(net.locmap.locmap.models.Location location) {
		Intent intent = new Intent(this, ShowLocationActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * Async task for creating location
	 * Needs four parameters:
	 * 
	 * 1. parameter: Title
	 * 2. parameter: Description
	 * 3. paramater: Latitude
	 * 4. parameter: Longitude
	 */
	public class CreateLocation extends AsyncTask<String, Void, Response> {

		/**
		 * Converts String data to JSON
		 * Calls Network.Post for creating new location
		 */
		@Override
		protected Response doInBackground(String... params) {
			if (params.length < 4)
				this.cancel(true);
			
			String json = "";
			JSONObject jsonObj = new JSONObject();

			try {
				jsonObj.accumulate("title", params[0]);
				jsonObj.accumulate("description", params[1]);
				jsonObj.accumulate("latitude", params[2]);
				jsonObj.accumulate("longitude", params[3]);
				json = jsonObj.toString();
			} catch (JSONException e) {
				Log.d("JSON convert", "String to JSON fail @ createLocation");
			}
			
			return Network.Post(Network.locationsUrl, json);
		}
		
		/**
		 * 
		 */
		@Override
		protected void onPostExecute(Response res) {
			//TODO upload pictures
			
			//TODO set location data
			net.locmap.locmap.models.Location newLocation = new net.locmap.locmap.models.Location();
			startShowLocationActivity(newLocation);
		}
		
	}

}
