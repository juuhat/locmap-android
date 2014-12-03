package net.locmap.locmap;

import java.util.ArrayList;

import net.locmap.locmap.models.LocationModel;
import net.locmap.locmap.models.UserModel;
import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;
import net.locmap.locmap.utils.UIFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationsActivity extends Activity {
	private ListView listUserLocations;
	private ArrayAdapter<String> listAdapter; 
	private UserModel user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		
		this.listUserLocations = (ListView) findViewById(R.id.listLocationsUsers);
 
		String userId = UIFunctions.getId(this);
		if (userId != null) {
			new GetUserLocations().execute(userId);
		} else {
			//TODO show 'Log in' text to user?
		}
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
	 * Set up "My locations" listView
	 */
	private void fillUserLocations() {
		ArrayList<String> userLocations = new ArrayList<String>();
		for (LocationModel loc : this.user.getLocations()) {
		    userLocations.add(loc.getTitle());
		}
		
	    listAdapter = new ArrayAdapter<String>(this, R.layout.list_text_row, userLocations);
	    listUserLocations.setAdapter( listAdapter );
	    
	    listUserLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				new GetLocation().execute(user.getLocations().get(position).getId());
				//startShowLocationActivity(user.getLocations().get(position));
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
	
}
