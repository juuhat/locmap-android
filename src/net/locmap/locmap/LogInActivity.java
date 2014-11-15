package net.locmap.locmap;

import net.locmap.locmap.utils.Network;
import net.locmap.locmap.utils.Response;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Functions for user to log in
 * @author Janne Heikkinen
 */
public class LogInActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
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
	
	/**
	 * Click event for register hyperlink. Open Register -activity
	 * @param v
	 */
	public void onRegisterClick(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	
	/**
	 * Click event for Log In -button.
	 * @param v
	 */
	public void btnLogIn(View v) {
		EditText username = (EditText) findViewById(R.id.editLogInUser);
		EditText password = (EditText) findViewById(R.id.editLogInPassword);
		
		String[] params = {username.getText().toString(), password.getText().toString()};
		
		new LogIn().execute(params);
	}
	
	/**
	 * Saves token in SharedPreferences
	 * @param token
	 */
	private void saveToken(String token) {
		SharedPreferences sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("token", token);
		editor.commit();
	}
	
	
	/**
	 * Sends LogIn request to API.
	 * Execute needs two parameters.
	 * 
	 *  1. parameter: Username
	 *  2. parameter: Password
	 */
	public class LogIn extends AsyncTask<String, Void, Response> {

		/**
		 * Converts string data to JSON and calls Network.Post
		 * Returns HTTP response content as string
		 */
		@Override
		protected Response doInBackground(String... params) {
			if (params.length < 2) 
				this.cancel(true);

			String json = "";
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.accumulate("email", params[0]);
				jsonObj.accumulate("password", params[1]);
				json = jsonObj.toString();
			} catch (JSONException ex) {
				Log.d("JSON Convert", "String to JSON fail @ login");
			}
			
			return Network.Post(Network.loginUrl, json);

		}
		
		@Override
		protected void onPostExecute(Response res) {
			
			TextView resultView = (TextView) findViewById(R.id.txtLogInResult);
			resultView.setText(res.getHeader("x-access-token"));
			
			String token = res.getHeader("x-access-token");
			if (token != null) {
				saveToken(token);
			} else {
				//TODO show error to user
			}
			
		}
	}
	
}
