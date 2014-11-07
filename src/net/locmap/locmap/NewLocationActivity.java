package net.locmap.locmap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
		
		initGPS();
		
	}
	
	public void btnNewLocationCoordinatesClicked(View view) {
		
		EditText latitude = (EditText) findViewById(R.id.editNewLocationLatitude);
		EditText longitude = (EditText) findViewById(R.id.editNewLocationLongitude);
		
		if (currentLocation != null) {
			latitude.setText(Double.toString(currentLocation.getLatitude()));
			longitude.setText(Double.toString(currentLocation.getLongitude()));
		}
		
	}
	
	private void initGPS() {
        googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
		
        googleApiClient.connect();
        
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			currentLocation = location;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

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

}
