package net.locmap.locmap;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity implements
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {

    private GoogleMap myMap;
    //private ArrayList<Marker> markers;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi locationProvider;
    private LocationRequest locationRequest;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		// Check Google Play availability status
		if (serviceAvailable()) {
			initGPS();
			initMap();
		}
	}
	
	// is Google Play Services running
	private boolean serviceAvailable() {
		// Getting Google Play availability status
		int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (errorCode != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
			return false;
		} else {
			return true;
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
	
	private void initMap() {
		// find map fragment
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		// show device location
        myMap.setMyLocationEnabled(true);

		// set map type
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
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
			myMap.moveCamera(CameraUpdateFactory.newLatLngZoom
					(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 14));
		}
		
		//Set up continuous location updates
		/*locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); //milliseconds
        locationProvider.requestLocationUpdates(googleApiClient, locationRequest, this);*/
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

