package net.locmap.locmap;

import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;
import net.locmap.locmap.utils.UIFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
	 * When returned from activityForResult - Log in
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		drawLoginOrLogout();
	}

	private Activity getActivity() {
		return this;
	}

	/**
	 * If sharedpref contains access-token, draw logout functions Else draw
	 * login
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
	 * Logs out user If user has lost Internet connection, doesn't allow to log
	 * out, because access-token cannot be reset at serverside
	 */
	private void logOut() {
		new Logout().execute();
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
		Intent intent = new Intent(this, LocationsActivity.class);
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

	/**
	 * Logs out user If no network connection detected, access-token is not
	 * cleared from API
	 */
	public class Logout extends AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... params) {
			return Network.Post(Network.logoutUrl, "",
					UIFunctions.getToken(getActivity()));
		}

		protected void onPostExecute(Response res) {
			int statusCode = res.getStatusCode();
			if (statusCode == 200) {
				Log.d("LOGOUT", "Logged out successfully");
			} else {
				Log.e("LOGOUT", "Not really logged out, only token cleared from appsettings");
			}
			UIFunctions.showToast(getActivity(),
					getString(R.string.logged_out));
			UIFunctions.clearToken(getActivity());
			drawLogIn();
		}

	}
}
