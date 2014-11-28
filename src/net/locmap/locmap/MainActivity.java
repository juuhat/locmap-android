package net.locmap.locmap;

import java.util.ArrayList;

import net.locmap.locmap.models.Location;
import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button login;

	/**
	 * Set up view. If user is logged in, changes log in button to log out
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.btnMainLogIn);

		drawLoginOrLogout();
	}

	/**
	 * When returned from activityForResult
	 * - Log in
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		drawLoginOrLogout();
	}
	
	/**
	 * If sharedpref contains access-token, draw logout functions
	 * Else draw login
	 */
	private void drawLoginOrLogout() {
		// draw login/logout
		if (UIFunctions.getToken(this) != null) {
			drawLogOut();
		} else {
			drawLogIn();
		}
	}

	/**
	 * Sets text and clickhandler to Login/logout button
	 */
	private void drawLogOut() {
		login.setText(getString(R.string.logout));
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logOut();
			}
		});
	}

	/**
	 * Sets text and clickhandler to Login/logout button
	 */
	private void drawLogIn() {
		login.setText(getString(R.string.title_activity_log_in));
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnMainLogIn(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Logs out user
	 */
	private void logOut() {
		// TODO: send logout request to API
		UIFunctions.showToast(this, getString(R.string.logged_out));
		UIFunctions.clearToken(this);
		drawLogIn();
	}

	/**
	 * Click event for log in button
	 * 
	 * @param view
	 */
	public void btnMainLogIn(View view) {
		Intent intent = new Intent(this, LogInActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * Click event for locations button
	 * 
	 * @param view
	 */
	public void btnMainLocations(View view) {

		// TODO change to Locations activity
		Intent intent = new Intent(this, ShowLocationActivity.class);

		// test data
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
			// Start map activity
			Intent intent = new Intent(this, MapActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_locations) {
			// Start locations
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
