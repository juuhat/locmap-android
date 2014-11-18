package net.locmap.locmap;

import java.io.File;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
	private Uri photoUri;
	private File image;
	private net.locmap.locmap.models.Location createdLocation;
	
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
		
        image = null;
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
		hideKeyboard();
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File tempFile;

		try {
			File tempDir = Environment.getExternalStorageDirectory();
			tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
			
			if (!tempDir.exists()) {
				tempDir.mkdir();
			}
			
			tempFile = File.createTempFile("locmapTmp", ".jpg", tempDir);
			tempFile.delete();
			
		} catch(Exception e) {
			Log.e("err", e.getMessage());
			return;
		}
		
		this.photoUri = Uri.fromFile(tempFile);
		
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
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
		
		changeProgressBarVisibility(true);
		
		EditText title = (EditText) findViewById(R.id.editNewLocationTitle);
		EditText description = (EditText) findViewById(R.id.editNewLocationDescription);
		EditText latitude = (EditText) findViewById(R.id.editNewLocationLatitude);
		EditText longitude = (EditText) findViewById(R.id.editNewLocationLongitude);
		
		String[] params = {title.getText().toString(), description.getText().toString(),
							latitude.getText().toString(), longitude.getText().toString()};
		
		new CreateLocation().execute(params);
	}
	
	
	/**
	 * Hide/show progressBar and createButton
	 */
	public void changeProgressBarVisibility(boolean visible) {
		Button btnCreate = (Button) findViewById(R.id.btnNewLocationCreate);
		ProgressBar progBottom = (ProgressBar) findViewById(R.id.progNewLocationBottom);
		
		if (visible) {
			progBottom.setVisibility(View.VISIBLE);
			btnCreate.setVisibility(View.GONE);
		} else {
			progBottom.setVisibility(View.GONE);
			btnCreate.setVisibility(View.VISIBLE);
		}

	}
	
	
	/**
	 * Called automatically when camera returns results
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	    if (requestCode == 1 && resultCode == RESULT_OK) {
	    	
	    	try {
	    		this.image = new File(photoUri.getPath());
			} catch (Exception e) {
				return;
			}
	    	
	        Bundle extras = data.getExtras();
	        Bitmap thumb = (Bitmap) extras.get("data");
	        ImageView imgView = (ImageView) findViewById(R.id.imgNewLocationPreview);
	        imgView.setImageBitmap(thumb);
	    }

	}
	
	
	/**
	 * Hide soft keyboard in activity
	 * @param activity
	 */
	public void hideKeyboard (){
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	    if (getCurrentFocus() != null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
	 * Gets authentication token from SharedPrefences
	 * @return token
	 */
	private String getToken() {
		SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
		String token = sharedPref.getString("token", "");
		return token;
	}
	
	
	/**
	 * Starts showLocation activity with given location
	 * @param location
	 */
	private void startShowLocationActivity(net.locmap.locmap.models.Location location) {
		Intent intent = new Intent(this, ShowLocationActivity.class);
		intent.putExtra("location", location);	
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
			
			return Network.Post(Network.locationsUrl, json, getToken());
		}
		
		@Override
		protected void onPostExecute(Response res) {			
			if (res.getStatusCode() == 200) {
				createdLocation = new net.locmap.locmap.models.Location(res.getBody());
				
				//If image is taken start upload
				if (image != null) {
					new UploadImage().execute(createdLocation.getId());
				} else {
					changeProgressBarVisibility(false);
					startShowLocationActivity(createdLocation);
				}
				
			} else {
				changeProgressBarVisibility(false);
				//TODO show error to user
			}

		}
		
	}
	
	
	/**
	 * Async task for uploading images
	 * 1. parameter: location id
	 */
	public class UploadImage extends AsyncTask<String, Void, Response> {

		@Override
		protected Response doInBackground(String... params) {
			return Network.Post(Network.imagesUrl, image, params[0], getToken());
		}
		
		@Override
		protected void onPostExecute(Response res) {
			changeProgressBarVisibility(false);
			if (res.getStatusCode() == 200) {
				startShowLocationActivity(createdLocation);
			} else {
				//TODO show error to user
			}

		}
		
	}

}
