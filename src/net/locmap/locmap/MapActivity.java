package net.locmap.locmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    private GoogleMap myMap;
    private Marker ict;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
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
	
	private void initMap() {
		// find map fragment
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		// show device location
        myMap.setMyLocationEnabled(true);
		// set map type
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// add one marker
		final LatLng ICT = new LatLng(62.2416223, 25.7597309);
		ict = myMap.addMarker(new MarkerOptions()
                          .position(ICT)
                          .title("JAMK/ICT"));
		// point to jamk/ict
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ICT, 14));
		// marker listener
		myMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	        @Override
	        public boolean onMarkerClick(final Marker marker) {
	        	if (marker.equals(ict)) {
	        		Toast.makeText(getApplicationContext(), "Marker = " + marker.getTitle(), Toast.LENGTH_SHORT).show();
	        		return true;
	        	}
	        	return false;
	        }
		});	
	}
	
}

