package net.locmap.locmap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.locmap.locmap.models.LocationModel;
import net.locmap.locmap.models.UserModel;
import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;
import net.locmap.locmap.utils.UIFunctions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationsActivity extends Activity implements
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {
	
	private ListView listUserLocations;
	private ListView listNearLocations;
	private UserModel user;
	private ArrayList<LocationModel> nearLocations;
	
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi locationProvider;
    private LocationRequest locationRequest;
	private Location currentLocation; //gps coords
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		this.listUserLocations = (ListView) findViewById(R.id.listLocationsUsers);
		this.listNearLocations = (ListView) findViewById(R.id.listLocationsNear);
		
		String userId = UIFunctions.getId(this);
		if (userId != null) {
			new GetUserLocations().execute(userId);
		} else {
			//TODO show 'Log in' text to user?
		}
		
		//build googleApiClient with locationServices
        googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
		
        googleApiClient.connect();
		
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
	
	
	/**
	 * Click event for starting NewLocationActity
	 * @param view
	 */
	public void btnLocationsNew(View view) {
		Intent intent = new Intent(this, NewLocationActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * Click event for starting MapActivity with near locations as parameter
	 * @param view
	 */
	public void btnLocationsNearMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("locations", nearLocations);	
		startActivity(intent);
	}
	
	
	/**
	 * Set up "My locations" listView
	 */
	private void fillUserLocations() {
		ArrayList<String> titles = new ArrayList<String>();
		for (LocationModel loc : this.user.getLocations()) {
		    titles.add(loc.getTitle());
		}
		
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.list_text_row, titles);
	    listUserLocations.setAdapter(listAdapter);
	    
	    listUserLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				new GetLocation().execute(user.getLocations().get(position).getId());
			}
		});
	}
	
	
	/**
	 * Set up "Near locations" listView 
	 */
	private void fillNearLocations() {
		ArrayList<String> titles = new ArrayList<String>();
		for (LocationModel loc : this.nearLocations) {
		    titles.add(loc.getTitle());
		}

	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.list_text_row, titles);
	    listNearLocations.setAdapter(listAdapter);
	    
	    listNearLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startShowLocationActivity(nearLocations.get(position));
			}
		});
	}
	
	
	/**
	 * AsyncTask for getting user's locations ("My locations")
	 * 1. parameter: User's ObjectId
	 */
	public class GetUserLocations extends AsyncTask<String, Void, Response> {

		@Override
		protected Response doInBackground(String... params) {		
			return Network.Get(Network.usersUrl + params[0]);
		}
		
		@Override
		protected void onPostExecute(Response res) {
			if (res.getStatusCode() == 200) {
				user = new UserModel(res.getBody());
				fillUserLocations();
			}
		}
	}
	
	
	/**
	 * AsyncTask for getting getting single location
	 * 1. parameter: Location's ObjectId
	 */
	public class GetLocation extends AsyncTask<String, Void, Response> {
		@Override
		protected Response doInBackground(String... params) {
			return Network.Get(Network.locationsUrl + params[0]);
		}
		
		@Override
		protected void onPostExecute(Response res) {
			if (res.getStatusCode() == 200) {
				LocationModel loc = new LocationModel(res.getBody());
				startShowLocationActivity(loc);
			}
		}
		
	}
	
	
	/**
	 * AsyncTask for getting locations near user
	 */
	public class GetNearLocations extends AsyncTask<String, Void, Response> {
		@Override
		protected Response doInBackground(String... params) {
			//TODO read dist from user's preferences
			return Network.Get("http://api.locmap.net/v1/locations" + 
						"?lat=" + currentLocation.getLatitude() +
						"&lon=" + currentLocation.getLongitude() +
						"&dist=40");
		}
		
		@Override
		protected void onPostExecute(Response res) {
			if (res.getStatusCode() == 200) {
				try {
					JSONObject jsonObj = new JSONObject(res.getBody());
					JSONArray jsonArr = jsonObj.getJSONArray("locations");
					nearLocations = new ArrayList<LocationModel>();
					if (jsonArr != null) {
						for (int i = 0; i < jsonArr.length(); i++) {
							nearLocations.add(new LocationModel(jsonArr.getString(i)));
						}
						
						fillNearLocations();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//TODO show error to user
			}
		}
		
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

	
	/**
	 * Called when Google play services is connected
	 */
	@Override
	public void onConnected(Bundle arg0) {
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
		 
		 new GetNearLocations().execute();
		 
	}


	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
