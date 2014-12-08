package net.locmap.locmap;

import java.util.ArrayList;
import java.util.HashMap;

import net.locmap.locmap.models.LocationModel;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {

    private GoogleMap myMap;
    private HashMap<Marker, LocationModel> markers;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi locationProvider;
    private LocationRequest locationRequest;
    private ArrayList<LocationModel> locations;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		this.locations = getIntent().getExtras().getParcelableArrayList("locations");
		
		// Check Google Play availability status
		if (serviceAvailable()) {
			initGPS();
			initMap();
			
			if (this.locations != null) {
				setMarkers();
			}
			
		}
	}
	
	
	/**
	 * Add markers to map
	 */
	private void setMarkers() {
		this.markers = new HashMap<Marker, LocationModel>();
		for (LocationModel i : this.locations) {
			Marker m = myMap.addMarker(new MarkerOptions()
				.position(new LatLng(i.getLatitude(), i.getLongitude()))
				.title(i.getTitle()));
			this.markers.put(m, i);
		}
	}
	
	
	/**
	 * Check if Google Play Services is running
	 * @return true/false
	 */
	private boolean serviceAvailable() {
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
		
		myMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				LocationModel loc = markers.get(marker);
				startShowLocationActivity(loc);
			}
		});
		
	}

	
	/**
	 * Starts showLocation activity with given location
	 * @param location
	 */
	private void startShowLocationActivity(LocationModel location) {
		Intent intent = new Intent(this, ShowLocationActivity.class);
		intent.putExtra("location", location);	
		startActivity(intent);
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

