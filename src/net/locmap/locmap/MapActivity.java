package net.locmap.locmap;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    private GoogleMap myMap;
    //private ArrayList<Marker> markers;
    private LocationManager mLocationManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		initGPS();
		
		// Getting Google Play availability status
		if (serviceAvailable()) {
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
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		long time = 10000; //update frequency in milliseconds
		float dist = 3; //update minimum distance in meters
	    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, dist, mLocationListener);
	}
	
	private void initMap() {
		// find map fragment
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// show device location
        myMap.setMyLocationEnabled(true);

		// set map type
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		// move camera to last known position
		Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			myMap.moveCamera(CameraUpdateFactory.newLatLngZoom
					(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 14));
		}
		
	}
	
	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	};
	
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

