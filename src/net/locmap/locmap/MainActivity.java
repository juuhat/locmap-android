package net.locmap.locmap;

import java.util.ArrayList;

import net.locmap.locmap.models.Location;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * Click event for log in button
	 * @param view
	 */
	public void btnMainLogIn(View view) {
		Intent intent = new Intent(this, LogInActivity.class);
		startActivity(intent);
	}

	/**
	 * Click event for locations button
	 * @param view
	 */
	public void btnMainLocations(View view) {
		
		//TODO change to Locations activity
		Intent intent = new Intent(this, ShowLocationActivity.class);
		
		//test data
		Location location = new Location();
		location.setId("545b73fe106958212eaeabdf");
		location.setTitle("Test");
		location.setDescription("Test description");
		ArrayList<String> imgs = new ArrayList<String>();
		imgs.add("545b7436106958212eaeabe0");
		imgs.add("5475c4a119bedfd609b78c7d");
		location.setImages(imgs);
		
		intent.putExtra("location", location);
		
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		int id = item.getItemId();
		if (id == R.id.action_settings) {
		} else if (id == R.id.action_map) {
			//Start map activity
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_locations) {
			//Start locations
			Intent intent = new Intent(this, ShowLocationActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_new_location) {
			Intent intent = new Intent(this, NewLocationActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_login) {
			Intent intent = new Intent(this, LogInActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
